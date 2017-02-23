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
     * Creates an instance of a bidimensional interval
     * with two unidimensional intervals
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
    
    /**
     * size of cartesian product of iX and iY
     * @return int size of instance's bidimensional interval
     */
    public final int size() {
        return iX.size()*iY.size();
    }
    
    /**
     * calculates the size of intersection between this and that
     * @param that other bidimensional interval
     * @return int size of intersection between this and that
     */
    public final int sizeOfIntersectionWith(Interval2D that) {
        int sizeOfIntersectionWithX = iX.sizeOfIntersectionWith(that.iX());
        int sizeOfIntersectionWithY = iY.sizeOfIntersectionWith(that.iY());
        
        return sizeOfIntersectionWithX*sizeOfIntersectionWithY;
    }
    
    /**
     * calculates a bidimensional interval with boundingUnions 
     * of this and that
     * @param that
     * @return an Interval2D whose intervals are boundingUnions 
     * with that's intervals
     */
    public final Interval2D boundingUnion(Interval2D that) {
        Interval1D interval1 = iX.boundingUnion(that.iX());
        Interval1D interval2 = iY.boundingUnion(that.iY());
        
        return new Interval2D(interval1, interval2);
    }
    
    /**
     * if the cartesian product this and that are unionable -> true
     * @param that bidimensional interval to compare
     * @return boolean true if this and that are unionable, false otherwise
     */
    public final boolean isUnionableWith(Interval2D that) {
        if (iX.isUnionableWith(that.iX()) && iY.isUnionableWith(that.iY())) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * return the union of this and that or raise an exception
     * if they are not unionable
     * @param that Interval2D for union with this
     * @return an Interval2D whichh represents the union 
     * of bidimensional interval this and that
     */
    public final Interval2D union(Interval2D that) {
        Preconditions.checkArgument(iX.isUnionableWith(that.iX()));
        Preconditions.checkArgument(iY.isUnionableWith(that.iY()));
        
        Interval1D interval1 = iX.union(that.iX());
        Interval1D interval2 = iY.union(that.iY());
        
        return new Interval2D(interval1, interval2);
    }
    
}
