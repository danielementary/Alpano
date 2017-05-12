/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.dem;

import static ch.epfl.alpano.Distance.toMeters;
import static ch.epfl.alpano.Math2.PI2;
import static ch.epfl.alpano.Math2.bilerp;
import static ch.epfl.alpano.Math2.sq;
import static ch.epfl.alpano.dem.DiscreteElevationModel.SAMPLES_PER_RADIAN;

import java.util.Objects;

import ch.epfl.alpano.GeoPoint;

public final class ContinuousElevationModel {
    
    private final DiscreteElevationModel dem;
    
    private final static double D_NORTH_SUD = toMeters(PI2)/(PI2*SAMPLES_PER_RADIAN);
    
    public ContinuousElevationModel(DiscreteElevationModel dem) {
        this.dem = Objects.requireNonNull(dem);
    }
    
    /**
     * gives the elevation of a GeoPoint interpolated by the 4 points around
     * @param p a geopoint
     * @return the elevation
     */
    public double elevationAt(GeoPoint p) {
        
        double longitudeIndex = DiscreteElevationModel.sampleIndex(p.longitude());
        double latitudeIndex = DiscreteElevationModel.sampleIndex(p.latitude());
        
        int longitudeIndexDown = (int)Math.floor(longitudeIndex);
        int longitudeIndexUp = (int)Math.ceil(longitudeIndex);
             
        int latitudeIndexDown = (int)Math.floor(latitudeIndex);
        int latitudeIndexUp = (int)Math.ceil(latitudeIndex);
        
        double elev00 = elevationExtension(longitudeIndexDown, latitudeIndexDown);
        double elev10 = elevationExtension(longitudeIndexUp, latitudeIndexDown);
        double elev01 = elevationExtension(longitudeIndexDown, latitudeIndexUp);
        double elev11 = elevationExtension(longitudeIndexUp, latitudeIndexUp);
        
        double x = longitudeIndex-longitudeIndexDown;
        double y = latitudeIndex-latitudeIndexDown;
        
        double elevation = bilerp(elev00, elev10, elev01, elev11, x, y) ;
        
        return elevation;
    }
    
    private double elevationExtension(int x, int y) {
        if (dem.extent().contains(x, y)) {
            return dem.elevationSample(x, y);
        }
        
        return 0.0;
    }
    
    private double slopeExtension(int x, int y) {
        if (dem.extent().contains(x, y)) {
            double elevExtXY = elevationExtension(x, y);
            
            double deltaZa = elevationExtension(x+1, y)-elevExtXY;
            double deltaZb = elevationExtension(x, y+1)-elevExtXY;
            
            double slope = Math.acos(D_NORTH_SUD/(Math.sqrt(sq(deltaZa)
                                 +sq(deltaZb) + sq(D_NORTH_SUD))));
            
            return slope;
        }
        
        return 0;
    }
    
    /**
     * gives the slope of the ground at the GeoPoint p interpolated by the 4 around points 
     * (angle between normal vector to surface and the vertical vector)
     * @param p the GeoPoint
     * @return the slope (angle)
     */
    public double slopeAt(GeoPoint p) {
        double longitudeIndex = DiscreteElevationModel.sampleIndex(p.longitude());
        double latitudeIndex = DiscreteElevationModel.sampleIndex(p.latitude());
        
        int longitudeIndexDown = (int)Math.floor(longitudeIndex);
        int longitudeIndexUp = (int)Math.ceil(longitudeIndex);
        
        int latitudeIndexDown = (int)Math.floor(latitudeIndex);
        int latitudeIndexUp = (int)Math.ceil(latitudeIndex);
        
        double slope00 = slopeExtension(longitudeIndexDown, latitudeIndexDown);
        double slope10 = slopeExtension(longitudeIndexUp, latitudeIndexDown);
        double slope01 = slopeExtension(longitudeIndexDown, latitudeIndexUp);
        double slope11 = slopeExtension(longitudeIndexUp, latitudeIndexUp);
        
        double x = longitudeIndex-longitudeIndexDown;
        double y = latitudeIndex-latitudeIndexDown;
        
        double slope = bilerp(slope00, slope10, slope01, slope11, x, y) ;
        
        return slope;
    }
}
