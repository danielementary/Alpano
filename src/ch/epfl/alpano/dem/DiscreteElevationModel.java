/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.dem;

import static ch.epfl.alpano.Preconditions.checkArgument;

import java.util.Objects;

import ch.epfl.alpano.Interval2D;

public interface DiscreteElevationModel extends AutoCloseable {
    
    public static final int SAMPLES_PER_DEGREE = 3600;
    public static final double SAMPLES_PER_RADIAN = SAMPLES_PER_DEGREE * Math.toDegrees(1);
    
    /**
     * gives the index in the MNT of angle
     * @param angle in radian
     * @return the index in double
     */
    public static double sampleIndex(double angle) {
        return SAMPLES_PER_RADIAN * angle;
    }
    
    /**
     * gives the extend of a DEM
     * @return the extend of the DEM
     */
    abstract Interval2D extent();
    
    /**
     * gives you the altitude of the point with coordinate x y
     * @param x longitude
     * @param y latitude
     * @return the altitude in meters
     */ 
    abstract double elevationSample(int x, int y);
    
    /**
     * gives the composite of two MNT this and that 
     * @param that the other MNT
     * @return a compositeElevationModel (union of this and that)
     */
    default DiscreteElevationModel union(DiscreteElevationModel that) {
        checkArgument(this.extent().isUnionableWith(Objects.requireNonNull(that).extent()));
        
        return new CompositeDiscreteElevationModel(this, that);
    }
}
