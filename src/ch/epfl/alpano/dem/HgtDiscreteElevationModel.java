/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.dem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;

import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;

public final class HgtDiscreteElevationModel implements DiscreteElevationModel {

    private ShortBuffer buffer;
    private int latitude, longitude;
    private final Interval2D extent;
    
    /**
     * create a hgtDiscreteElevationModel instance. It opens the HGT file <file> 
     * and throw a IllegalArgumentException if file is not a valid hgt file
     * @param file hgt file
     * @throws IOException
     */
    public HgtDiscreteElevationModel(File file) {
        
        long l = file.length();
        
        String name = file.getName();
        
        try {
            latitude = Integer.parseInt(name.substring(1, 3));
            longitude = Integer.parseInt(name.substring(4, 7));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
        
        if (name.charAt(0) == 'S') {
            latitude *= -1;
        }
        
        if (name.charAt(3) == 'W') {
            longitude *= -1;
        }
        
        try (FileInputStream stream = new FileInputStream(file)) {
            buffer = stream.getChannel().map(MapMode.READ_ONLY, 0, l).asShortBuffer();
        } catch(IOException e) {
          throw new IllegalArgumentException();   
        } 
        
        Interval1D longitudeInterval = new Interval1D(longitude * SAMPLES_PER_DEGREE,
                                                      (longitude+1) * SAMPLES_PER_DEGREE);
        
        Interval1D latitudeInterval = new Interval1D(latitude * SAMPLES_PER_DEGREE,
                                                     (latitude+1) * SAMPLES_PER_DEGREE);
        
        extent = new Interval2D(longitudeInterval, latitudeInterval);
    }
    
    @Override
    public void close() {
        buffer = null;
    }

    @Override
    public Interval2D extent() {
        return extent;
    }

    @Override
    public double elevationSample(int x, int y) {
        int nbrLines = (latitude + 1)*SAMPLES_PER_DEGREE - y;
        int nbrColumns = x - (longitude * 3600);
        
        return buffer.get(nbrLines*(SAMPLES_PER_DEGREE + 1) + nbrColumns);
    }
}
