package ch.epfl.alpano;

/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

import static java.lang.Math.PI;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GeoPointTest {

    @Test
    public void distanceToWorks(){
        GeoPoint tokyo = new GeoPoint(2.4379160417585, 0.6227072971728);
        GeoPoint zurich = new GeoPoint(0.12999386801779, 0.8193971772263);
        GeoPoint sydney = new GeoPoint(2.6390617473923, -0.59112381902871);
        
        //on trivial values
        assertEquals(9666799.915, zurich.distanceTo(tokyo), 1e-3);
        assertEquals(9666799.915, tokyo.distanceTo(zurich), 1e-3);
        assertEquals(16658577.436, zurich.distanceTo(sydney), 1e-3);
        //on simple values
        assertEquals(0.0, zurich.distanceTo(zurich), 1e-5);
    }
    
    @Test
    public void toStringToWorks(){
        GeoPoint zurich = new GeoPoint(0.12999386801779, 0.8193971772263);
        GeoPoint pole = new GeoPoint(0.0, Math.PI/2);
        GeoPoint tokyo = new GeoPoint(2.4379160417585, 0.6227072971728);
        
        //on trivial values
        assertEquals("(139.6823,35.6785)", tokyo.toString());
        assertEquals("(7.4481,46.9480)", zurich.toString());
        //on simple values
        assertEquals("(0.0000,90.0000)", pole.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void constructorFailsOnBiggerOutOfBoundsValues() {
        GeoPoint fake1 = new GeoPoint(PI+1, PI/2+1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void constructorFailsOnLowerOutOfBoundsValues() {
        GeoPoint fake2 = new GeoPoint(-PI-1, -PI/2-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void constructorFailsOnLongitudeLowerOutOfBoundsValues() {
        GeoPoint fake3 = new GeoPoint(PI+1, PI/2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void constructorFailsOnLatitudeLowerOutOfBoundsValues() {
        GeoPoint fake4 = new GeoPoint(PI, PI/2+1);
    }
    
    @Test
    public void azimuthToWorks() {
        GeoPoint tokyo = new GeoPoint(2.4379160417585, 0.6227072971728);
        GeoPoint zurich = new GeoPoint(0.12999386801779, 0.8193971772263);
        GeoPoint sydney = new GeoPoint(2.6390617473923, -0.59112381902871);

        
        //on trivial values
        assertEquals(5.747268040636002, tokyo.azimuthTo(zurich), 1e-1);
        assertEquals(2.9496064358631000069, tokyo.azimuthTo(sydney), 1e-1);
        assertEquals(0.64577182323630000216, zurich.azimuthTo(tokyo), 1e-1);
        assertEquals(1.3439035240323000675, zurich.azimuthTo(sydney), 1e-1);
        //on simple values
        assertEquals(0.0, tokyo.azimuthTo(tokyo), 0);
        assertEquals(0.0, zurich.azimuthTo(zurich), 0);
        assertEquals(0.0, sydney.azimuthTo(sydney), 0);
    }
}
