/*
 *	Author:      Samuel Chassot (270955)
 *	Date:        24 avr. 2017
 */


package ch.epfl.alpano.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
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
    private List<Summit> visibleSummits(ContinuousElevationModel hgt, PanoramaParameters param){
        List<Summit> list_inAzimuth = new ArrayList();
        List<Summit> visibleSummits = new ArrayList();
        int step = 64;
        int delta = 4;
        int tolerance = 200;
        
        for (Summit summit : summits) {
            double summitAzimuth = summit.position().azimuthTo(param.observerPosition());
            
            if (Math2.angularDistance(summitAzimuth, param.centerAzimuth()) <= param.horizontalFieldOfView()/2 ){
                list_inAzimuth.add(summit);
            }
        }
        
        for (Summit summit : list_inAzimuth) {
            double distanceSummitObserver = summit.position().distanceTo(param.observerPosition());
            
            ElevationProfile profile = new ElevationProfile(hgt, param.observerPosition(),
                    summit.position().azimuthTo(param.observerPosition()),
                    distanceSummitObserver);
            
            DoubleUnaryOperator distanceFunction = 
                    PanoramaComputer.rayToGroundDistance(profile, 
                            param.observerElevation(), 
                            (summit.elevation() - param.observerElevation()) / distanceSummitObserver );
            
            double lowerBoundRoot = Math2.firstIntervalContainingRoot(
                    distanceFunction,
                            0, distanceSummitObserver, step);
            
            double root;
            
            if (lowerBoundRoot < Double.POSITIVE_INFINITY) {
                root = Math2.improveRoot(distanceFunction,
                             lowerBoundRoot, lowerBoundRoot + step, delta);
            }else{
                root = lowerBoundRoot;
            }
            
            double distance = profile.positionAt(root).distanceTo(param.observerPosition());
            
            if (distance >= distanceSummitObserver-tolerance){
                visibleSummits.add(summit);
            }
        } 
        
        return visibleSummits;
        
        
        
    }
}
