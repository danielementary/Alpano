package ch.epfl.alpano.summit;

/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ch.epfl.alpano.GeoPoint;

public class SummitTest {
    
    @Test
    public void toStringWorks() {
        GeoPoint point = new GeoPoint(0.139718, 0.812931);
        Summit summit = new Summit("EIGER", point, 3970);
        
        assertEquals("EIGER (8.0053,46.5775) 3970", summit.toString());
        
    }

}
