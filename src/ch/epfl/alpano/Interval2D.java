/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano;

import static ch.epfl.alpano.Preconditions.checkArgument;

import java.util.Objects;

public final class Interval2D {
    
    /**
     * first interval
     * second interval
     */
    private final Interval1D iX, iY;
    
    /**
     * Creates an instance of a bidimensional interval
     * with two unidimensional intervals
     * @param iX first unidimensional interval
     * @param iY second unidimensional interval
     */
    public Interval2D(Interval1D iX, Interval1D iY) {
        
        this.iX = Objects.requireNonNull(iX);
        this.iY = Objects.requireNonNull(iY);
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
        return iX.contains(x)
               && iY.contains(y);
    }
    
    /**
     * size of cartesian product of iX and iY
     * @return int size of instance's bidimensional interval
     */
    public final int size() {
        return iX.size()
               *iY.size();
    }
    
    /**
     * calculates the size of intersection between this and that
     * @param that other bidimensional interval
     * @return int size of intersection between this and that
     */
    public final int sizeOfIntersectionWith(Interval2D that) {
        return iX.sizeOfIntersectionWith(that.iX())
               *iY.sizeOfIntersectionWith(that.iY());
    }
    
    /**
     * calculates a bidimensional interval with boundingUnions 
     * of this and that
     * @param that
     * @return an Interval2D whose intervals are boundingUnions 
     * with that's intervals
     */
    public final Interval2D boundingUnion(Interval2D that) {
        return new Interval2D(iX.boundingUnion(that.iX()),
                              iY.boundingUnion(that.iY()));
    }
    
    /**
     * if the cartesian product this and that are unionable -> true
     * @param that bidimensional interval to compare
     * @return boolean true if this and that are unionable, false otherwise
     */
    public final boolean isUnionableWith(Interval2D that) {
        return this.size()-this.sizeOfIntersectionWith(that)+that.size()
               == this.boundingUnion(that).size();
    }
    
    /**
     * return the union of this and that or raise an exception
     * if they are not unionable
     * @param that Interval2D for union with this
     * @return an Interval2D whichh represents the union 
     * of bidimensional interval this and that
     */
    public final Interval2D union(Interval2D that) {
//        checkArgument(this.isUnionableWith(that));
        
        return new Interval2D(iX.union(that.iX()), iY.union(that.iY()));
    }
    
    /**
     * redefines how bidimensional intervals should be compared
     */
    @Override
    public boolean equals(Object thatO) {
        return thatO != null
               && this.getClass() == thatO.getClass()
               && this.iX().equals(((Interval2D)thatO).iX())
               && this.iY().equals(((Interval2D)thatO).iY());
    }
    
    /**
     * overriding hashCode() for equals() 
     */
    @Override
    public int hashCode() {
      return Objects.hash(iX(), iY());
    }
    
    /**
     * displays the instance like 
     * "[lowerBound1 .. upperBound1]x[lowerbound2 .. upperbound2]"
     */
    @Override
    public String toString() {
        return iX().toString() + "x" + iY().toString();
    }
    
}
