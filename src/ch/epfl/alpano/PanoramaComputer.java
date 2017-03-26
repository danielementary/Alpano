package ch.epfl.alpano;

/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

import java.util.function.DoubleUnaryOperator;
import static java.util.Objects.requireNonNull;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;

public final class PanoramaComputer {
    
    private final ContinuousElevationModel dem;
    
    public PanoramaComputer(ContinuousElevationModel dem) {
        this.dem = requireNonNull(dem);
    }
    
    public Panorama computePanorama(PanoramaParameters parameters) {
        
        Panorama.Builder builder = new Panorama.Builder(parameters);
        
        double step = Math.scalb(1, 6);
        double delta = Math.scalb(1, 2);
        
        for (int i = 0 ; i < parameters.width() ; ++i) {
            int j = parameters.height()-1;
            
            ElevationProfile profile = new ElevationProfile(dem, parameters.observerPosition(),
                    parameters.azimuthForX(i), parameters.maxDistance());
            
            double x = 0;
            
            while (j >= 0 && x < Double.POSITIVE_INFINITY) {
                
               double lowerBoundRoot = Math2.firstIntervalContainingRoot(
                        rayToGroundDistance(profile, parameters.observerElevation(), 
                                parameters.altitudeForY(j)),
                                x, parameters.maxDistance(), step);
               
                if (lowerBoundRoot < Double.POSITIVE_INFINITY) {
                    x = Math2.improveRoot(rayToGroundDistance(profile, 
                                    parameters.observerElevation(),
                                    parameters.altitudeForY(j)),
                                    lowerBoundRoot, lowerBoundRoot + step, delta);
                } else {
                    x = lowerBoundRoot;
                }
                
                if (x < Double.POSITIVE_INFINITY) {
                    float distance = (float) (x/(Math.cos(Math.abs(parameters.altitudeForY(j)))));
                    float latitude = (float) profile.positionAt(x).latitude();
                    float longitude = (float) profile.positionAt(x).longitude();
                    float elevation = (float) profile.elevationAt(x);
                    float slope = (float) profile.slopeAt(x);
                    
                    builder.setDistanceAt(i, j, distance);
                    builder.setLatitudeAt(i, j, latitude);
                    builder.setLongitudeAt(i, j, longitude);
                    builder.setElevationAt(i, j, elevation);
                    builder.setSlopeAt(i, j, slope);
                }
                
                --j;
            }
        }

        return builder.build();
    }
    
    public static DoubleUnaryOperator rayToGroundDistance(ElevationProfile profile, double ray0, double raySlope) {
        return x -> {
            return ray0 + x*Math.tan(raySlope) - profile.elevationAt(x) + ((1-0.13)/(2*Distance.EARTH_RADIUS))*Math2.sq(x);
        };
    }
}
