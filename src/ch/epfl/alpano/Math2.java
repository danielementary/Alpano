package ch.epfl.alpano;

import static java.lang.Math.PI;
import static java.lang.Math.sin;
import static java.lang.Math.floor;
import java.util.function.DoubleUnaryOperator;

public interface Math2 {

    public static double PI2 = 2*PI;
    public static double PI_OVER8 = PI/8;
    
    public static double sq(double x) {
        return x*x;
    }
    
    public static double floorMod(double x, double y) {
        double quotient = x/y;
        double floQuotient = floor(quotient);
        double remainder = x-floQuotient*y;
        
        return remainder;
    }
    
    public static double haversin(double x) {
        return sq(sin(x/2));
    }
    
    public static double angularDistance(double a1, double a2) {
        return floorMod((a2 - a1 + PI), PI2) - PI;
    }
    
    public static double lerp(double y0, double y1, double x) {
        double slope = y1-y0;
        
        return slope*x + y0;
    }
    
    public static double bilerp(double z00, double z10, double z01, double z11, double x, double y) {
        double lerp1 = lerp(z00, z10, x);
        double lerp2 = lerp(z01, z11, x);
 
        return lerp(lerp1, lerp2, y);
    }
    
    /**
     * return the lower bound of the smallest interval of size dX containing a root of the function f, between maxX and minX
     * @param f the function for which we are seeking a root
     * @param minX the lower bound of interval in which we are seeking
     * @param maxX the upper bound of interval in which we are seeking
     * @param dX the size of the the interval we want
     * @return the lower bound of the interval of size dX containing the root
     */
    public static double firstIntervalContainingRoot(DoubleUnaryOperator f, double minX, double maxX, double dX){
        double currentMinX = minX;
        double currentMaxX = minX + dX;
        
        do{
            if (currentMaxX > maxX){
                currentMaxX = maxX;
            }
            if(f.applyAsDouble(currentMinX) * f.applyAsDouble(currentMaxX) <= 0){
                return currentMaxX - dX;
            }
            
            currentMinX = currentMaxX;
            currentMaxX = currentMaxX + dX;
        }while (currentMinX < maxX);
        
        return Double.POSITIVE_INFINITY;
    }
    
    public static double improveRoot(DoubleUnaryOperator f, double x1, double x2, double epsilon){
        
        Preconditions.checkArgument(f.applyAsDouble(x1)*f.applyAsDouble(x2) <= 0);
        
        if (x1 > x2){
            double temp = x1;
            x1 = x2;
            x2 = temp;
        }
        double middle;
        
        do{
            middle = (x1 + x2)/2;
            if (f.applyAsDouble(middle) * f.applyAsDouble(x1) <= 0){
                x2 = middle;
            }else{
                x1 = middle;
            }
        }while (x2 - x1 > epsilon);
        
        return x1;
    }
    
    
}
