package ch.epfl.alpano;

/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

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
       double angle = distanceInMeters/EARTH_RADIUS;
       
       return angle;
    }
    
    /**
     * transform an angle into a distance on earth
     * @param distanceInRadians to transform
     * @return distance in meters
     */
    public static double toMeters(double distanceInRadians) {
        double distance = distanceInRadians*EARTH_RADIUS;
        
        return distance;
    }
}
