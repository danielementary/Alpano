package ch.epfl.alpano;

import java.lang.AutoCloseable;

/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

public interface DiscreteElevationModel extends AutoCloseable{
    
    /**
     * gives the extend of a DEM
     * @return the extend of the DEM
     */
    Interval2D extent();
    
    /**
     * gives you the altitude of the point with coordinate x y
     * @param x longitude
     * @param y latitude
     * @return the altitude in meters
     */ 
    double elevationSample(int x, int y);
    
    default DiscreteElevationModel union(DiscreteElevationModel that){
        return new CompositeDiscreteElevationModel(this, that);
    }
}
