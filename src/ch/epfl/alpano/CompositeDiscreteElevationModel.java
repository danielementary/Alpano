/*
 *	Author:      Samuel Chassot (270955)
 *	Date:        27 févr. 2017
 */


package ch.epfl.alpano;

import static java.util.Objects.requireNonNull;

public final class CompositeDiscreteElevationModel implements DiscreteElevationModel {
    
    private final DiscreteElevationModel dem1;
    private final DiscreteElevationModel dem2;
    
    
    CompositeDiscreteElevationModel(DiscreteElevationModel dem1, DiscreteElevationModel dem2){
        
        requireNonNull(dem1);
        requireNonNull(dem2);

        this.dem1 = dem1;
        this.dem2 = dem2;
    }
    
    @Override
    public void close() throws Exception {
        dem1.close();
        dem2.close();

    }

    @Override
    public Interval2D extent() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public double elevationSample(int x, int y) {
        // TODO Auto-generated method stub
        return 0;
    }

}
