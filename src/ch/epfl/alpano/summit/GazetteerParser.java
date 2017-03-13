/*
 *	Author:      Samuel Chassot (270955)
 *	Date:        13 mars 2017
 */


package ch.epfl.alpano.summit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.util.List;

public class GazetteerParser {

    private GazetteerParser(){
        
    }
    
    public static List<Summit> readSummitsFrom(File file) throws IOException{
        
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file)))){
            String line;
            while ( (line = buffer.readLine()) != null){
                
            }
        }
    }
    
    private String toSummit(String line){
        return line.substring(36);
    }
    
    private double angle(String string){
        String[] hms = string.split(":");
        double degreeAngle = Integer.parseInt(hms[0]);
        degreeAngle += Integer.parseInt(hms[1]) * 60;
        degreeAngle += Integer.parseInt(hms[2]) * 3600;
        
        return Math.toRadians(degreeAngle);
    }
}
