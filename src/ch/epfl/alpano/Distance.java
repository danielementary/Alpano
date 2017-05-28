/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano;

public interface Distance {
    
    /**
     * earth's radius in meters
     */
    public static double EARTH_RADIUS = 6371000;
    
    /**
     * transform a distance on earth into an angle
     * @param distanceInMeters to transform
     * @return radian angle
     */
    public static double toRadians(double distanceInMeters) {
        return distanceInMeters/EARTH_RADIUS;
    }
    
    /**
     * transform an angle into a distance on earth
     * @param distanceInRadians to transform
     * @return distance in meters
     */
    public static double toMeters(double distanceInRadians) {
        return distanceInRadians*EARTH_RADIUS;
    }
}
