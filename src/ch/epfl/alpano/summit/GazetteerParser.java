package ch.epfl.alpano.summit;

/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.alpano.GeoPoint;

public class GazetteerParser {

    private GazetteerParser(){
        
    }
    
    public static List<Summit> readSummitsFrom(File file) throws IOException{
        
        List<Summit> summits =new ArrayList<Summit>();
        
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file)))){
            String line;
            while ( (line = buffer.readLine()) != null){
                double longitude = angle(line.substring(0, 9).trim());
                double latitude = angle(line.substring(10, 18).trim());
                String name = toSummit(line);
               
                int elevation = Integer.parseInt(line.substring(20,24).trim());
                GeoPoint localisation = new GeoPoint(longitude, latitude);
                
                Summit summit = new Summit(name, localisation, elevation);
                
                summits.add(summit);
            }
        }
        
        return Collections.unmodifiableList(summits);
    }
    
    private static String toSummit(String line){
        return line.substring(36);
    }
    
    private static double angle(String string){
        String[] hms = string.split(":");
        double degreeAngle = Integer.parseInt(hms[0]);
        degreeAngle += Integer.parseInt(hms[1]) / 60.0;
        degreeAngle += Integer.parseInt(hms[2]) / 3600.0;
        
        return Math.toRadians(degreeAngle);
    }
}
