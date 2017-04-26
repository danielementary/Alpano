/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.gui;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.Distance;
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
    final int vertLim = 170; //limit of height for summits
    
    Labelizer(ContinuousElevationModel hgt, List<Summit> summits){
        this.hgt = hgt;
        this.summits = summits;
    }

    
    public List<Node> labels(PanoramaParameters param){
        BitSet column = new BitSet(); //false if the column is free, true if she is busy(she has a sign
                                      // or a column in the 20 left or right has one))
        
        List<Node> nodes = new ArrayList<>();
        
        List<Summit> visible = visibleSummits(hgt, param);
        
        visible.sort( (s1, s2) -> {

            int y1 = yForSummit(s1, param);
            int y2 = yForSummit(s2, param);
                        
            return y1 == y2 ? s2.elevation() - s1.elevation() : y1-y2;
        });
        
        for (Summit summit : visible) {
            int x = (int) Math.round(param.xForAzimuth(param.observerPosition().azimuthTo(summit.position())));
            int y = yForSummit(summit, param);
            
            if (y > vertLim){
                if(column.get(x) == false){
                    //create a node, add it to the list and change column
                }
            }
            
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
        List<Summit> visibleSummits = new ArrayList<>();
        int step = 64;
        int tolerance = 200;

        GeoPoint observerPosition = param.observerPosition();

        for (Summit summit : summits) {
            double summitAzimuth = observerPosition.azimuthTo(summit.position());

            if ( Math.abs(Math2.angularDistance(summitAzimuth, param.centerAzimuth())) <=  param.horizontalFieldOfView()/2){

                GeoPoint summitPosition = summit.position();

                double distanceObserverSummit = observerPosition.distanceTo(summitPosition);
                
                if (distanceObserverSummit/1000 <= param.maxDistance() ){


                    ElevationProfile profile = new ElevationProfile(hgt, observerPosition,
                            observerPosition.azimuthTo(summitPosition),
                            distanceObserverSummit);

                    DoubleUnaryOperator distanceFunction = 
                            PanoramaComputer.rayToGroundDistance(profile, 
                                    param.observerElevation(), 
                                    (correctedElevation(summit, param) - param.observerElevation()) / distanceObserverSummit );

                    double lowerBoundRoot = Math2.firstIntervalContainingRoot(
                            distanceFunction,
                            0, distanceObserverSummit, step);


                    if (lowerBoundRoot < Double.POSITIVE_INFINITY ) {
                        //                    
                        double distance = profile.positionAt(lowerBoundRoot).distanceTo(observerPosition); 

                        if (distance >= distanceObserverSummit-tolerance){

                            visibleSummits.add(summit);
                        }
                    }else{

                        visibleSummits.add(summit);
                    }
                }
            }
        }

        return visibleSummits;

    }
    
    private int yForSummit(Summit summit, PanoramaParameters param){
        GeoPoint obsPos = param.observerPosition();
        int obsElev = param.observerElevation();
        
//        if(summit.name().equals("FROMBERGHORE") || summit.name().equals("NIESEN"))
//        {
//            System.out.println(param.yForAltitude(Math.atan2(
//                    (correctedElevation(summit, param)-obsElev),
//                    summit.position().distanceTo(obsPos) )));
//        }
        
        return (int) Math.round(param.yForAltitude(Math.atan2(
                (correctedElevation(summit, param)- obsElev),
                summit.position().distanceTo(obsPos) )) );
    }
    
    /**
     * compute the "new elevation" of a summit by ajusting with earth radius and atmosphere refraction:
     * we throw a ray from the observer position in the direction of the summit but with a angle of 0°
     * it gives us the elevation between a horizontal line which goes from the observer and the top of the summit.
     * Then we have to add this distance to the observer elevation and we obtain a corrected elevation for the summit.
     * @param summit
     * @param param
     * @return
     */
    private double correctedElevation(Summit summit, PanoramaParameters param){
        double dist = param.observerPosition().distanceTo(summit.position());
        
        GeoPoint observerPosition = param.observerPosition();
        
        ElevationProfile profile = new ElevationProfile(hgt, observerPosition,
                observerPosition.azimuthTo(summit.position()),
                dist);
        
        DoubleUnaryOperator distanceFunction = 
                PanoramaComputer.rayToGroundDistance(profile, 
                        param.observerElevation(), 
                       0);
        
        return  param.observerElevation() + Math.abs(distanceFunction.applyAsDouble(dist));
    }
}

