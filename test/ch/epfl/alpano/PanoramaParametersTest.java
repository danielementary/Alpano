package ch.epfl.alpano;

/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

import static org.junit.Assert.*;

import org.junit.Test;

import static java.lang.Math.toRadians;

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
        PanoramaParameters pano2 = new PanoramaParameters(new GeoPoint(0,0),0, Math.PI/2, Math.PI, 100, 181, 360);
        PanoramaParameters pano3 = new PanoramaParameters(new GeoPoint(0,0),0, Math.PI/4, Math.PI, 100, 181, 360);
        
        assertEquals(90, pano.xForAzimuth(Math.PI/2), 0.0);
        assertEquals(45, pano2.xForAzimuth(Math.PI/4), 0.0);
        assertEquals(46, pano3.xForAzimuth(Math2.PI2/360), 0.0);
    }

    @Test
    public void testAltitudeForY() {
        PanoramaParameters pano = new PanoramaParameters(new GeoPoint(0,0),50, Math.PI, Math2.PI2, 100, 361, 101);  

        assertEquals(Math.toRadians(50), pano.altitudeForY(0), 0.0);
        
    }

    @Test 
    public void trivialAltitudeForYTest(){
        PanoramaParameters pano = new PanoramaParameters(new GeoPoint(0,0),0, Math.PI,Math2.PI2,100,361,101);
        for(int i = 0; i < 101;++i){
            
            assertEquals(-i + 50,Math.toDegrees(pano.altitudeForY(i)),1e-10);
        }       
    }   
    
    
    @Test
    public void testYForAltitude() {
        PanoramaParameters pano = new PanoramaParameters(new GeoPoint(0,0),50, Math.PI, Math2.PI2, 100, 361, 101);
        
        for (int i = 0 ; i < 101 ; ++i){
            assertEquals(i,pano.yForAltitude(Math.toRadians(50-i)),10e-10);
        }
        assertEquals(0, pano.yForAltitude(Math.toRadians(50)), 0.0);
    }

    
    
    
    
    //Julie's tests
    
    @Test
    public void constructor() {
        new PanoramaParameters(new GeoPoint(toRadians(6.8087),toRadians(47.0085)),1380, toRadians(162),toRadians(27),300, 2500,800);
    }
    
    @Test(expected = NullPointerException.class)
    public void constructorThrowsNullPointerException() {
        new PanoramaParameters(null,1380, toRadians(162),toRadians(27),300, 2500,800);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsIllegalArgumentException1() {
        new PanoramaParameters(new GeoPoint(toRadians(6.8087),toRadians(47.0085)),1380, 8,toRadians(27),300, 2500,800);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsIllegalArgumentException2() {
        new PanoramaParameters(new GeoPoint(toRadians(6.8087),toRadians(47.0085)),1380, toRadians(162),0,300, 2500,800);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsIllegalArgumentException3() {
        new PanoramaParameters(new GeoPoint(toRadians(6.8087),toRadians(47.0085)),1380, toRadians(162),7,300, 2500,800);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsIllegalArgumentException4() {
        new PanoramaParameters(new GeoPoint(toRadians(6.8087),toRadians(47.0085)),1380, toRadians(162),toRadians(27),0, 2500,800);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsIllegalArgumentException5() {
        new PanoramaParameters(new GeoPoint(toRadians(6.8087),toRadians(47.0085)),1380, toRadians(162),toRadians(27),300, 0, 800);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsIllegalArgumentException6() {
        new PanoramaParameters(new GeoPoint(toRadians(6.8087),toRadians(47.0085)),1380, toRadians(162),toRadians(27),300, 2500, 0);
    }

    @Test
    public void testVerticalFieldOfViewJulie() {
        PanoramaParameters panorama1 = new PanoramaParameters(new GeoPoint(toRadians(6.8087),toRadians(47.0085)),1380, toRadians(162),Math.PI,300, 2, 2);
        
        assertEquals(Math.PI, panorama1.verticalFieldOfView(), 0);
    }

    @Test
    public void testAzimuthForXJulie() {
        PanoramaParameters panorama1 = new PanoramaParameters(new GeoPoint(0,0),0, Math.PI,Math2.PI2,300, 361, 101);
    
        assertEquals(Math.PI,panorama1.azimuthForX(180), 10e-8);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAzimuthForXThrowsIllegalArgumentException() {
        PanoramaParameters panorama1 = new PanoramaParameters(new GeoPoint(0,0),0, Math.PI,Math2.PI2,300, 361, 101);
        
        panorama1.azimuthForX(-1);
        panorama1.azimuthForX(362);
    }

    @Test
    public void testXForAzimuthJulie() {
        PanoramaParameters panorama1 = new PanoramaParameters(new GeoPoint(0,0),0, Math.PI,Math2.PI2,300, 361, 101);
        
        assertEquals(Math.PI /2,panorama1.azimuthForX(90), 10e-8);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testXForAzimuthThrowsIllegalArgumentExcpetion() {
        PanoramaParameters panorama1 = new PanoramaParameters(new GeoPoint(0,0),0, Math.PI/2,Math.PI,300, 361, 101);
        
       panorama1.xForAzimuth(3*Math.PI/2);
    }
    
    @Test
    public void trivialAltitudeForYTestJulie() {
        PanoramaParameters panorama = new PanoramaParameters(new GeoPoint(0, 0), 0,
                Math.PI, Math2.PI2, 100, 361, 101);
        for (int i = 0; i < 101; ++i) {

            assertEquals(-i + 50, Math.toDegrees(panorama.altitudeForY(i)), 1e-10);
        }
    }
    
    @Test
    public void testAltitudeForYJulie() {
        PanoramaParameters panorama = new PanoramaParameters(new GeoPoint(0, 0), 50,
                Math.PI, Math2.PI2, 100, 361, 101);

        assertEquals(Math.toRadians(50), panorama.altitudeForY(0), 0.0);

    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltitudeForYThrowsIllegalArgumentException() {
        PanoramaParameters panorama = new PanoramaParameters(new GeoPoint(0, 0), 50,
                Math.PI, Math2.PI2, 100, 361, 101);

        panorama.altitudeForY(-1);
        panorama.altitudeForY(200);
    }

    @Test
    public void testYForAltitudeJulie() {
            PanoramaParameters panorama = new PanoramaParameters(new GeoPoint(0, 0), 50,
                    Math.PI, Math2.PI2, 100, 361, 101);

            assertEquals(0, panorama.yForAltitude(toRadians(50)), 0.0);
            assertEquals(10, panorama.yForAltitude(toRadians(40)), 0.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testYForAltitudeThrowsIllegalArgumentException() {
        PanoramaParameters panorama = new PanoramaParameters(new GeoPoint(0, 0), 50,
                Math.PI, Math2.PI2, 100, 361, 101);

        panorama.yForAltitude(2);
        panorama.yForAltitude(-2);
    }

    @Test
    public void testIsValidSampleIndex() {
        PanoramaParameters panorama = new PanoramaParameters(new GeoPoint(0, 0), 50,
                Math.PI, Math2.PI2, 100, 361, 101);

        assertTrue(panorama.isValidSampleIndex(0,0));
        assertFalse(panorama.isValidSampleIndex(-1,0));
        assertFalse(panorama.isValidSampleIndex(0,-1));
        assertFalse(panorama.isValidSampleIndex(361,0));
        assertFalse(panorama.isValidSampleIndex(0,101));

    }

    @Test
    public void testLinearSampleIndex() {
        PanoramaParameters panorama = new PanoramaParameters(new GeoPoint(0, 0), 50,
                Math.PI, Math2.PI2, 100, 361, 101);

        assertEquals(0, panorama.linearSampleIndex(0,0), 0);
        assertEquals(1, panorama.linearSampleIndex(1,0), 0);
        assertEquals(361*101 -1, panorama.linearSampleIndex(360,100), 0);
    }

}
