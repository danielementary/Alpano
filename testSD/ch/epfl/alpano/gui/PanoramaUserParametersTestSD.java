/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */
package ch.epfl.alpano.gui;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PanoramaUserParametersTestSD {
    //let's do it later
    
    PanoramaUserParameters pan1 = new PanoramaUserParameters(9000, 46500, 5000, 180, 180, 305, 8000, 2000, 0);
    

    @Test
    public void test() {
        assertEquals(9000, pan1.getOberserverLong(), 0);
        assertEquals(46500, pan1.getOberserverLati(), 0);
        assertEquals(5000, pan1.getObserverElev(), 0);
        assertEquals(180, pan1.getCenterAzim(), 0);
        assertEquals(180, pan1.getHoriFieldOfView(), 0);
        assertEquals(305, pan1.getMaxDist(), 0);
        assertEquals(8000, pan1.getWidth(), 0);
        assertEquals(2000, pan1.getHeight(), 0);
        assertEquals(0, pan1.getSuperSamp(), 0);
    }
}
