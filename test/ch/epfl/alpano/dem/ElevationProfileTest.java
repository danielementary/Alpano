package ch.epfl.alpano.dem;

import static java.lang.Math.PI;
import static java.lang.Math.toRadians;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;

public class ElevationProfileTest {
    @Test(expected = NullPointerException.class)
    public void constructorFailsWhenElevationModelIsNull() {
        new ElevationProfile(null, new GeoPoint(0,0), 0, 100);
    }

    @Test(expected = NullPointerException.class)
    public void constructorFailsWhenOriginIsNull() {
        new ElevationProfile(newConstantSlopeDEM(), null, 0, 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorFailsWhenAzimuthIsNotCanonical() {
        new ElevationProfile(newConstantSlopeDEM(), new GeoPoint(0,0), 6.3, 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorFailsWhenLengthIsZero() {
        new ElevationProfile(newConstantSlopeDEM(), new GeoPoint(0,0), 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void elevationAtFailsWhenXIsTooBig() {
        ElevationProfile p = new ElevationProfile(newConstantSlopeDEM(), new GeoPoint(0,0), 0, 100);
        p.elevationAt(101);
    }

    @Test
    public void elevationAtWorksOnConstantSlopeDEMGoingNorth() {
        ElevationProfile p = new ElevationProfile(newConstantSlopeDEM(), new GeoPoint(0,0), 0, 100_000);
        for (int i = 0; i < 100; ++i) {
            double x = 100d * i;
            assertEquals(x, p.elevationAt(x), 1e-5);
        }
    }

    @Test
    public void elevationAtWorksOnConstantSlopeDEMGoingSouth() {
        ElevationProfile p = new ElevationProfile(newConstantSlopeDEM(), new GeoPoint(0,0), PI, 100_000);
        for (int i = 0; i < 100; ++i) {
            double x = 100d * i;
            assertEquals(-x, p.elevationAt(x), 1e-5);
        }
    }

    @Test
    public void elevationAtWorksOnConstantSlopeDEMGoingEast() {
        ElevationProfile p = new ElevationProfile(newConstantSlopeDEM(), new GeoPoint(0,0), PI/2d, 100_000);
        for (int i = 0; i < 100; ++i) {
            double x = 100d * i;
            assertEquals(x, p.elevationAt(x), 1e-5);
        }
    }

    @Test
    public void elevationAtWorksOnConstantSlopeDEMGoingWest() {
        ElevationProfile p = new ElevationProfile(newConstantSlopeDEM(), new GeoPoint(0,0), 3d*PI/2d, 100_000);
        for (int i = 0; i < 100; ++i) {
            double x = 100d * i;
            assertEquals(-x, p.elevationAt(x), 1e-5);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void positionAtFailsWhenXIsTooBig() {
        ElevationProfile p = new ElevationProfile(newConstantSlopeDEM(), new GeoPoint(0,0), 0, 100);
        p.positionAt(101);
    }

    @Test
    public void positionAtProducesConstantLongitudeWhenGoingNorth() {
        double lon = toRadians(3);
        ElevationProfile p = new ElevationProfile(newConstantSlopeDEM(), new GeoPoint(lon,toRadians(40)), 0, 100_000);
        for (int i = 0; i < 100; ++i) {
            double x = 500d * i;
            assertEquals(lon, p.positionAt(x).longitude(), 1e-5);
        }
    }

    @Test
    public void positionAtProducesConstantLongitudeWhenGoingSouth() {
        double lon = toRadians(3);
        ElevationProfile p = new ElevationProfile(newConstantSlopeDEM(), new GeoPoint(lon,toRadians(40)), PI, 100_000);
        for (int i = 0; i < 100; ++i) {
            double x = 500d * i;
            assertEquals(lon, p.positionAt(x).longitude(), 1e-5);
        }
    }

    @Test
    public void positionAtProducesConstantLatitudeWhenGoingEast() {
        double lat = toRadians(40);
        ElevationProfile p = new ElevationProfile(newConstantSlopeDEM(), new GeoPoint(toRadians(3),lat), PI/2d, 100_000);
        for (int i = 0; i < 100; ++i) {
            double x = 500d * i;
            assertEquals(lat, p.positionAt(x).latitude(), 1e-4);
        }
    }

    @Test
    public void positionAtProducesConstantLatitudeWhenGoingWest() {
        double lat = toRadians(40);
        ElevationProfile p = new ElevationProfile(newConstantSlopeDEM(), new GeoPoint(toRadians(3),lat), 3d*PI/2d, 100_000);
        for (int i = 0; i < 100; ++i) {
            double x = 500d * i;
            assertEquals(lat, p.positionAt(x).latitude(), 1e-4);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void slopeAtFailsWhenXIsNegative() {
        ElevationProfile p = new ElevationProfile(newConstantSlopeDEM(), new GeoPoint(0,0), 0, 100);
        p.positionAt(-1);
    }

    private static ContinuousElevationModel newConstantSlopeDEM() {
        Interval2D extent = new Interval2D(
                new Interval1D(-10_000, 10_000),
                new Interval1D(-10_000, 10_000));
        return new ContinuousElevationModel(new ConstantSlopeDEM(extent));
    }
}
