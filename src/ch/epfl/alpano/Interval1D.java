/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano;

import java.util.Locale;
import java.util.Objects;

public final class Interval1D {
    
    /**
     * lower bound included
     * upperBound included
     */
    private final int includedFrom, includedTo;
    
    /**
     * Creates an instance of an 1 dimension interval
     * @param includedFrom lower bound included
     * @param includedTo upper bound included
     */
    public Interval1D(int includedFrom, int includedTo) {
//        checkArgument(includedTo >= includedFrom);
        
        this.includedFrom = includedFrom;
        this.includedTo = includedTo;
    }
    
    /**
     * gives the lower bound of the instance (includedFrom)
     * @return the interval's lower bound in int
     */
    public final int includedFrom() {
        return includedFrom;
    }
    
    /**
     * gives the upper bound of the instance (includedTo)
     * @return the interval's upper bound in int
     */
    public final int includedTo() {
        return includedTo;
    }
    
    /**
     * gives the number of elements in the interval
     * @return size of interval in int
     */
    public final int size() {
        return includedTo()-includedFrom()+1;
    }
    
    /**
     * gives the size of the intersection of this with that
     * @param that other interval
     * @return the size of intersection
     */
    public final int sizeOfIntersectionWith(Interval1D that) {
        int interFrom = Math.max(this.includedFrom(), that.includedFrom());
        int interTo = Math.min(this.includedTo(), that.includedTo());
        
        int size = interTo-interFrom+1;
        
        return (size > 0) ? size : 0;
    }
    
    /**
     * gives the bounding union of this and that
     * @param that the other interval
     * @return the bounding union (a new interval)
     */
    public final Interval1D boundingUnion(Interval1D that) {
        int interFrom = Math.min(this.includedFrom(), that.includedFrom());
        int interTo = Math.max(this.includedTo(), that.includedTo());
        
        return new Interval1D(interFrom, interTo);
    }
    
    /**
     * says if this is unionable with that
     * @param that other interval
     * @return boolean true if unionable
     */
    public final boolean isUnionableWith(Interval1D that) {
        return this.size()-this.sizeOfIntersectionWith(that)+that.size()
               == this.boundingUnion(that).size();
    }
    
    /**
     * gives the union of two intervals if there are unionable
     * @param that the other interval
     * @return the union
     * @throws IllegalArgumentException
     */
    public final Interval1D union(Interval1D that) {
        return this.boundingUnion(that);
    }
    
    /**
     * redefines how intervals should be compared
     */
    @Override
    public final boolean equals(Object that) {
        return that != null
               && that.getClass() == this.getClass()
               && ((Interval1D)that).includedFrom() == this.includedFrom()
               && ((Interval1D)that).includedTo() == this.includedTo();
    }
    
    /**
     * overriding hashCode() for equals() 
     */
    @Override
    public int hashCode() {
      return Objects.hash(includedFrom(), includedTo());
    }
    
    /**
     * displays the interval like : "[lowerBound .. upperBound]"
     */
    @Override
    public String toString() {
        return String.format((Locale)null, "[%d..%d]", includedFrom(), includedTo());
    }
    
    /**
     * checks if an int is contained in this interval
     * @param that int to check
     * @return true if it is contained false otherwise
     */
    public boolean contains(int that) {
        return (that >= this.includedFrom && that <= this.includedTo);
    }
}
