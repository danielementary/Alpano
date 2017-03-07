package ch.epfl.alpano.dem;

/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

import static java.util.Objects.requireNonNull;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Preconditions;
import ch.epfl.alpano.Azimuth;

public final class ElevationProfile {
    
    ContinuousElevationModel elevMod;
    GeoPoint origin;
    double azimuth;
    double length;

    /**
     * represents an aritmetic profile corresponding to a great circle
     * @param elevationModel
     * @param origin
     * @param azimuth
     * @param length
     */
    ElevationProfile(ContinuousElevationModel elevationModel, GeoPoint origin,
                                              double azimuth, double length) {
        requireNonNull(elevationModel);
        requireNonNull(origin);
        
        Preconditions.checkArgument(Azimuth.isCanonical(azimuth));
        Preconditions.checkArgument(length > 0);
        
        this.elevMod = elevationModel;
        this.origin = origin;
        this.azimuth = azimuth;
        this.length = length;
        
    }
    
    public double elevationAt(double x) {
        return 0.0;
    }
    
    public GeoPoint positionAt(double x) {
        return null;
    }
    
    public double slopeAt(double x) {
        return 0.0;
    }

}
