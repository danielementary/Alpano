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
import ch.epfl.alpano.Distance;
import static java.lang.Math.sin;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.asin;

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
        Preconditions.checkArgument(x <= length);
        
        GeoPoint point = positionAt(x);
        
        return elevMod.elevationAt(point);
    }
    
    public GeoPoint positionAt(double x) {
        Preconditions.checkArgument(x <= length);
        
        double originLatitude = origin.latitude();
        double originLongitude = origin.longitude();
        double radianLength = Distance.toRadians(length);
        double toMathAzimuth = Azimuth.toMath(azimuth);
        
        double pointLatitude = asin(sin(originLongitude)*cos(radianLength) 
                + cos(originLongitude)*sin(radianLength)*cos(toMathAzimuth));
        double pointLongitude = Azimuth.canonicalize((originLatitude
                -asin((sin(toMathAzimuth)*sin(radianLength))
                        /cos(originLongitude))+PI))-PI;
        
        GeoPoint point = new GeoPoint(pointLatitude, pointLongitude);
        
        return point;
    }
    
    public double slopeAt(double x) {
        Preconditions.checkArgument(x <= length);
        
        GeoPoint point = positionAt(x);
        
        return elevMod.slopeAt(point);
    }
}
