/*
 *	Author:      Samuel Chassot (270955)
 *	Date:        24 avr. 2017
 */


package ch.epfl.alpano.gui;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.Distance;
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
        BitSet column = new BitSet(); //false if the column is free, true if she is busy(she has a sign
                                      // or a column in the 20 left or right has one))
        
        
        List<Summit> visible = visibleSummits(hgt, param);
        visible.sort( (s1, s2)-> {
            double y1 = param.yForAltitude(
                    (correctedElevation(s1, param)-param.observerElevation())/
                    s1.position().distanceTo(param.observerPosition()) );
            
            double y2 = param.yForAltitude(
                    (correctedElevation(s2, param)-param.observerElevation())/
                    s2.position().distanceTo(param.observerPosition()) );
            
            int y1_round = (int) Math.round(y1);
            int y2_round = (int) Math.round(y2);
            if(y1_round == y2_round){
                return s1.elevation() - s2.elevation();
                
            }else{
                return y2_round - y1_round;
            }
        });
        
        for (Summit summit : visible) {
            int x = (int) Math.round(param.xForAzimuth(param.observerPosition().azimuthTo(summit.position())));
            int y = (int) Math.round(param.yForAltitude(
                    (correctedElevation(summit, param)-param.observerElevation())/
                    summit.position().distanceTo(param.observerPosition()) ) );
            
            System.out.println(summit.name() + "(" + x + " , " + y + ")");
        }
        
        return null;
    }
    
    /**
     * gives the list of the summits who are visible for the observer
     * @param param
     * @return
     */
    private List<Summit> visibleSummits(ContinuousElevationModel hgt, PanoramaParameters param){
        List<Summit> list_inAzimuth = new ArrayList<>();
        List<Summit> visibleSummits = new ArrayList<>();
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
    
    private double correctedElevation(Summit summit, PanoramaParameters param){
        double elev = summit.elevation();
        double tan = (elev -param.observerElevation())/param.observerPosition().distanceTo(summit.position());
        
        return elev - ((1-0.13)/(2*Distance.EARTH_RADIUS))*Math2.sq(tan);
    }
}
