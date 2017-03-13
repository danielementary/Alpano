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
import ch.epfl.alpano.Math2;
import static java.lang.Math.sin;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.asin;

public final class ElevationProfile {
    
    private ContinuousElevationModel elevMod;
    private GeoPoint origin;
    private double azimuth;
    private double length;
    private final int DIFFERENCE = 4096;
    
    private double[][] positions;

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

        int positionLength = (int)Math.ceil(length/DIFFERENCE);
        positions = new double[positionLength+1][3];
        
        double initLength = 0; 
        for (int i = 0; i <= positionLength; ++i) {
           
            
            double originLatitude = origin.latitude();
            double originLongitude = origin.longitude();
            double radianLength = Distance.toRadians(initLength);
            double toMathAzimuth = Azimuth.toMath(azimuth);
            
            double pointLatitude = asin((sin(originLatitude) * cos(radianLength))
                    + (cos(originLatitude) * sin(radianLength) * cos(toMathAzimuth)));
            
            double pointLongitude = Azimuth.canonicalize((originLongitude
                    -asin((sin(toMathAzimuth)*sin(radianLength))
                            /cos(pointLatitude))+PI))-PI;
            
            double[] nextArray = new double[] {initLength, pointLongitude, pointLatitude};
            positions[i] = nextArray;
            initLength += DIFFERENCE;
        }
        
//        GeoPoint nextPoint = positionAt(initLength);
//        positions[positionLength] = new double[] {initLength, nextPoint.longitude(), nextPoint.latitude()};
    }
    
    /*
    public double elevationAt(double x) {
        Preconditions.checkArgument(x <= length && x >= 0);
        
        GeoPoint point = positionAt(x);
        
        return elevMod.elevationAt(point);
    }
    */
    
    public double elevationAt(double x) {
        Preconditions.checkArgument(x <= length && x >= 0);
        
        GeoPoint point = positionAt(x);
        
        return elevMod.elevationAt(point);
    }
    
    public GeoPoint positionAt(double x) {
        Preconditions.checkArgument(x <= length && x >= 0);
        
        double[] lowerBound;
        double[] upperBound;
        
        double xPoint;
        
        int indexLowBound = (int)Math.floor(x/DIFFERENCE);
        int indexUpBound = indexLowBound+1;
        if(indexUpBound > positions.length -1){indexUpBound -=1;
            }
        
        lowerBound = positions[indexLowBound];
        upperBound = positions[indexUpBound];
        
        xPoint = (x/DIFFERENCE-indexLowBound);
        
        double pointLongitude = Math2.lerp(lowerBound[1], upperBound[1], xPoint);
        double pointLatitude = Math2.lerp(lowerBound[2], upperBound[2], xPoint);
        
        GeoPoint point = new GeoPoint(pointLongitude, pointLatitude);
        
        return point;
    }
    
    public double slopeAt(double x) {
        Preconditions.checkArgument(x <= length && x >= 0);
        
        GeoPoint point = positionAt(x);
        
        return elevMod.slopeAt(point);
    }
}
