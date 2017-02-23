package ch.epfl.alpano;

/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

public final class Interval2D {
    private Interval1D iX;
    private Interval1D iY;
    
    /**
     * Creates an instance of a bidimensional interval with two unidimensional intervals
     * @param iX first unidimensional interval
     * @param iY second unidimensional interval
     */
    public Interval2D(Interval1D iX, Interval1D iY) {
        Preconditions.checkArgument(iX != null);
        Preconditions.checkArgument(iY != null);
        
        this.iX = iX;
        this.iY = iY;
    }
    
    /**
     * getter for first unidimensional interval
     * @return Interval1D iX
     */
    public final Interval1D iX() {
        return iX;
    }
    
    /**
     * getter for second unidimensional interval
     * @return Interval1D iY
     */
    public final Interval1D iY() {
        return iY;
    }
    /**
     * informs if the cartesian product of iX and iY contains (x,y)
     * @param x of (x,y)
     * @param y of (x,y)
     * @return true if (x,y) is in iX x iY
     */
    public final boolean contains(int x, int y) {
        if (iX.contains(x) && iY.contains(y)) {
            return true;
        } else {
            return false;
        }
    }

}
