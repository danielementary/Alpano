/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano;

import java.util.function.DoubleUnaryOperator;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;

public final class PanoramaComputer {
    private final static double step = Math.scalb(1, 6);
    private final static double delta = Math.scalb(1, 2);
    
    private final ContinuousElevationModel dem;
    
    /**
     * create a instance of panoramaComputer with Continuous elevation model dem
     * throw nullpointerExcetion if dem == null
     * @param dem Continuous elevation model
     */
    public PanoramaComputer(ContinuousElevationModel dem) {
        if (dem == null) {
            throw new NullPointerException();
        }
        
        this.dem = dem;
    }
    
    /**
     * compute the panorama with the paramaters 
     * @param parameters parameters for panorama
     * @return instance of Panorama
     */
    public Panorama computePanorama(PanoramaParameters parameters) {
        
        Panorama.Builder builder = new Panorama.Builder(parameters);
        
        double x;
        double lowerBoundRoot;
        
        float distance, latitude, longitude, elevation, slope;
        
        for (int i = 0 ; i < parameters.width() ; ++i) {
            
            int j = parameters.height()-1;
            
            ElevationProfile profile = new ElevationProfile(dem, 
                    parameters.observerPosition(),
                    parameters.azimuthForX(i), 
                    parameters.maxDistance());
            
            x = 0;
            while (j >= 0 && x < Double.POSITIVE_INFINITY) {
                
               DoubleUnaryOperator distanceFunction = 
                          rayToGroundDistance(profile, 
                                  parameters.observerElevation(), 
                                  Math.tan(parameters.altitudeForY(j)));
                
               lowerBoundRoot = Math2.firstIntervalContainingRoot(
                        distanceFunction,
                                x, parameters.maxDistance(), step);
               
                if (lowerBoundRoot < Double.POSITIVE_INFINITY) {
                    x = Math2.improveRoot(distanceFunction,
                                 lowerBoundRoot, lowerBoundRoot + step, delta);
                } else {
                    x = lowerBoundRoot;
                }
                
                if (x < Double.POSITIVE_INFINITY) {
                    distance = (float) (x/(Math.cos(
                            Math.abs(parameters.altitudeForY(j)))));
                    
                    GeoPoint position = profile.positionAt(x);
                    latitude = (float) position.latitude();
                    longitude = (float) position.longitude();
                    elevation = (float) profile.elevationAt(x);
                    slope = (float) profile.slopeAt(x);
                    
                    builder.setDistanceAt(i, j, distance)
                            .setLatitudeAt(i, j,latitude)
                            .setLongitudeAt(i, j, longitude)
                            .setElevationAt(i, j, elevation)
                            .setSlopeAt(i, j, slope);
                }
                
                --j;
            }
        }
        return builder.build();
    }
   
    /**
     * gives the distance between the ray from the observer and the ground
     * @param profile elevation profile
     * @param ray0 
     * @param raySlope tan(a) where a is the angle between the horizontal and the ray
     * @return DoubleUnaryOperator distance between the ray from the observer
     *  and the ground
     */
    public static DoubleUnaryOperator rayToGroundDistance(ElevationProfile profile,
                                                    double ray0, double raySlope) {
        return x -> {
            return ray0 + x*raySlope - profile.elevationAt(x)
                                + ((1-0.13)/(2*Distance.EARTH_RADIUS))*Math2.sq(x);
        };
    }
    
    /**
     * @return the value of private final static value of step
     */
    public static double getStep() {
        return step;
    }
}
