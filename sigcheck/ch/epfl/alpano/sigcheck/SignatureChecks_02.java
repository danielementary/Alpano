package ch.epfl.alpano.sigcheck;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;

final class SignatureChecks_02 {
    private SignatureChecks_02() {}

    String checkGeoPoint() {
        double lon = 0, lat = 0;
        GeoPoint p = new GeoPoint(lon, lat);
        lon += p.longitude() + p.latitude();
        double d = p.distanceTo(p);
        double a = p.azimuthTo(p);
        return String.valueOf(d) + a;
    }

    String checkInterval1D() {
        int a = 0;
        Interval1D i = new Interval1D(a, a);
        a = i.includedFrom() + i.includedTo() + i.size() + i.sizeOfIntersectionWith(i);
        boolean b = i.contains(a)
                | i.isUnionableWith(i);
        i = i.union(i.boundingUnion(i));
        return i.toString() + b;
    }

    String checkInterval2D() {
        int a = 0;
        Interval1D i1 = null;
        Interval2D i2 = new Interval2D(i1, i1);
        i1 = i2.iX();
        i1 = i2.iY();
        a = i2.size() + i2.sizeOfIntersectionWith(i2);
        boolean b = i2.contains(a, a)
                | i2.isUnionableWith(i2);
        i2 = i2.union(i2.boundingUnion(i2));
        return i2.toString() + b;
    }
}
