package ch.epfl.alpano;

import static ch.epfl.alpano.Math2.PI2;

public interface Azimuth {
    
    public static boolean isCanonical(double azimuth) {
        if (azimuth >= 0 && azimuth < PI2) {
            return true;
        }
        
        return false;
    }
    
    public static double canonicalize(double azimuth) {
        if (isCanonical(azimuth)) {
            return azimuth;
        } else {
            while (!isCanonical(azimuth)) {
                azimuth -= PI2;
            }
            
            return azimuth;
        }
    }
    
    public static double toMath(double azimuth) {
        if (!isCanonical(azimuth)) {
            throw new IllegalArgumentException();
        }
        
        double angle = 360 - azimuth*360/PI2;
        
        return angle;
    }
    
    public static double fromMath(double azimuth) {
        double radianAng = PI2 - azimuth*PI2/360;
        
        if (!isCanonical(azimuth)) {
            throw new IllegalArgumentException();
        }
        
        return radianAng;
        
    }

    public static String toOctantString(double azimuth, String n, String e, String s, String w) {
        if (!isCanonical(azimuth)) {
            throw new IllegalArgumentException();
        }
        
        String cardinalDirection = "";
        
        //a compléter
        if (azimuth > 292.5 || azimuth < 67.5) {
            cardinalDirection += n;
        }
        
        return cardinalDirection;
    }

}
