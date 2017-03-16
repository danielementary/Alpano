/*
 *	Author:      Samuel Chassot (270955)
 *	Date:        15 mars 2017
 */


package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;

public class PanoramaParametersTest {

    @Test
    public void testAzimuthForX() {
        PanoramaParameters pano = new PanoramaParameters(new GeoPoint(0,0),0, Math.PI, Math2.PI2, 100, 361, 360);
        assertEquals(0.0 ,pano.azimuthForX(0), 10e-15);
        assertEquals(1.396263401595464 , pano.azimuthForX(80), 10e-10);
        PanoramaParameters pano2 = new PanoramaParameters(new GeoPoint(0,0),0, Math.PI/2, Math.PI, 100, 361, 360);
        assertEquals(0.0 ,pano2.azimuthForX(0), 10e-15);
        PanoramaParameters pano3 = new PanoramaParameters(new GeoPoint(0,0),0, Math.PI/4, Math.PI, 100, 361, 360);
        assertEquals(Azimuth.canonicalize(-Math.PI/4) ,pano3.azimuthForX(0), 10e-15);

    }

    @Test
    public void testXForAzimuth() {
       PanoramaParameters pano = new PanoramaParameters(new GeoPoint(0,0),0, Math.PI, Math2.PI2, 100, 361, 360);
    }

    @Test
    public void testAltitudeForY() {
        fail("Not yet implemented");
    }

    @Test
    public void testYForAltitude() {
        fail("Not yet implemented");
    }

}
