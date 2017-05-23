/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.gui;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
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
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public final class Labelizer {
    
    private final ContinuousElevationModel hgt;
    private final List<Summit> summits;
    
    private final static int vertLim = 170;
    private final static int bound = 20;
    private final static int rotationAng = -60;
    private final static int tolerance = 200;
    
    private final static double step = PanoramaComputer.getStep();
    
    Labelizer(ContinuousElevationModel hgt, List<Summit> summits) {
        
        this.hgt = hgt;
        this.summits = new ArrayList<>(summits);
    }
    
    /**
     * Create a list of Node which are the line and the name
     * of the summits which are visible on the panorama
     * represented by the PanoramaParameters given in argument
     * @param param
     * @return
     */
    public List<Node> labels(PanoramaParameters param) {
        
        int width = param.width();
        
        //false if the column is free, true if it is busy
        BitSet column = new BitSet(width);
        
        for (int i = 0; i < bound; ++i) {
            column.set(i);
        }
        
        for (int i = width-1; i > width-bound-1; --i){
            column.set(i);
        }
        
        List<Node> nodes = new ArrayList<>();
        List<Summit> visible = visibleSummits(hgt, param);
        
        visible.sort((s1, s2) -> {
            int y1 = yForSummit(s1, param);
            int y2 = yForSummit(s2, param);
                        
            return y1 == y2 ? Integer.compare(s2.elevation(), s1.elevation()) : Integer.compare(y1, y2);
        });
        
        int yLabel = -1;
        
        for (Summit summit : visible) {
            
            int x = (int) Math.round(param.xForAzimuth(param.observerPosition().azimuthTo(summit.position())));
            int y = yForSummit(summit, param);
            
            if (y > vertLim) {
                
                if (!column.get(x)) {
                    
                    //create a node, add it to the list and change column
                    if (yLabel < 0) {
                        yLabel = y-22;
                    }
                    
                    //adding the Text Node
                    Text t = new Text(String.format("%s (%d m)", summit.name(), summit.elevation()));
                    
                    t.getTransforms().addAll(new Translate(x, yLabel), new Rotate(rotationAng, 0,0));
                    
                    //adding the Line
                    Line l = new Line(x, y, x, yLabel+2);
                    
                    nodes.add(t);
                    nodes.add(l);
                    
                    //we put the column x to true and the 19 on right and 19 on the left to true too
                    //because these are now full and can't get a label
                    column.set(x);
                    
                    for (int i = 1 ; i <= 19 ; ++i) {
                        column.set(x+i);
                        column.set(x-i);
                    }
                }
            }
        }
        
        return Collections.unmodifiableList(new ArrayList<>(nodes));
    }

    /**
     * gives the list of the summits who are visible for the observer
     * @param param
     * @return
     */
    private List<Summit> visibleSummits(ContinuousElevationModel hgt, PanoramaParameters param) {
        
        List<Summit> visibleSummits = new ArrayList<>();
        GeoPoint observerPosition = param.observerPosition();

        for (Summit summit : summits) {
            
            double summitAzimuth = observerPosition.azimuthTo(summit.position());

            if (Math.abs(Math2.angularDistance(summitAzimuth, param.centerAzimuth()))
                <= param.horizontalFieldOfView()/2) {

                GeoPoint summitPosition = summit.position();
                double distanceObserverSummit = observerPosition.distanceTo(summitPosition);
                
                double tan = elevationDifference(summit, param)/ distanceObserverSummit;
                double angle = Math.atan(tan);
                
                if (distanceObserverSummit <= param.maxDistance()
                    && Math.abs(angle) <= param.verticalFieldOfView()/2) {
                    
                    ElevationProfile profile = new ElevationProfile(hgt, 
                                                                    observerPosition,
                                                                    observerPosition.azimuthTo(summitPosition),
                                                                    distanceObserverSummit);

                    DoubleUnaryOperator distanceFunction = PanoramaComputer.rayToGroundDistance(profile, 
                                                                                                param.observerElevation(), 
                                                                                                tan);

                    double lowerBoundRoot = Math2.firstIntervalContainingRoot(distanceFunction,
                                                                              0,
                                                                              distanceObserverSummit,
                                                                              step);
                    
                    if (!(lowerBoundRoot < Double.POSITIVE_INFINITY)
                        || profile.positionAt(lowerBoundRoot).distanceTo(observerPosition)
                           >= distanceObserverSummit-tolerance) {
                        
                        visibleSummits.add(summit);
                    } 
                }
            }
        }

        return visibleSummits;
    }
    
    /**
     * @param summit
     * @param param
     * @return
     */
    private int yForSummit(Summit summit, PanoramaParameters param){
        
        GeoPoint obsPos = param.observerPosition();
        
        return (int) Math.round(param.yForAltitude(Math.atan2((elevationDifference(summit, param)),
                                                               summit.position().distanceTo(obsPos))));
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
    private double elevationDifference(Summit summit, PanoramaParameters param) {
        
        double dist = param.observerPosition().distanceTo(summit.position());
        GeoPoint observerPosition = param.observerPosition();
        
        ElevationProfile profile = new ElevationProfile(hgt, 
                                                        observerPosition,
                                                        observerPosition.azimuthTo(summit.position()),
                                                        dist);
        
        DoubleUnaryOperator distanceFunction = PanoramaComputer.rayToGroundDistance(profile, 
                                                                                    param.observerElevation(), 
                                                                                    0);
        return  -(distanceFunction.applyAsDouble(dist));
    }
}
