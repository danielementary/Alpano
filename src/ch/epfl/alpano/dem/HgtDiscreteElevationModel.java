


package ch.epfl.alpano.dem;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;

import ch.epfl.alpano.Preconditions;
import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;
import static ch.epfl.alpano.dem.DiscreteElevationModel.SAMPLES_PER_DEGREE;

/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

public final class HgtDiscreteElevationModel implements DiscreteElevationModel {

    private File file;
    private FileInputStream stream;
    private ShortBuffer buffer;
    private int latitude;
    private int longitude;
    
    HgtDiscreteElevationModel(File file) throws IOException{
        this.file = file;
        long l = file.length();
        Preconditions.checkArgument(l == 25934402 );
        
        String name = file.getName();
        Preconditions.checkArgument(name.charAt(0) == 'N' || name.charAt(0) == 'S');
        Preconditions.checkArgument(name.charAt(3) == 'W' || name.charAt(3) == 'E');
        
        try{
        latitude = Integer.parseInt(name.substring(1, 3));
        longitude = Integer.parseInt(name.substring(4, 8));
        }
        catch (NumberFormatException e){
            throw new IllegalArgumentException();
        }
        
        Preconditions.checkArgument(latitude >= 0 && latitude <= 90);
        Preconditions.checkArgument(longitude >= 0 && longitude <= 180);
        
        if (name.charAt(0) == 's'){
            latitude *= -1;
        }
        if (name.charAt(3) == 'W'){
            longitude *= -1;
        }
        Preconditions.checkArgument(name.substring(7) == ".hgt");
        
        try{
            stream = new FileInputStream(file);            
        }catch(IOException e){
            
        }
        
        try{
            buffer = stream.getChannel().map(MapMode.READ_ONLY, 0, l).asShortBuffer();
        }catch(IOException e){
            
        }
    }
    @Override
    public void close() throws Exception {
        stream.close();

    }

    @Override
    public Interval2D extent() {
        Interval1D longitudeInterval = new Interval1D(longitude * SAMPLES_PER_DEGREE, (longitude+1) * SAMPLES_PER_DEGREE);
        Interval1D latitudeInterval = new Interval1D(latitude * SAMPLES_PER_DEGREE, (latitude+1) * SAMPLES_PER_DEGREE);
        return null;
    }

    @Override
    public double elevationSample(int x, int y) {
//        int index = SAMPLES_PER_DEGREE*y + x;
//        return buffer.get(index);
        return 0;
    }

}