package ch.epfl.alpano.summit;

import static ch.epfl.alpano.summit.GazetteerParser.readSummitsFrom;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import ch.epfl.alpano.GeoPoint;

public class GazetteerParserTest {
    @Test(expected = IOException.class)
    public void parserFailsOnNonExistantFile() throws IOException {
        readSummitsFrom(new File("/   /d:/ééé"));
    }

    @Test(expected = IOException.class)
    public void parserFailsOnGarbageLine() throws IOException {
        readSummitsFrom(tempFileWithLines("blabla"));
    }

    @Test(expected = IOException.class)
    public void parserFailsOnInvalidLongitude() throws IOException {
        String l = "  7:25:1x 45:08:25  1325  R0 E07 BA MONTE CURT";
        readSummitsFrom(tempFileWithLines(l));
    }

    @Test(expected = IOException.class)
    public void parserFailsOnInvalidLatitude() throws IOException {
        String l = "  7:25:12 45:08:2_  1325  R0 E07 BA MONTE CURT";
        readSummitsFrom(tempFileWithLines(l));
    }

    @Test(expected = IOException.class)
    public void parserFailsOnInvalidElevation() throws IOException {
        String l = "  7:25:12 45:08:25  leet  R0 E07 BA MONTE CURT";
        readSummitsFrom(tempFileWithLines(l));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void summitListIsUnmodifiable() throws IOException {
        String l = "  7:01:02 46:32:56  2002  H1 B01 D7 LE MOLESON";
        readSummitsFrom(tempFileWithLines(l)).clear();
    }

    @Test
    public void parserWorksOnValidFile() throws IOException {
        List<Summit> summits = Arrays.asList(
                new Summit("A MONT UN", hmsPoint(7, 30, 00, 15, 16, 17), 30),
                new Summit("B MONT ZWEI", hmsPoint(6, 12, 34, 1, 23, 45), 10),
                new Summit("C MONTE TRE", hmsPoint(1, 33, 33, 15, 66, 66), 1000),
                new Summit("D MONT AU NOM TRES LONG", hmsPoint(5, 00, 00, 5, 00, 00), 8000));
        String[] formattedSummits = new String[summits.size()];
        int i = 0;
        for (Summit s: summits)
            formattedSummits[i++] = formatSummit(s);
        File f = tempFileWithLines(formattedSummits);
        List<Summit> readSummits = new ArrayList<>(readSummitsFrom(f));
        assertEquals(summits.size(), readSummits.size());

        readSummits.sort(Comparator.comparing(Summit::name));
        Iterator<Summit> expectedIt = summits.iterator();
        Iterator<Summit> actualIt = readSummits.iterator();
        while (expectedIt.hasNext()) {
            Summit expected = expectedIt.next();
            Summit actual = actualIt.next();
            assertEquals(expected.name(), actual.name());
            assertEquals(expected.elevation(), actual.elevation());
            assertEquals(
                    expected.position().longitude(),
                    actual.position().longitude(),
                    toRadians(1d / 3600d));
            assertEquals(
                    expected.position().latitude(),
                    actual.position().latitude(),
                    toRadians(1d / 3600d));
        }
    }

    private String formatSummit(Summit s) {
        double lon = s.position().longitude();
        double lat = s.position().latitude();
        return String.format("%3d:%02d:%02d %2d:%02d:%02d %5d  R0 E07 BA %s",
                h(lon), m(lon), s(lon), h(lat), m(lat), s(lat), s.elevation(), s.name());
    }

    private static GeoPoint hmsPoint(
            int hLon, int mLon, int sLon,
            int hLat, int mLat, int sLat) {
        return new GeoPoint(hmsToRad(hLon, mLon, sLon), hmsToRad(hLat, mLat, sLat));
    }

    private static double hmsToRad(int h, int m, int s) {
        return toRadians(h + (m / 60d) + (s / 3600d));
    }

    private static int h(double d) {
        return (int)toDegrees(d);
    }

    private static int m(double d) {
        return (int)(toDegrees(d) * 60d) % 60;
    }

    private static int s(double d) {
        return (int)(toDegrees(d) * 3600d) % 60;
    }

    private static File tempFileWithLines(String... lines) throws IOException {
        File f = Files.createTempFile("summits", ".txt").toFile();
        f.deleteOnExit();
        try (BufferedWriter b = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(f), US_ASCII))) {
            for (String l: lines) {
                b.write(l);
                b.newLine();
            }
        }
        return f;
    }
}
