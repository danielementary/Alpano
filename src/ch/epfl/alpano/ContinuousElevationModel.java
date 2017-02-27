/*
 *	Author:      Samuel Chassot (270955)
 *	Date:        27 févr. 2017
 */


package ch.epfl.alpano;

import static java.util.Objects.requireNonNull;

public final class ContinuousElevationModel {
    
    private final DiscreteElevationModel dem;
    
    ContinuousElevationModel(DiscreteElevationModel dem){
        requireNonNull(dem);
        
        this.dem = dem;
        
    }
}
