/*
 *	Author:      Samuel Chassot (270955)
 *	Date:        22 févr. 2017
 */


package ch.epfl.alpano;
import java.lang.Math;

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
        
        
}
