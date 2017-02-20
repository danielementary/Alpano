package ch.epfl.alpano;

public interface Distance {
    
    public static double EARTH_RADIUS = 6371000;
    
    public static double toRadians(double distanceInMeters) {
       double angle = distanceInMeters/EARTH_RADIUS;
       
       return angle;
    }
    
    public static double toMeters(double distanceInRadians) {
        double distance = distanceInRadians*EARTH_RADIUS;
        
        return distance;
    }
}
