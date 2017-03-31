package ch.epfl.alpano.sigcheck;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;

final class SignatureChecks_03 {
    private SignatureChecks_03() {}

    String checkDiscElevationModel(DiscreteElevationModel d) throws Exception {
        double a = DiscreteElevationModel.SAMPLES_PER_DEGREE * DiscreteElevationModel.SAMPLES_PER_RADIAN;
        a = DiscreteElevationModel.sampleIndex(a);
        Interval2D e = d.extent();
        int v = 0;
        a = d.elevationSample(v, v);
        d = d.union(d);
        d.close();
        return d.toString() + e;
    }

    String checkContElevationModel() {
        DiscreteElevationModel md = null;
        ContinuousElevationModel m = new ContinuousElevationModel(md);
        GeoPoint p = null;
        double e = m.elevationAt(p) + m.slopeAt(p);
        return String.valueOf(e);
    }
}
