package ch.epfl.alpano;

/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import java.util.Locale;

public final class GeoPoint {
    
    private final double longitude;
    private final double latitude;
    
    /**
     * Creates an instance of GeoPoint representing a Point on the Earth's surface by its longitude and latitude
     * @param longitude in radians
     * @param latitude in radians
     */
    public GeoPoint(double longitude, double latitude) {
        Preconditions.checkArgument(longitude <= PI && longitude >= -PI);
        Preconditions.checkArgument(latitude <= PI/2 && latitude >= -PI/2);
        
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    /**
     * gives the longitude of the point
     * @return longitude in double
     */
    public final double longitude() {
        return longitude;
    }
    
    /**
     * gives the latitude of the point
     * @return latitude in double
     */
    public final double latitude() {
        return latitude;
    }
    
    /**
     * calculates the distance between the point this and that
     * @param that other GeoPoint
     * @return the distance between these points in double
     */
    public final double distanceTo(GeoPoint that) {
        double sqrt = sqrt(Math2.haversin(this.latitude - that.latitude)
                + cos(this.latitude) * cos(that.latitude) 
                * Math2.haversin(this.longitude - that.longitude));
        
        return Distance.toMeters(2*asin(sqrt));
    }
    
    /**
     * returns the azimuth of that according to this
     * @param that other GeoPoint
     * @return azimuth in radians
     */
    public final double azimuthTo(GeoPoint that) {
        double beta = atan2(sin(this.longitude - that.longitude)
                * cos(that.latitude), cos(this.latitude) 
                * sin(that.latitude) - sin(this.latitude)
                * cos(that.latitude) * cos(this.longitude - that.longitude));
        
        return Azimuth.fromMath(Azimuth.canonicalize(beta));
    }
    
    /**
     * displays the coordinates of the instance's point
     * with 4 decimals precision between brackets
     */
    @Override
    public final String toString() {
        Locale region = null;
        
        String str = String.format(region, "(%.4f,%.4f)",Math.toDegrees(longitude) , Math.toDegrees(latitude));
        
        return str;
    }   
}
