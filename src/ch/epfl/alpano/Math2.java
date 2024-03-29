/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano;

import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.lang.Math.PI;
import static java.lang.Math.floor;
import static java.lang.Math.sin;

import java.util.function.DoubleUnaryOperator;

public interface Math2 {
    
    /**
     * 2 times PI
     */
    public static double PI2 = 2*PI;
    
    /**
     * PI divided by 8
     */
    public static double PI_OVER8 = PI/8;
    
    /**
     * squares x
     * @param x what we want to square
     * @return x squared
     */
    public static double sq(double x) {
        return x*x;
    }
    
    /**
     * return x modulo y with the floor rounding
     * @param x denominator
     * @param y divisor
     * @return x modulo y
     */
    public static double floorMod(double x, double y) {
        return x-floor(x/y)*y;
    }
    
    /**
     * return the haversin of x
     * @param x
     * @return (sin(x/2))^2
     */
    public static double haversin(double x) {
        return sq(sin(x/2));
    }
    
    /**
     * give the angular distance between a1 and a2
     * @param a1 angle 1
     * @param a2 angle 2
     * @return angular distance
     */
    public static double angularDistance(double a1, double a2) {
        return floorMod((a2-a1 + PI), PI2) - PI;
    }
    
    /**
     * give a linear interpolation with y0 and y1 to find value of x
     * @param y0 first point of the function
     * @param y1 second point of the function
     * @param x the point you want to interpolate
     * @return the value of x in the function found with interpolation
     */
    public static double lerp(double y0, double y1, double x) {
        return (y1-y0)*x + y0;
    }
    /**
     * make a bilinear interpolation with 4 point
     * @param z00 first parameter
     * @param z10 second parameter
     * @param z01 third parameter
     * @param z11 fourth parameter
     * @param x 
     * @param y
     * @return the bilinear interpolation
     */
    public static double bilerp(double z00, double z10, 
                                double z01, double z11,
                                double x, double y) {
        
        return lerp(lerp(z00, z10, x), lerp(z01, z11, x), y);
    }
    
    /**
     * return the lower bound of the smallest interval of size dX 
     * containing a root of the function f, between maxX and minX
     * @param f the function for which we are seeking a root
     * @param minX the lower bound of interval in which we are seeking
     * @param maxX the upper bound of interval in which we are seeking
     * @param dX the size of the the interval we want
     * @return the lower bound of the interval of size dX containing the root
     */
    public static double firstIntervalContainingRoot(DoubleUnaryOperator f, 
                                                     double minX,
                                                     double maxX,
                                                     double dX) {
        checkArgument(dX > 0);
        if (f == null) {
            throw new NullPointerException();
        }
        
        double currentMinX = minX;
        
        while (currentMinX + dX <= maxX) {
            if (f.applyAsDouble(currentMinX)
                *f.applyAsDouble(currentMinX + dX) <= 0) {
                
                return currentMinX;
            }
            
            currentMinX += dX;
        }
        
        return Double.POSITIVE_INFINITY;
    }
    
    /**
     * find an interval smaller than epsilon containing a root
     * @param f function for which we are seeking a root
     * @param x1 first bound
     * @param x2 second bound
     * @param epsilon precision we want
     * @return the lower bound of the interval smaller 
     * or equal to epsilon containing the root
     */
    public static double improveRoot(DoubleUnaryOperator f, 
                                     double x1, double x2,
                                     double epsilon) {
        
        checkArgument(f.applyAsDouble(x1)*f.applyAsDouble(x2) <= 0);
        
        if (x1 > x2) {
            double temp = x1;
            
            x1 = x2;
            x2 = temp;
        }
        
        double middle = (x1+x2)/2;
        
        while (x2-x1 > epsilon) {
            if (f.applyAsDouble(middle)
                *f.applyAsDouble(x1) <= 0) {
                
                x2 = middle;
            } else {
                x1 = middle;
            }
            
            middle = (x1+x2)/2;
        }
        
        return x1;
    } 
}
