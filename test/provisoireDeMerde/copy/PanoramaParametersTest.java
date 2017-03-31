package provisoireDeMerde.copy;

import static java.lang.Math.PI;
import static java.lang.Math.floorMod;
import static java.lang.Math.nextUp;
import static java.lang.Math.toRadians;
import static org.junit.Assert.assertEquals;
import static provisoireDeMerde.copy.TestRandomizer.RANDOM_ITERATIONS;
import static provisoireDeMerde.copy.TestRandomizer.newRandom;

import java.util.Random;

import org.junit.Test;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;
import ch.epfl.alpano.PanoramaParameters;

public class PanoramaParametersTest {
    // Default (and valid) arguments for constructor
    private static GeoPoint O_POS() { return new GeoPoint(toRadians(4), toRadians(4)); }
    private static int O_EL = 1000;
    private static double C_AZ = toRadians(180);
    private static double H_FOV = toRadians(60);
    private static int MAX_D = 1000;
    private static int W = 100, H = 100;

    @Test(expected = NullPointerException.class)
    public void constructorFailsWithNullObserverPosition() {
        new PanoramaParameters(null, O_EL, C_AZ, H_FOV, MAX_D, W, H);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorFailsWithNonCanonicalAzimuth() {
        new PanoramaParameters(O_POS(), O_EL, 42d, H_FOV, MAX_D, W, H);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorFailsWithZeroFieldOfView() {
        new PanoramaParameters(O_POS(), O_EL, C_AZ, 0, MAX_D, W, H);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorFailsWithTooLargeOfView() {
        new PanoramaParameters(O_POS(), O_EL, C_AZ, nextUp(2d * PI), MAX_D, W, H);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorFailsWithZeroWidth() {
        new PanoramaParameters(O_POS(), O_EL, C_AZ, H_FOV, MAX_D, 0, H);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorFailsWithZeroHeight() {
        new PanoramaParameters(O_POS(), O_EL, C_AZ, H_FOV, MAX_D, W, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorFailsWithZeroMaxDistance() {
        new PanoramaParameters(O_POS(), O_EL, C_AZ, H_FOV, 0, W, H);
    }

    @Test
    public void verticalFieldOfViewIsCorrect() {
        PanoramaParameters p =
                new PanoramaParameters(O_POS(), O_EL, C_AZ, toRadians(60), MAX_D, 601, 201);
        assertEquals(p.verticalFieldOfView(), toRadians(20), 1e-10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void azimuthForXFailsForNegativeX() {
        PanoramaParameters p =
                new PanoramaParameters(O_POS(), O_EL, C_AZ, H_FOV, MAX_D, W, H);
        p.azimuthForX(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void azimuthForXFailsForTooBigX() {
        PanoramaParameters p =
                new PanoramaParameters(O_POS(), O_EL, C_AZ, H_FOV, MAX_D, W, H);
        p.azimuthForX(W + 1);
    }

    @Test
    public void azimuthForXWorksForFullCircle() {
        int centralAzDeg = 90;
        PanoramaParameters p =
                new PanoramaParameters(O_POS(), O_EL, toRadians(centralAzDeg), Math2.PI2, MAX_D, 361, 201);
        for (int azDeg = 0; azDeg < 360; ++azDeg) {
            double expectedAz = toRadians(floorMod(azDeg - centralAzDeg, 360));
            double actualAz = p.azimuthForX(azDeg);
            assertEquals(expectedAz, actualAz, 1e-10);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void xForAzimuthFailsForTooSmallAzimuth() {
        PanoramaParameters p =
                new PanoramaParameters(O_POS(), O_EL, toRadians(10), toRadians(40), MAX_D, W, H);
        p.xForAzimuth(toRadians(349.99));
    }

    @Test(expected = IllegalArgumentException.class)
    public void xForAzimuthFailsForTooBigAzimuth() {
        PanoramaParameters p =
                new PanoramaParameters(O_POS(), O_EL, toRadians(10), toRadians(40), MAX_D, W, H);
        p.xForAzimuth(toRadians(50.01));
    }

    @Test(expected = IllegalArgumentException.class)
    public void altitudeForYFailsForNegativeY() {
        PanoramaParameters p =
                new PanoramaParameters(O_POS(), O_EL, C_AZ, H_FOV, MAX_D, W, H);
        p.altitudeForY(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void altitueForYFailsForTooBigY() {
        PanoramaParameters p =
                new PanoramaParameters(O_POS(), O_EL, C_AZ, H_FOV, MAX_D, W, H);
        p.altitudeForY(H + 1);
    }

    @Test
    public void altitudeForYWorks() {
        int height = 201;
        PanoramaParameters p =
                new PanoramaParameters(O_POS(), O_EL, C_AZ, toRadians(60), MAX_D, 601, height);
        double halfVerticalFOV = toRadians(20) / 2d;
        double delta = toRadians(0.1);
        for (int y = 0; y < height; ++y) {
            assertEquals(halfVerticalFOV - y * delta, p.altitudeForY(y), 1e-9);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void yForAltitudeFailsForTooSmallAltitude() {
        PanoramaParameters p =
                new PanoramaParameters(O_POS(), O_EL, C_AZ, toRadians(60), MAX_D, 601, 201);
        p.yForAltitude(toRadians(-10.01));
    }

    @Test(expected = IllegalArgumentException.class)
    public void yForAltitudeFailsForTooBigAltitude() {
        PanoramaParameters p =
                new PanoramaParameters(O_POS(), O_EL, C_AZ, toRadians(60), MAX_D, 601, 201);
        p.yForAltitude(toRadians(10.01));
    }

    @Test
    public void azimuthForXAndXForAzimuthAreInverse() {
        PanoramaParameters p =
                new PanoramaParameters(O_POS(), O_EL, C_AZ, toRadians(60), MAX_D, 601, 201);
        Random rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            int x = 1 + rng.nextInt(600);
            assertEquals(x, p.xForAzimuth(p.azimuthForX(x)), 1e-10);
        }
    }

    @Test
    public void altitudeForYAndYForAltitudeAreInverse() {
        PanoramaParameters p =
                new PanoramaParameters(O_POS(), O_EL, C_AZ, toRadians(60), MAX_D, 601, 201);
        Random rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            int x = 1 + rng.nextInt(200);
            assertEquals(x, p.yForAltitude(p.altitudeForY(x)), 1e-10);
        }
    }
}
