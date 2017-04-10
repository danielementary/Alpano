/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.gui;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public final class PanoramaUserParameters {

    private Map<UserParameter, Integer> parameters =
                            new EnumMap<>(UserParameter.class);
    
    public PanoramaUserParameters(Map<UserParameter, Integer> parameters) {
        parameters.replaceAll((k, v) -> k.sanitize(v));
        
        this.parameters = Collections.unmodifiableMap(new EnumMap<>(parameters));
    }
    
    public PanoramaUserParameters(int longitude, int lattitude, int elevation, 
                                  int centerAzimuth, int horizontalFieldOfView,
                                  int maxDist, int width, int height,
                                  int superSampling) {
        
        this(asEnumIntMap(longitude, lattitude, elevation, centerAzimuth,
                          horizontalFieldOfView, maxDist, width, height,
                          superSampling));
        
        
        
    }
    
    private static Map<UserParameter, Integer> asEnumIntMap (int longitude, int lattitude,
                                                 int elevation, int centerAzimuth,
                                                 int horizontalFieldOfView,
                                                 int maxDist, int width, int height,
                                                 int superSampling) {
        
        Map<UserParameter, Integer>  paramMap = new EnumMap<>(UserParameter.class);
        
        paramMap.put(UserParameter.OBSERVER_LONGITUDE, longitude);
        paramMap.put(UserParameter.OBSERVER_LATITUDE, lattitude);
        paramMap.put(UserParameter.OBSERVER_ELEVATION, elevation);
        paramMap.put(UserParameter.CENTER_AZIMUTH, centerAzimuth);
        paramMap.put(UserParameter.HORIZONTAL_FIELD_OF_VIEW, horizontalFieldOfView);
        paramMap.put(UserParameter.MAX_DISTANCE, maxDist);
        paramMap.put(UserParameter.WIDTH, width);
        paramMap.put(UserParameter.HEIGHT, height);
        paramMap.put(UserParameter.SUPER_SAMPLING_EXPONENT, superSampling);
        
        return paramMap;
    }
}
