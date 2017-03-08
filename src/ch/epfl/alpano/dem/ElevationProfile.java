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
    
    private ContinuousElevationModel elevMod;
    private GeoPoint origin;
    private double azimuth;
    private double length;

    /**
     * represents an altimetric profile corresponding to a great circle
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
        Preconditions.checkArgument(x <= length && x >= 0);
        
        GeoPoint point = positionAt(x);
        
        return elevMod.elevationAt(point);
    }
    
    public GeoPoint positionAt(double x) {
        Preconditions.checkArgument(x <= length);
        
        double originLatitude = origin.latitude();
        double originLongitude = origin.longitude();
        double radianLength = Distance.toRadians(x);
        double toMathAzimuth = Azimuth.toMath(azimuth);
        
        double pointLongitude = asin((sin(originLongitude) * cos(radianLength))
                + (cos(originLongitude) * sin(radianLength) * cos(toMathAzimuth))); 
        
//        double pointLongitude = asin(sin(originLongitude)*cos(radianLength) 
//                + cos(originLongitude)*sin(radianLength)*cos(toMathAzimuth));
        
        double pointLatitude = Azimuth.canonicalize((originLatitude
                -asin((sin(toMathAzimuth)*sin(radianLength))
                        /cos(pointLongitude))+PI))-PI;
        
        GeoPoint point = new GeoPoint(pointLongitude, pointLatitude);
        
        return point;
    }
    
    public double slopeAt(double x) {
        Preconditions.checkArgument(x <= length && x >= 0);
        
        
        GeoPoint point = positionAt(x);
        
        return elevMod.slopeAt(point);
    }
}
