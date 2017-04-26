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

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import ch.epfl.alpano.summit.Summit;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.scene.shape.Line;

public class Labelizer {
    
    ContinuousElevationModel hgt;
    List<Summit> summits;
    final int vertLim = 170; //limit of height for summits
    final int bound = 20;
    
    Labelizer(ContinuousElevationModel hgt, List<Summit> summits){
        this.hgt = hgt;
        this.summits = summits;
    }

    
    public List<Node> labels(PanoramaParameters param){
        int width = param.width();
        BitSet column = new BitSet(width); //false if the column is free, true if she is busy(she has a sign
                                      // or a column in the 20 left or right has one))
        
        for(int i = 0 ; i < bound ; ++i){
            column.set(i);
        }
        for (int i = width-1 ; i > width - 1 - bound ; --i){
            column.set(i);
        }
        
        List<Node> nodes = new ArrayList<>();
        
        List<Summit> visible = visibleSummits(hgt, param);
        
        visible.sort( (s1, s2) -> {

            int y1 = yForSummit(s1, param);
            int y2 = yForSummit(s2, param);
                        
            return y1 == y2 ? s2.elevation() - s1.elevation() : y1-y2;
        });
        
        int yLabel = yForSummit(visible.get(0), param) - 22;
        double angleForTextLabel = 60; //degrees
        
        for (Summit summit : visible) {
            int x = (int) Math.round(param.xForAzimuth(param.observerPosition().azimuthTo(summit.position())));
            int y = yForSummit(summit, param);
            
            if (y > vertLim){
                if(column.get(x) == false){
                    //create a node, add it to the list and change column
                    
                    //adding the Text Node
                    String str = summit.name() + " (" + summit.elevation() + " m)";
                    Text t = new Text(x, y, str);
                    t.getTransforms().addAll(new Rotate(angleForTextLabel), new Translate(0, (y-yLabel)));
                    
                    //adding the Line
                    Line l = new Line();
                    l.setStartX(x);
                    l.setEndX(x);
                    l.setStartY(y-2);
                    l.setEndY(yLabel+2);
                    
                    nodes.add(l);
                    nodes.add(t);
                    
                    //we put the column x to true and the 19 on right and 19 on the left to true too
                    //because these are now full and can't get a label
                    column.set(x);
                    for (int i = 1 ; i <= 19 ; ++i){
                        column.set(x + i);
                        column.set(x - i);
                    }
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
        
        return  param.observerElevation() -(distanceFunction.applyAsDouble(dist));
    }
}

