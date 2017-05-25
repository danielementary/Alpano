/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.dem;

import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import ch.epfl.alpano.Interval2D;

final class CompositeDiscreteElevationModel implements DiscreteElevationModel {
    
    private final DiscreteElevationModel dem1, dem2;
    private final Interval2D union;
    
    public CompositeDiscreteElevationModel(DiscreteElevationModel dem1, DiscreteElevationModel dem2) {
        
        if (!(requireNonNull(dem1).extent().isUnionableWith(requireNonNull(dem2).extent()))) {
            
            throw new IllegalArgumentException();
        } else {
            union = dem1.extent().union(dem2.extent());
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
        return union;
    }

    @Override
    public double elevationSample(int x, int y) {
        Interval2D inter1 = dem1.extent();
        Interval2D inter2 = dem2.extent();
        
        checkArgument(inter1.contains(x, y) || inter2.contains(x, y));
        
        if (inter1.contains(x, y)) {
            return dem1.elevationSample(x, y);
        }

        return dem2.elevationSample(x, y);   
    }
}
