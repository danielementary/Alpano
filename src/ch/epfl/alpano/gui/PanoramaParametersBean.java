/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.gui;

import ch.epfl.alpano.PanoramaParameters;
import javafx.beans.property.*;
import static javafx.application.Platform.runLater;

public final class PanoramaParametersBean {
    private ReadOnlyObjectProperty parametersProp;
    private ObjectProperty<Integer> observerLongitudeProp;
    private ObjectProperty<Integer> observerLatitudeProp;
    private ObjectProperty<Integer> observerElevationProp;
    private ObjectProperty<Integer> centerAzimuthProp;
    private ObjectProperty<Integer> horizontalFieldOfViewProp;
    private ObjectProperty<Integer> maxDistanceProp;
    private ObjectProperty<Integer> widthProp;
    private ObjectProperty<Integer> heightProp;
    private ObjectProperty<Integer> superSamplingExponentProp;
    
    public PanoramaParametersBean(PanoramaUserParameters pUP) {
        parametersProp = new SimpleObjectProperty<>(pUP); 
        
        observerLongitudeProp = new SimpleObjectProperty<>(pUP.getOberserverLong());
        observerLongitudeProp.addListener((p, o, n) -> runLater(this::synchronizeParameters));
        
        observerLatitudeProp = new SimpleObjectProperty<>(pUP.getOberserverLati());
        observerLatitudeProp.addListener((p, o, n) -> runLater(this::synchronizeParameters));
        
        observerElevationProp = new SimpleObjectProperty<>(pUP.getObserverElev());
        observerElevationProp.addListener((p, o, n) -> runLater(this::synchronizeParameters));
        
        centerAzimuthProp = new SimpleObjectProperty<>(pUP.getCenterAzim());
        centerAzimuthProp.addListener((p, o, n) -> runLater(this::synchronizeParameters));
        
        horizontalFieldOfViewProp = new SimpleObjectProperty<>(pUP.getHoriFieldOfView());
        horizontalFieldOfViewProp.addListener((p, o, n) -> runLater(this::synchronizeParameters));
        
        maxDistanceProp = new SimpleObjectProperty<>(pUP.getMaxDist());
        maxDistanceProp.addListener((p, o, n) -> runLater(this::synchronizeParameters));
        
        widthProp = new SimpleObjectProperty<>(pUP.getWidth());
        widthProp.addListener((p, o, n) -> runLater(this::synchronizeParameters));
        
        heightProp = new SimpleObjectProperty<>(pUP.getHeight());
        heightProp.addListener((p, o, n) -> runLater(this::synchronizeParameters));
        
        superSamplingExponentProp = new SimpleObjectProperty<>(pUP.getSuperSamp());
        superSamplingExponentProp.addListener((p, o, n) -> runLater(this::synchronizeParameters));
    }
    
    /**
     * getter : ReadOnlyObjectProperty<PanoramaUserParameters> ParametersProp()
     * @return ParametersProp
     */
    public ReadOnlyObjectProperty<PanoramaUserParameters> ParametersProp() {
        return parametersProp;
    }
    
    /**
     * 
     * @return
     */
    public ObjectProperty<Integer> ObserverLongitudeProp() {
        return observerLongitudeProp;
    }
    
    public ObjectProperty<Integer> ObserverLatitudeProp() {
        return observerLatitudeProp;
    }
    
    public ObjectProperty<Integer> ObserverElevationProp() {
        return observerElevationProp;
    }
    
    public ObjectProperty<Integer> CenterAzimuthProp() {
        return centerAzimuthProp;
    }
    
    public ObjectProperty<Integer> HorizontalFieldOfViewProp() {
        return horizontalFieldOfViewProp;
    }
    
    public ObjectProperty<Integer> MaxDistanceProp() {
        return maxDistanceProp;
    }
    
    public ObjectProperty<Integer> WidthProp() {
        return widthProp;
    }
    
    public ObjectProperty<Integer> HeightProp() {
        return heightProp;
    }
    
    public ObjectProperty<Integer> SuperSamplingExponentProp() {
        return superSamplingExponentProp;
    }
    
    private void synchronizeParameters() {
        PanoramaUserParameters nPUP = new PanoramaUserParameters(observerLongitudeProp.get(),
                                                                 observerLatitudeProp.get(),
                                                                 observerElevationProp.get(),
                                                                 centerAzimuthProp.get(),
                                                                 horizontalFieldOfViewProp.get(),
                                                                 maxDistanceProp.get(),
                                                                 widthProp.get(),
                                                                 heightProp.get(),
                                                                 superSamplingExponentProp.get());
        parametersProp = new SimpleObjectProperty<>(nPUP); 
        
        observerLongitudeProp.set(nPUP.getOberserverLong());
        observerLatitudeProp.set(nPUP.getOberserverLati());
        observerElevationProp.set(nPUP.getObserverElev());
        centerAzimuthProp.set(nPUP.getCenterAzim());
        horizontalFieldOfViewProp.set(nPUP.getHoriFieldOfView());
        maxDistanceProp.set(nPUP.getMaxDist());
        widthProp.set(nPUP.getWidth());
        heightProp.set(nPUP.getHeight());
        superSamplingExponentProp.set(nPUP.getSuperSamp());
    }  
}
