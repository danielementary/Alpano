package ch.epfl.alpano;

import static ch.epfl.alpano.Math2.PI2;
import static ch.epfl.alpano.Math2.PI_OVER8;
import static ch.epfl.alpano.Preconditions.checkArgument;

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
        checkArgument(isCanonical(azimuth));
        
        double angle = PI2 - azimuth;
        
        return canonicalize(angle);
    }
    
    public static double fromMath(double azimuth) {
        checkArgument(isCanonical(azimuth));
        
        double radianAng = PI2 - azimuth;
        
        return canonicalize(radianAng);
        
    }

    public static String toOctantString(double azimuth, String n, String e, String s, String w) {
        checkArgument(isCanonical(azimuth));
        
        String cardinalDirection = "";
        
        if (azimuth > PI2-3*PI_OVER8 || azimuth < 3*PI_OVER8) {
            cardinalDirection += n;
        } else if (azimuth <= 180+3*PI_OVER8 && azimuth >= 180-3*PI_OVER8) {
            cardinalDirection += s;
        }

        return cardinalDirection;
    }

}
