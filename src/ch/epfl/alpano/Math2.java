package ch.epfl.alpano;

import static java.lang.Math.PI;
import static java.lang.Math.sin;
import static java.lang.Math.floor;

public interface Math2 {

    public static double PI2 = 2*PI;
    
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
    
    
}
