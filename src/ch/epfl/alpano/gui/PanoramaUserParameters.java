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
    
    /*
     * construct an instance of PanoramaUserParameters with the given map
     * after having corrected all values
     */
    public PanoramaUserParameters(Map<UserParameter, Integer> parameters) {
        
        parameters.replaceAll((k, v) -> k.sanitize(v));
        
        int maxHeight = ((170*(parameters.get(UserParameter.WIDTH)-1))
                            /parameters.get(UserParameter.HORIZONTAL_FIELD_OF_VIEW))+1;
        
        if (parameters.get(UserParameter.HEIGHT) > maxHeight) {
            parameters.replace(UserParameter.HEIGHT, UserParameter.HEIGHT.sanitize(maxHeight)); 
        }
        
        this.parameters = Collections.unmodifiableMap(new EnumMap<>(parameters));
    }
    
    /*
     * construct an instance of PanoramaUserParameters with the given elements
     * after having collected them in a map
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
    
    /**
     * @return the parameters of the instance after scaling the dimensions
     */
    public PanoramaParameters panoramaParameters() {
//        Map<UserParameter, Integer> paramMap = new EnumMap<>(parameters);
//        
//        paramMap.replace(UserParameter.HEIGHT, (int) (getHeight()*scalb(1, getSuperSamp())));
//        paramMap.replace(UserParameter.WIDTH, (int) (getWidth()*scalb(1, getSuperSamp())));
//        
//        return Collections.unmodifiableMap(new EnumMap<>(paramMap));
        
        int newHeight = (int) (scalb(getHeight(), getSuperSamp()));
        int newWidth= (int) (scalb(getWidth(), getSuperSamp()));
        
        return new PanoramaParameters(
                new GeoPoint(Math.toRadians(getOberserverLong()*1e-4), Math.toRadians(getOberserverLati()*1e-4)), 
                getObserverElev(),
                Math.toRadians(getCenterAzim()), Math.toRadians(getHoriFieldOfView()),
                getMaxDist(), newWidth, newHeight);
        
        
    }
    
    /**
     * @return the parameters of the instance
     */
    public PanoramaParameters panoramaDisplayParameters() {
//        return Collections.unmodifiableMap(new EnumMap<>(parameters));
        return new PanoramaParameters(
                new GeoPoint(Math.toRadians(getOberserverLong()*1e-4), Math.toRadians(getOberserverLati()*1e-4)), 
                getObserverElev(),
                Math.toRadians(getCenterAzim()), Math.toRadians(getHoriFieldOfView()),
                getMaxDist(), getWidth(), getHeight());
        
    }
    
    // defines how PanoramaUserParameters should be compared
    
    @Override
    public boolean equals(Object that) {
        
        if (that == null || that.getClass() != this.getClass()) {
            return false;
        }
        
        return parameters.equals(((PanoramaUserParameters) that).parameters);
    }
    
    @Override
    public int hashCode() {

        return parameters.hashCode();
    }
}
