/*
 *	Author:      Samuel Chassot (270955)
 *	Date:        22 févr. 2017
 */


package ch.epfl.alpano;
import java.lang.Math;
import java.util.Locale;

public final class GeoPoint {
    
    final double longitude;
    final double latitude;
    
    /**
     * Create an instance of GeoPoint representing a Point on the Earth's surface by its longitude and latitude
     * @param longitude in radians
     * @param latitude in radians
     */
    GeoPoint(double longitude, double latitude){
        Preconditions.checkArgument(longitude <= Math.PI && longitude >= - Math.PI);
        Preconditions.checkArgument(latitude <= Math.PI/2 && latitude >= - Math.PI/2);
        
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    /**
     * give the longitude of the point
     * @return longitude in double
     */
    public double longitude(){
        return longitude;
    }
    
    /**
     * give the latitude of the point
     * @return latitude in double
     */
    public double latitude(){
        return latitude;
    }
    
    /**
     * calculate the distance between the point this and that
     * @param that other GeoPoint
     * @return the distance between these points in double
     */
    public double distanceTo(GeoPoint that){
        double sqrt = Math.sqrt(Math2.haversin(this.latitude - that.latitude) + Math.cos(this.latitude)
        * Math.cos(that.latitude) * Math2.haversin(this.longitude - that.longitude));
        
        double alpha = 2*Math.asin(sqrt);
        
        return Distance.toMeters(alpha);
    }
    
    /**
     * return the azimuth of that according to this
     * @param that other GeoPoint
     * @return azimuth in radians
     */
    public double azimuthTo(GeoPoint that){
        double beta = Math.atan2(Math.sin(this.longitude - that.longitude) * Math.cos(that.latitude),
                Math.cos(this.latitude) * Math.sin(that.latitude) - Math.sin(this.latitude)
                * Math.cos(that.latitude) * Math.cos(this.longitude - that.longitude));
        
        return Azimuth.fromMath(beta);
    }
    
    @Override
    public String toString(){
        Locale region = null;
        double degreeLatitude = latitude * 180 / Math.PI;
        double degreeLongitude = longitude * 180 / Math.PI;
        
        String str = String.format(region, "(%.4f,%.4f)", degreeLongitude, degreeLatitude);
        return str;
    }
    
    
       
}
