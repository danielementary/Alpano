/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.gui;

import static java.lang.Math.scalb;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.PanoramaParameters;

public final class PanoramaUserParameters {

    private Map<UserParameter, Integer> parameters =
                            new EnumMap<>(UserParameter.class);
    
    private final GeoPoint point;
    private final int distMeter;
    private final double centAzim, hFOV;
    
    /**
     * construct an instance of PanoramaUserParameters with the given map
     * after having corrected all values
     * @param parameters
     */
    public PanoramaUserParameters(Map<UserParameter, Integer> parameters) {
        
        parameters.replaceAll((k, v) -> k.sanitize(v));
        
        //height correction
        int maxHeight = ((170*(parameters.get(UserParameter.WIDTH)-1))
                         /parameters.get(UserParameter.HORIZONTAL_FIELD_OF_VIEW))+1;
        
        if (parameters.get(UserParameter.HEIGHT) > maxHeight) {
            parameters.replace(UserParameter.HEIGHT, UserParameter.HEIGHT.sanitize(maxHeight)); 
        }
        
        this.parameters = Collections.unmodifiableMap(new EnumMap<>(parameters));
        
        this.point = new GeoPoint(Math.toRadians(getOberserverLong()*1e-4),
                                  Math.toRadians(getOberserverLati()*1e-4));
        
        this.distMeter = getMaxDist()*1000;
        this.centAzim = Math.toRadians(getCenterAzim());
        this.hFOV = Math.toRadians(getHoriFieldOfView());
    }

    /**
     * construct an instance of PanoramaUserParameters with the given elements
     * after having collected them in a map
     * @param longitude
     * @param lattitude
     * @param elevation
     * @param centerAzimuth
     * @param horizontalFieldOfView
     * @param maxDist
     * @param width
     * @param height
     * @param superSampling
     */
    public PanoramaUserParameters(int longitude, int lattitude, int elevation, 
                                  int centerAzimuth, int horizontalFieldOfView,
                                  int maxDist, int width, int height,
                                  int superSampling) {
        
        this(asEnumIntMap(longitude, lattitude, elevation, centerAzimuth,
                          horizontalFieldOfView, maxDist, width, height,
                          superSampling));
        
    }
    
    /**
     * collects all parameters in a Map
     * @param longitude
     * @param lattitude
     * @param elevation
     * @param centerAzimuth
     * @param horizontalFieldOfView
     * @param maxDist
     * @param width
     * @param height
     * @param superSampling
     * @return an Map with all the given element
     */
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
    
    //getters whose the name is clear
    
    public int get(UserParameter userParam) {
        return parameters.get(userParam);
    }
    
    public int getOberserverLong() {
        return parameters.get(UserParameter.OBSERVER_LONGITUDE);
    }
    
    public int getOberserverLati() {
        return parameters.get(UserParameter.OBSERVER_LATITUDE);
    }
    
    public int getObserverElev() {
        return parameters.get(UserParameter.OBSERVER_ELEVATION);
    }
    
    public int getCenterAzim() {
        return parameters.get(UserParameter.CENTER_AZIMUTH);
    }
    
    public int getHoriFieldOfView() {
        return parameters.get(UserParameter.HORIZONTAL_FIELD_OF_VIEW);
    }
    
    public int getMaxDist() {
        return parameters.get(UserParameter.MAX_DISTANCE);
    }
    
    public int getWidth() {
        return parameters.get(UserParameter.WIDTH);
    }
    
    public int getHeight() {
        return parameters.get(UserParameter.HEIGHT);
    }
    
    public int getSuperSamp() {
        return parameters.get(UserParameter.SUPER_SAMPLING_EXPONENT);
    }
    //End of getters
    
    
    /**
     * @return the parameters of the instance, after scaling the dimensions, as an instance of PanoramaParameters
     * regarding of the SuperSampling (height * 2^superSampling, width*2^superSampling)
     */
    public PanoramaParameters panoramaParameters() {
        
        int newHeight = (int) (scalb(getHeight(), getSuperSamp()));
        int newWidth= (int) (scalb(getWidth(), getSuperSamp()));
        
        return new PanoramaParameters(point, 
                                      getObserverElev(),
                                      centAzim,
                                      hFOV,
                                      distMeter,
                                      newWidth,
                                      newHeight);
}
    
    /**
     * @return the parameters of the instance 
     * without regarding the SuperSampling (width and height of the real image)
     */
    public PanoramaParameters panoramaDisplayParameters() {
        
        return new PanoramaParameters(point, 
                                      getObserverElev(),
                                      centAzim,
                                      hFOV,
                                      distMeter,
                                      getWidth(),
                                      getHeight());
    }
    
    @Override
    public boolean equals(Object that) {
        
        return that != null 
               && that.getClass() == this.getClass()
               && parameters.equals(((PanoramaUserParameters) that).parameters);
    }
    
    @Override
    public int hashCode() {

        return parameters.hashCode();
    }
}
