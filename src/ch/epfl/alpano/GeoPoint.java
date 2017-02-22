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
    
    final double longitude;
    final double latitude;
    
    /**
     * Creates an instance of GeoPoint representing a Point on the Earth's surface by its longitude and latitude
     * @param longitude in radians
     * @param latitude in radians
     */
    GeoPoint(double longitude, double latitude){
        Preconditions.checkArgument(longitude <= PI && longitude >= -PI);
        Preconditions.checkArgument(latitude <= PI/2 && latitude >= -PI/2);
        
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    /**
     * gives the longitude of the point
     * @return longitude in double
     */
    public double longitude(){
        return longitude;
    }
    
    /**
     * gives the latitude of the point
     * @return latitude in double
     */
    public double latitude(){
        return latitude;
    }
    
    /**
     * calculates the distance between the point this and that
     * @param that other GeoPoint
     * @return the distance between these points in double
     */
    public double distanceTo(GeoPoint that){
        double sqrt = sqrt(Math2.haversin(this.latitude - that.latitude)
                                    + cos(this.latitude) * cos(that.latitude) 
                                    * Math2.haversin(this.longitude - that.longitude));
        
        double alpha = 2*asin(sqrt);
        
        return Distance.toMeters(alpha);
    }
    
    /**
     * returns the azimuth of that according to this
     * @param that other GeoPoint
     * @return azimuth in radians
     */
    public double azimuthTo(GeoPoint that){
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
    public String toString(){
        Locale region = null;
        
        double degreeLatitude = latitude*180 / PI;
        double degreeLongitude = longitude*180 / PI;
        
        String str = String.format(region, "(%.4f,%.4f)", degreeLongitude, degreeLatitude);
        
        return str;
    }
       
}
