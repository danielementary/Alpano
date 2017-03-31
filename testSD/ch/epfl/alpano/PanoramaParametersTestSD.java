package ch.epfl.alpano;

/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

import static org.junit.Assert.*;

import org.junit.Test;


public class PanoramaParametersTestSD {

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
        
        assertEquals(90, pano.xForAzimuth(Math.PI/2), 0.0);
        assertEquals(45, pano2.xForAzimuth(Math.PI/4), 0.0);
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
}