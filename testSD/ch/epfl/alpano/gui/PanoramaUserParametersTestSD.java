/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */
package ch.epfl.alpano.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.EnumMap;
import java.util.Map;

import org.junit.Test;

public class PanoramaUserParametersTestSD {
    
    //verticalFieldOfView ~= 44.98
    PanoramaUserParameters pan1 = new PanoramaUserParameters(9000, 46500, 5000, 180, 180, 305, 8000, 2000, 0);
    PanoramaUserParameters pan1Copy = new PanoramaUserParameters(9000, 46500, 5000, 180, 180, 305, 8000, 2000, 0);
    //pan1 superSampling 2
    PanoramaUserParameters pan1SuperSamp0 = new PanoramaUserParameters(9000, 46500, 5000, 180, 180, 305, 8000, 2000, 0);
    PanoramaUserParameters pan1SuperSamp1 = new PanoramaUserParameters(9000, 46500, 5000, 180, 180, 305, 8000, 2000, 1);
    PanoramaUserParameters pan1SuperSamp2 = new PanoramaUserParameters(9000, 46500, 5000, 180, 180, 305, 8000, 2000, 2);
    
    //verticalFieldOfView ~= 00.31
    PanoramaUserParameters pan2 = new PanoramaUserParameters(-1, -1, -1, -1, -1, -1, -1, -1, -1);
    
    //verticlaFielOfView tricky
    PanoramaUserParameters pan3 = new PanoramaUserParameters(9000, 46500, 5000, 180, 180, 305, 30, 4000, 0);
    
    @Test
    public void worksOnTrivialPan1() {
        assertEquals(9000, pan1.getOberserverLong(), 0);
        assertEquals(46500, pan1.getOberserverLati(), 0);
        assertEquals(5000, pan1.getObserverElev(), 0);
        assertEquals(180, pan1.getCenterAzim(), 0);
        assertEquals(180, pan1.getHoriFieldOfView(), 0);
        assertEquals(305, pan1.getMaxDist(), 0);
        assertEquals(8000, pan1.getWidth(), 0);
        assertEquals(2000, pan1.getHeight(), 0);
        assertEquals(0, pan1.getSuperSamp(), 0);
        
        assertEquals(9000, pan1.get(UserParameter.OBSERVER_LONGITUDE), 0);
    }
    
    @Test
    public void worksOnMinValuesPan2() {
        assertEquals(6000, pan2.getOberserverLong(), 0);
        assertEquals(45000, pan2.getOberserverLati(), 0);
        assertEquals(300, pan2.getObserverElev(), 0);
        assertEquals(0, pan2.getCenterAzim(), 0);
        assertEquals(1, pan2.getHoriFieldOfView(), 0);
        assertEquals(10, pan2.getMaxDist(), 0);
        assertEquals(30, pan2.getWidth(), 0);
        assertEquals(10, pan2.getHeight(), 0);
        assertEquals(0, pan2.getSuperSamp(), 0);
    }
    
    @Test
    public void worksPanoDispParamAndSecConst() { 
        Map<UserParameter, Integer> paramMap = new EnumMap<>(UserParameter.class);
        
        paramMap.put(UserParameter.OBSERVER_LONGITUDE, 9000);
        paramMap.put(UserParameter.OBSERVER_LATITUDE, 46500);
        paramMap.put(UserParameter.OBSERVER_ELEVATION, 5000);
        paramMap.put(UserParameter.CENTER_AZIMUTH, 180);
        paramMap.put(UserParameter.HORIZONTAL_FIELD_OF_VIEW, 180);
        paramMap.put(UserParameter.MAX_DISTANCE, 305);
        paramMap.put(UserParameter.WIDTH, 8000);
        paramMap.put(UserParameter.HEIGHT, 2000);
        paramMap.put(UserParameter.SUPER_SAMPLING_EXPONENT, 0);
        
        assertTrue(paramMap.equals(pan1.panoramaDisplayParameters()));
        assertTrue(new PanoramaUserParameters(paramMap).equals(pan1));
    }
    
    @Test
    public void worksOnSuperSamp() {
        Map<UserParameter, Integer> paramMap = new EnumMap<>(UserParameter.class);
        
        paramMap.put(UserParameter.OBSERVER_LONGITUDE, 9000);
        paramMap.put(UserParameter.OBSERVER_LATITUDE, 46500);
        paramMap.put(UserParameter.OBSERVER_ELEVATION, 5000);
        paramMap.put(UserParameter.CENTER_AZIMUTH, 180);
        paramMap.put(UserParameter.HORIZONTAL_FIELD_OF_VIEW, 180);
        paramMap.put(UserParameter.MAX_DISTANCE, 305);
        paramMap.put(UserParameter.WIDTH, 8000);
        paramMap.put(UserParameter.HEIGHT, 2000);
        paramMap.put(UserParameter.SUPER_SAMPLING_EXPONENT, 0);
        
        assertTrue(paramMap.equals(pan1SuperSamp0.panoramaParameters()));
        
        paramMap.replace(UserParameter.SUPER_SAMPLING_EXPONENT, 1);
        paramMap.replace(UserParameter.WIDTH, 16000);
        paramMap.replace(UserParameter.HEIGHT, 4000);
        assertTrue(paramMap.equals(pan1SuperSamp1.panoramaParameters()));
        
        paramMap.replace(UserParameter.SUPER_SAMPLING_EXPONENT, 2);
        paramMap.replace(UserParameter.WIDTH, 32000);
        paramMap.replace(UserParameter.HEIGHT, 8000);
        assertTrue(paramMap.equals(pan1SuperSamp2.panoramaParameters()));
    }
    
    @Test
    public void worksOnTrickyHeight() {
        assertEquals(9000, pan3.getOberserverLong(), 0);
        assertEquals(46500, pan3.getOberserverLati(), 0);
        assertEquals(5000, pan3.getObserverElev(), 0);
        assertEquals(180, pan3.getCenterAzim(), 0);
        assertEquals(180, pan3.getHoriFieldOfView(), 0);
        assertEquals(305, pan3.getMaxDist(), 0);
        assertEquals(30, pan3.getWidth(), 0);
        //assertEquals(4000, pan3.getHeight(), 0);
        assertEquals(0, pan3.getSuperSamp(), 0);
    }
    
    @Test
    public void worksEqualsOnEqualPan() {
        assertTrue(pan1.equals(pan1Copy));
        assertFalse(pan1.equals(pan2));
    }
    
    @Test
    public void worksHashCodeOnEqualPan() {
        assertTrue(pan1.hashCode() == (pan1Copy.hashCode()));
        assertFalse(pan1.hashCode() == (pan2.hashCode()));
    }  
}
