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
    
    

}
