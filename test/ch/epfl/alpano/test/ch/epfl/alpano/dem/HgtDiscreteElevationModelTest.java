package ch.epfl.alpano.dem;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;

public class HgtDiscreteElevationModelTest {
    private final static long HGT_FILE_SIZE = 3601L * 3601L * 2L;
    private static Path FAKE_HGT_DIR, FAKE_HGT_FILE;

    @BeforeClass
    public static void createFakeHgtFiles() throws IOException {
        Path fakeHgtDir = Files.createTempDirectory("hgt");

        Path fakeHgtFile = fakeHgtDir.resolve("empty.hgt");
        try (FileChannel c = FileChannel.open(fakeHgtFile, CREATE_NEW, READ, WRITE)) {
            // make sure the empty hgt file has the right size
            c.map(MapMode.READ_WRITE, 0, HGT_FILE_SIZE).asShortBuffer();
        }

        FAKE_HGT_FILE = fakeHgtFile;
        FAKE_HGT_DIR = fakeHgtDir;
    }

    @AfterClass
    public static void deleteFakeHgtFiles() throws IOException {
        Files.walkFileTree(FAKE_HGT_DIR, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (exc != null)
                    throw exc;
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorFailsWithTooShortName() throws Exception {
        createHgtDemWithFileNamed("N47E010.hg");
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorFailsWithInvalidLatitudeLetter() throws Exception {
        createHgtDemWithFileNamed("N4xE010.hgt");
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorFailsWithInvalidLongitudeLetter() throws Exception {
        createHgtDemWithFileNamed("N47x010.hgt");
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorFailsWithInexistantFile() throws Exception {
        Path p = FAKE_HGT_DIR.resolve("N40E010.hgt");
        try (DiscreteElevationModel d = new HgtDiscreteElevationModel(p.toFile())) {}
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorFailsWithEmptyFile() throws Exception {
        File f = FAKE_HGT_DIR.resolve("N41E010.hgt").toFile();
        try (FileOutputStream s = new FileOutputStream(f)) {
            s.write(0);
        }
        try (DiscreteElevationModel d = new HgtDiscreteElevationModel(f)) {}
    }

    @Test
    public void constructorWorksInEcuador() throws Exception {
        createHgtDemWithFileNamed("S03W078.hgt");
    }

    @Test
    public void extentMatchesFileName() throws Exception {
        int[] lons = new int[] { 1, 7 };
        int[] lats = new int[] { 1, 47 };
        for (int lon: lons) {
            for (int lat: lats) {
                Interval2D expectedExtent = new Interval2D(
                        new Interval1D(lon * 3600, (lon + 1) * 3600),
                        new Interval1D(lat * 3600, (lat + 1) * 3600));
                String hgtFileName = String.format("N%02dE%03d.hgt", lat, lon);
                Path p = copyEmptyHgtFileAs(hgtFileName);
                try (HgtDiscreteElevationModel dem = new HgtDiscreteElevationModel(p.toFile())) {
                    assertEquals(expectedExtent, dem.extent());
                }
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void elevationSampleFailsForIndexNotInExtent() throws Exception {
        String hgtFileName = "N02E002.hgt";
        Path p = copyEmptyHgtFileAs(hgtFileName);
        try (HgtDiscreteElevationModel dem = new HgtDiscreteElevationModel(p.toFile())) {
            dem.elevationSample(10, 10);
        }
    }

    @Test
    public void elevationSampleIsCorrectInFourCorners() throws Exception {
        Path p = FAKE_HGT_DIR.resolve("N01E001.hgt");
        try (FileChannel c = FileChannel.open(p, CREATE_NEW, READ, WRITE)) {
            ShortBuffer b = c.map(MapMode.READ_WRITE, 0, HGT_FILE_SIZE).asShortBuffer();
            b.put(0, (short)1);
            b.put(3600, (short) 2);
            b.put(3601 * 3600, (short) 3);
            b.put(3601 * 3601 - 1, (short) 4);
        }
        try (HgtDiscreteElevationModel dem = new HgtDiscreteElevationModel(p.toFile())) {
            assertEquals(0, dem.elevationSample(4000, 4000), 1e-10);
            assertEquals(1, dem.elevationSample(3600, 7200), 1e-10);
            assertEquals(2, dem.elevationSample(7200, 7200), 1e-10);
            assertEquals(3, dem.elevationSample(3600, 3600), 1e-10);
            assertEquals(4, dem.elevationSample(7200, 3600), 1e-10);
        }
    }

    private static void createHgtDemWithFileNamed(String hgtFileName) throws Exception {
        Path p = copyEmptyHgtFileAs(hgtFileName);
        try (DiscreteElevationModel d = new HgtDiscreteElevationModel(p.toFile())) {}
    }

    private static Path copyEmptyHgtFileAs(String hgtFileName) throws IOException {
        return Files.copy(FAKE_HGT_FILE, FAKE_HGT_DIR.resolve(hgtFileName), REPLACE_EXISTING);
    }
}
