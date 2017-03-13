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
    
    //distance between points we really calculate the altitude
    private final int DELTA = 4096;
    //array for saving calculated points
    private double[][] points;

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
        
        //length of array containing all points
        int positionLength = (int)Math.ceil(length/DELTA);
        points = new double[positionLength][3];
        
        //calculating and filling the array
        double interLength = 0; 
        for (int i = 0; i <= positionLength; ++i) {
            double originLatitude = origin.latitude();
            double originLongitude = origin.longitude();
            double radianLength = Distance.toRadians(interLength);
            double toMathAzimuth = Azimuth.toMath(azimuth);
            
            double pointLatitude = asin((sin(originLatitude) * cos(radianLength))
                    + (cos(originLatitude) * sin(radianLength) * cos(toMathAzimuth)));
            
            double pointLongitude = Azimuth.canonicalize((originLongitude
                    -asin((sin(toMathAzimuth)*sin(radianLength))
                            /cos(pointLatitude))+PI))-PI;
            
            double[] nextArray = new double[] {interLength, pointLongitude, pointLatitude};
            points[i] = nextArray;
            
            interLength += DELTA;
        }
    }
    
    /**
     * calculating the elevation at length x
     * @param x distance in meter to origin
     * @return the elevation as a double
     */
    public double elevationAt(double x) {
        Preconditions.checkArgument(x <= length && x >= 0);
        
        GeoPoint point = positionAt(x);
        
        return elevMod.elevationAt(point);
    }
    
    /**
     * calculates a GeoPoint situated at distance x of origin
     * @param x distance in meter to origin
     * @return corresponding GeoPoint
     */
    public GeoPoint positionAt(double x) {
        Preconditions.checkArgument(x <= length && x >= 0);
        
        double[] lowerBound;
        double[] upperBound;
        
        double xPoint;
        
        int indexLowBound = (int)Math.floor(x/DELTA);
        int indexUpBound = indexLowBound+1;
        
        if(indexUpBound > points.length-1){
            indexUpBound -= 1;
        }
        
        lowerBound = points[indexLowBound];
        upperBound = points[indexUpBound];
        
        xPoint = (x/DELTA-indexLowBound);
        
        double pointLongitude = Math2.lerp(lowerBound[1], upperBound[1], xPoint);
        double pointLatitude = Math2.lerp(lowerBound[2], upperBound[2], xPoint);
        
        GeoPoint point = new GeoPoint(pointLongitude, pointLatitude);
        
        return point;
    }
    
    /**
     * calculating the slope at length x
     * @param x distance in meter to origin
     * @return the slope as a double
     */
    public double slopeAt(double x) {
        Preconditions.checkArgument(x <= length && x >= 0);
        
        GeoPoint point = positionAt(x);
        
        return elevMod.slopeAt(point);
    }
}
