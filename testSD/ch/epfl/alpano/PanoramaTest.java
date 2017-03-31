package ch.epfl.alpano;

/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

import org.junit.Test;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaParameters;

public class PanoramaTest {

    @Test(expected = IndexOutOfBoundsException.class)
    public void testExceptionSetDist() {
        PanoramaParameters para = new PanoramaParameters(new GeoPoint(0,0), 0, Math.PI, 1.0, 100, 20, 20 );
        
        Panorama.Builder build = new Panorama.Builder(para);
        
        build.setDistanceAt(21, 1, 18f);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testExceptionSetLogitude() {
        PanoramaParameters para = new PanoramaParameters(new GeoPoint(0,0), 0, Math.PI, 1.0, 100, 20, 20 );
        
        Panorama.Builder build = new Panorama.Builder(para);
        
        build.setLongitudeAt(21, 1, 18f);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testExceptionSetLatitude() {
        PanoramaParameters para = new PanoramaParameters(new GeoPoint(0,0), 0, Math.PI, 1.0, 100, 20, 20 );
        
        Panorama.Builder build = new Panorama.Builder(para);
        
        build.setLatitudeAt(21, 1, 18f);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testExceptionSetElev() {
        PanoramaParameters para = new PanoramaParameters(new GeoPoint(0,0), 0, Math.PI, 1.0, 100, 20, 20 );
        
        Panorama.Builder build = new Panorama.Builder(para);
        
        build.setElevationAt(21, 1, 18f);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testExceptionSetSlope() {
        PanoramaParameters para = new PanoramaParameters(new GeoPoint(0,0), 0, Math.PI, 1.0, 100, 20, 20 );
        
        Panorama.Builder build = new Panorama.Builder(para);
        
        build.setSlopeAt(21, 1, 18f);
    }

}
