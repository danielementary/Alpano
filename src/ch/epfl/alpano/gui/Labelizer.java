/*
 *	Author:      Samuel Chassot (270955)
 *	Date:        24 avr. 2017
 */


package ch.epfl.alpano.gui;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.summit.Summit;
import javafx.scene.Node;

public class Labelizer {
    ContinuousElevationModel hgt;
    List<Summit> summits;
    
    Labelizer(ContinuousElevationModel hgt, List<Summit> summits){
        this.hgt = hgt;
        this.summits = summits;
    }

    
    public List<Node> labels(PanoramaParameters param){
        
        
        return null;
    }
    
    /**
     * gives the list of the summits who are visible for the observer
     * @param param
     * @return
     */
    private List<Summit> visibleSummits(PanoramaParameters param){
        List<Summit> list = new ArrayList();
        
        for (Summit summit : list) {
            double summitAzimuth = 
        }
    }
}
