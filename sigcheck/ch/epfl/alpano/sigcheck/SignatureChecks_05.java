package ch.epfl.alpano.sigcheck;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;

final class SignatureChecks_05 {
    private SignatureChecks_05() {}

    void checkSummit() {
        GeoPoint p = null;
        int e = 0;
        Summit s = new Summit("", p, e);
        p = s.position();
        e = s.elevation();
        System.out.println(s.name());
    }

    void checkGazetteerParser() throws IOException {
        File f = null;
        List<Summit> s = GazetteerParser.readSummitsFrom(f);
        System.out.println(s);
    }

    void checkPanoramaParameters() {
        GeoPoint p = null;
        int i = 0;
        double d = 0d;
        PanoramaParameters pp = new PanoramaParameters(p, i, d, d, i, i, i);
        p = pp.observerPosition();
        i = pp.observerElevation();
        d = pp.centerAzimuth();
        d = pp.horizontalFieldOfView();
        d = pp.verticalFieldOfView();
        i = pp.width();
        i = pp.height();
        i = pp.maxDistance();
        d = pp.xForAzimuth(pp.azimuthForX(d));
        d = pp.yForAltitude(pp.altitudeForY(d));
    }
}
