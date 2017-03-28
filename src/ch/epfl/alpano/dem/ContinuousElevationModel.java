package ch.epfl.alpano.dem;

/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

import static java.util.Objects.requireNonNull;

import ch.epfl.alpano.Distance;
import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;

public final class ContinuousElevationModel {
    
    private final DiscreteElevationModel dem;
    private final static double D_NORTH_SUD = Math2.PI2*Distance.EARTH_RADIUS
                        /(Math2.PI2*DiscreteElevationModel.SAMPLES_PER_RADIAN);
    
    public ContinuousElevationModel(DiscreteElevationModel dem) {
        requireNonNull(dem);
        
        this.dem = dem;
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
        
        double elevation = Math2.bilerp(elev00, elev10, elev01, elev11, x, y) ;
        
        return elevation;
    }
    
    private double elevationExtension(int x, int y) {
        if (dem.extent().contains(x, y)) {
            return dem.elevationSample(x, y);
        } else {
            return 0.0;
        }
    }
    
    private double slopeExtension(int x, int y) {
        if (dem.extent().contains(x, y)) {
            double deltaZa = elevationExtension(x+1, y)-elevationExtension(x, y);
            double deltaZb = elevationExtension(x, y+1)-elevationExtension(x, y);
            double slope = Math.acos(D_NORTH_SUD/(Math.sqrt(Math2.sq(deltaZa)
                                 +Math2.sq(deltaZb) + Math2.sq(D_NORTH_SUD))));
            
            return slope;
        } else {
            return 0;
        }
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
        
        double slope = Math2.bilerp(slope00, slope10, slope01, slope11, x, y) ;
        
        return slope;
    }
}
