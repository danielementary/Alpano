/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.Preconditions;

final class CompositeDiscreteElevationModel implements DiscreteElevationModel {
    
    private final DiscreteElevationModel dem1;
    private final DiscreteElevationModel dem2;
    
    CompositeDiscreteElevationModel(DiscreteElevationModel dem1, DiscreteElevationModel dem2) {
        Preconditions.checkArgumentNullPointerEx(dem1);
        Preconditions.checkArgumentNullPointerEx(dem2);
        
        
        
        if (!(dem1.extent().isUnionableWith(dem2.extent()))) {
            throw new IllegalArgumentException();
        }
        
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
        Interval2D inter1 = dem1.extent();
        Interval2D inter2 = dem2.extent();
        
       return inter1.union(inter2);
    }

    @Override
    public double elevationSample(int x, int y) {
        Interval2D inter1 = dem1.extent();
        Interval2D inter2 = dem2.extent();
        
        Preconditions.checkArgument(inter1.contains(x, y) || inter2.contains(x, y));
        
        if (inter1.contains(x, y)) {
            return dem1.elevationSample(x, y);
        }

        return dem2.elevationSample(x, y);   
    }
}
