/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.dem;

import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.lang.Math.PI;
import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.util.Objects.requireNonNull;

import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.Distance;
import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;

public final class ElevationProfile {
    
    private final ContinuousElevationModel elevMod;
    private final double length;
    
    //distance between points we really calculate the altitude
    private final static int DELTA = 4096;
    //array for saving calculated points
    private final double[][] points;

    /**
     * represents an altimetric profile corresponding to a great circle
     * @param elevationModel
     * @param origin
     * @param azimuth
     * @param length
     */
    public ElevationProfile(ContinuousElevationModel elevationModel, 
                            GeoPoint origin,
                            double azimuth, double length) {
        
        if (origin == null) {
            throw new NullPointerException();
        }
        
//        checkArgument(Azimuth.isCanonical(azimuth));
//        checkArgument(length > 0);
        
        this.elevMod = requireNonNull(elevationModel);
        this.length = length;
        
        //length of array containing all points
        int positionLength = (int)Math.ceil(length/DELTA);
        points = new double[positionLength+1][3];
        
        //calculating and filling the array
        for (int i = 0; i <= positionLength; ++i) {
            
            double originLatitude = origin.latitude();
            double originLongitude = origin.longitude();
            double radianLength = Distance.toRadians(DELTA*i);
            double toMathAzimuth = Azimuth.toMath(azimuth);
            
            double pointLatitude = asin((sin(originLatitude) * cos(radianLength))
                                   +(cos(originLatitude) * sin(radianLength) * cos(toMathAzimuth)));
            
            double pointLongitude = Azimuth.canonicalize((originLongitude
                                    -asin((sin(toMathAzimuth)*sin(radianLength))
                                    /cos(pointLatitude))+PI))-PI;
            
            double[] nextArray = new double[] {pointLongitude, pointLatitude};
            
            points[i] = nextArray;
        }
    }
    
    /**
     * calculating the elevation at length x
     * @param x distance in meter to origin
     * @return the elevation as a double
     */
    public double elevationAt(double x) {
//        checkArgument(x <= length && x >= 0);
        
        GeoPoint point = positionAt(x);
        
        return elevMod.elevationAt(point);
    }
    
    /**
     * calculates a GeoPoint situated at distance x of origin
     * @param x distance in meter to origin
     * @return corresponding GeoPoint
     */
    public GeoPoint positionAt(double x) {
//        checkArgument(x <= length && x >= 0);
        
        double[] lowerBound;
        double[] upperBound;
        
        double xPoint;
        
        int indexLowBound = (int)Math.floor(x/DELTA);
        int indexUpBound = (int)Math.ceil(x/DELTA);
        
        lowerBound = points[indexLowBound];
        upperBound = points[indexUpBound];
        
        xPoint = (x/DELTA-indexLowBound);
        
        double pointLongitude = Math2.lerp(lowerBound[0], upperBound[0], xPoint);
        double pointLatitude = Math2.lerp(lowerBound[1], upperBound[1], xPoint);
        
        GeoPoint point = new GeoPoint(pointLongitude, pointLatitude);
        
        return point;
    }
    
    /**
     * calculating the slope at length x
     * @param x distance in meter to origin
     * @return the slope as a double
     */
    public double slopeAt(double x) {
//        checkArgument(x <= length && x >= 0);
        
        GeoPoint point = positionAt(x);
        
        return elevMod.slopeAt(point);
    }
}
