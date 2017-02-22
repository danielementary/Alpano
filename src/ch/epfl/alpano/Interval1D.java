package ch.epfl.alpano;

/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

public final class Interval1D {
    
    /**
     * lower bound included
     */
    private final int includedFrom;
    /**
     * upperBound included
     */
    private final int includedTo;
    
    /**
     * Creates an instance of an interval
     * @param includedFrom lower bound
     * @param includedTo upper bound
     */
    public Interval1D(int includedFrom, int includedTo) {
        Preconditions.checkArgument(includedTo >= includedFrom);
        
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
        return includedTo-includedFrom+1;
    }
    
    public final int sizeOfIntersectionWith(Interval1D that) {
        int thisFrom = this.includedFrom();
        int thisTo = this.includedTo();
        int thatFrom = that.includedFrom();
        int thatTo = that.includedTo();
        
        //à compléter
        
        return 0;
    }
}
