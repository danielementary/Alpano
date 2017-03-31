package ch.epfl.alpano.sigcheck;

import java.io.File;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;

final class SignatureChecks_04 {
    private SignatureChecks_04() {}

    void checkHgtDiscreteElevationModel() {
        File f = null;
        DiscreteElevationModel m = new HgtDiscreteElevationModel(f);
        System.out.println(m);
    }

    void checkElevationProfile() {
        ContinuousElevationModel dem = null;
        GeoPoint o = null;
        double d = 0;

        ElevationProfile p = new ElevationProfile(dem, o, d, d);
        d = p.elevationAt(d);
        o = p.positionAt(d);
        d = p.slopeAt(d);
    }
}
