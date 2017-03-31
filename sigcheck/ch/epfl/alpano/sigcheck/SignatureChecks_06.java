package ch.epfl.alpano.sigcheck;

import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;

final class SignatureChecks_06 {
    private SignatureChecks_06() {}

    void checkPanorama(Panorama p) {
        PanoramaParameters pp = p.parameters();
        int x = 0;
        float d = p.distanceAt(x, x);
        d = p.distanceAt(x, x, d);
        d = p.longitudeAt(x, x);
        d = p.latitudeAt(x, x);
        d = p.elevationAt(x, x);
        d = p.slopeAt(x, x);
        checkPanoramaBuilder(pp);
    }

    void checkPanoramaBuilder(PanoramaParameters pp) {
        Panorama.Builder b = new Panorama.Builder(pp);
        int x = 0;
        float d = 0;
        b.setDistanceAt(x, x, d);
        b.setLongitudeAt(x, x, d);
        b.setLatitudeAt(x, x, d);
        b.setElevationAt(x, x, d);
        b.setSlopeAt(x, x, d);
        Panorama p = b.build();
        checkPanorama(p);
    }

    void checkPanoramaComputer(PanoramaParameters pp) {
        ContinuousElevationModel d = null;
        PanoramaComputer pc = new PanoramaComputer(d);
        Panorama p = pc.computePanorama(pp);
        checkPanorama(p);
        double y = 0;
        ElevationProfile pr = null;
        DoubleUnaryOperator o = PanoramaComputer.rayToGroundDistance(pr, y, y);
        System.out.println(o);
    }
}
