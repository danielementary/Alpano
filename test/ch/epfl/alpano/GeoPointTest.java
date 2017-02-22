/*
 *	Author:      Samuel Chassot (270955)
 *	Date:        22 févr. 2017
 */


package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;

public class GeoPointTest {

    @Test
    public void distanceToWorks(){
        GeoPoint tokyo = new GeoPoint(2.4379160417585, 0.6227072971728);
        GeoPoint zurich = new GeoPoint(0.12999386801779, 0.8193971772263);
        GeoPoint sydney = new GeoPoint(2.6390617473923, -0.59112381902871);
        
        assertEquals(9666799.915, zurich.distanceTo(tokyo), 1e-3);
        assertEquals(9666799.915, tokyo.distanceTo(zurich), 1e-3);
        assertEquals(16658577.436, zurich.distanceTo(sydney), 1e-3);
        assertEquals(0.0, zurich.distanceTo(zurich), 1e-5);
        
    }
    
    @Test
    public void toStringToWorks(){
        GeoPoint zurich = new GeoPoint(0.12999386801779, 0.8193971772263);
        GeoPoint pole = new GeoPoint(0.0, Math.PI/2);
        GeoPoint tokyo = new GeoPoint(2.4379160417585, 0.6227072971728);
        
        assertEquals("(139.6823,35.6785)", tokyo.toString());
        assertEquals("(7.4481,46.9480)", zurich.toString());
        assertEquals("(0.0000,90.0000)", pole.toString());
    }
    

}
