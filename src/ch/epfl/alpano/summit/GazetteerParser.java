/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.summit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import ch.epfl.alpano.GeoPoint;

public class GazetteerParser {
    
    //non instantiable class
    private GazetteerParser() { }
    
    /**
     * reads all the summits in file and return an unmodifiable list of summits
     * @param file to read
     * @return an unmodifiable list of summits
     * @throws IOException
     */
    public static List<Summit> readSummitsFrom(File file) throws IOException {
        
        List<Summit> summits = new ArrayList<>();
        
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(Objects.requireNonNull(file))))) {
            
            String line;
            
            while ((line = buffer.readLine()) != null) {
                
                double longitude, latitude;
                String name;
                int elevation;
                
                //read the differents informations about the summit
                longitude = angle(line.substring(0, 9).trim());
                latitude = angle(line.substring(10, 18).trim());
                
                name = toSummit(line);
                
                elevation = Integer.parseInt(line.substring(20,24).trim());
                    
                //instansiating the GeoPoint corresponding to the coordinates
                GeoPoint localisation = new GeoPoint(longitude, latitude);
                
                //instantiating the Summit corresponding to all informations
                Summit summit = new Summit(name, localisation, elevation);
                
                //add the Summit to the list
                summits.add(summit);
            }
            
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            throw new IOException();
        }
        
        //returns an unmodifiable version of the array of Summits
        return Collections.unmodifiableList(summits);
    }
    
    /**
     * @param line
     * @return summit's name of corresponding line
     */
    private static String toSummit(String line) {
        return line.substring(36);
    }
    
    /**
     * converts a string representing an angle into an double degree angle
     * @param string
     * @return double degree angle
     */
    private static double angle(String string) {
        String[] hms = string.split(":");
        
        double degreeAngle = Integer.parseInt(hms[0]);
        
        degreeAngle += Integer.parseInt(hms[1]) / 60.0;
        degreeAngle += Integer.parseInt(hms[2]) / 3600.0;
        
        return Math.toRadians(degreeAngle);
    }
}
