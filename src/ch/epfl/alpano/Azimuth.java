/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano;

import static ch.epfl.alpano.Math2.PI2;
import static ch.epfl.alpano.Math2.PI_OVER8;
import static ch.epfl.alpano.Math2.floorMod;
import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.lang.Math.PI;


public interface Azimuth {
    
    /**
     * check if a azimuth is in [0,PI2[
     * @param azimuth to check
     * @return boolean, true if azimuth is in [0,PI2[, false otherwise
     */
    public static boolean isCanonical(double azimuth) {
        if (azimuth >= 0 && azimuth < PI2) {
            return true;
        }
        
        return false;
    }
    
    /**
     * transform azimuth into an equivalent between [0,PI2[
     * @param azimuth to modify
     * @return equivalent azimuth between [0,PI2[
     */
    public static double canonicalize(double azimuth) {
        return floorMod(azimuth, PI2);
    }
    
    /**
     * transform azimuth into radian angle
     * @param azimuth 
     * @return radian angle equivalent to azimuth
     */
    public static double toMath(double azimuth) {
        checkArgument(isCanonical(azimuth));
        
        double radianAngle = PI2 - azimuth;
        
        return canonicalize(radianAngle);
    }
    
    /**
     * transform radian angle into azimuth
     * @param azimuth, radian angle
     * @return azimuth equivalent to radian angle
     */
    public static double fromMath(double radianAngle) {
        checkArgument(isCanonical(radianAngle));
        
        double azimuth = PI2 - radianAngle;
        
        return canonicalize(azimuth);
    }

    /**
     * reprents an azimuth by a string
     * @param azimuth to represent
     * @param n north String
     * @param e est String
     * @param s South String
     * @param w West String
     * @return string which represents the cardinal direction corresponding to the azimuth
     */
    public static String toOctantString(double azimuth, 
                                            String n, String e,
                                            String s, String w) {
        
        checkArgument(isCanonical(azimuth));
        
        StringBuilder cardDir = new StringBuilder();
        
        if (azimuth > PI2-3*PI_OVER8 || azimuth < 3*PI_OVER8) {
            cardDir.append(n);
        } else if (azimuth <= PI+3*PI_OVER8 && azimuth >= PI-3*PI_OVER8) {
            cardDir.append(s);
        }
        
        if (azimuth > PI_OVER8 && azimuth < PI-PI_OVER8) {
            cardDir.append(e);
        } else if (azimuth > PI+PI_OVER8 && azimuth < PI2-PI_OVER8) {
            cardDir.append(w);
        }

        return cardDir.toString();
    }

}
