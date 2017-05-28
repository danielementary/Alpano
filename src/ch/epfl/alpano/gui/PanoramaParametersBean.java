/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.gui;

import static javafx.application.Platform.runLater;

import java.util.Objects;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public final class PanoramaParametersBean {
    
    //property for parameters
    private final ObjectProperty<PanoramaUserParameters> parametersProperty;
    
    //properties for each modifiable parameter
    private final ObjectProperty<Integer> observerLongitudeProperty,
                                          observerLatitudeProperty,
                                          observerElevationProperty,
                                          centerAzimuthProperty,
                                          horizontalFieldOfViewProperty,
                                          maxDistanceProperty,
                                          widthProperty,
                                          heightProperty,
                                          superSamplingExponentProperty;
    
    /**
     * constructs an instance of PanoramaParametersBean with parameters pUP
     * and add listeners to properties
     * @param pUP
     */
    public PanoramaParametersBean(PanoramaUserParameters pUP) {
        
        //parametersProperty does not require a listener because it is not modifiable
        parametersProperty = new SimpleObjectProperty<>(Objects.requireNonNull(pUP)); 
        
        //properties and their listeners
        observerLongitudeProperty = new SimpleObjectProperty<>(pUP.getOberserverLong());
        observerLongitudeProperty.addListener((p, o, n) -> runLater(this::synchronizeParameters));
        
        observerLatitudeProperty = new SimpleObjectProperty<>(pUP.getOberserverLati());
        observerLatitudeProperty.addListener((p, o, n) -> runLater(this::synchronizeParameters));
        
        observerElevationProperty = new SimpleObjectProperty<>(pUP.getObserverElev());
        observerElevationProperty.addListener((p, o, n) -> runLater(this::synchronizeParameters));
        
        centerAzimuthProperty = new SimpleObjectProperty<>(pUP.getCenterAzim());
        centerAzimuthProperty.addListener((p, o, n) -> runLater(this::synchronizeParameters));
        
        horizontalFieldOfViewProperty = new SimpleObjectProperty<>(pUP.getHoriFieldOfView());
        horizontalFieldOfViewProperty.addListener((p, o, n) -> runLater(this::synchronizeParameters));
        
        maxDistanceProperty = new SimpleObjectProperty<>(pUP.getMaxDist());
        maxDistanceProperty.addListener((p, o, n) -> runLater(this::synchronizeParameters));
        
        widthProperty = new SimpleObjectProperty<>(pUP.getWidth());
        widthProperty.addListener((p, o, n) -> runLater(this::synchronizeParameters));
        
        heightProperty = new SimpleObjectProperty<>(pUP.getHeight());
        heightProperty.addListener((p, o, n) -> runLater(this::synchronizeParameters));
        
        superSamplingExponentProperty = new SimpleObjectProperty<>(pUP.getSuperSamp());
        superSamplingExponentProperty.addListener((p, o, n) -> runLater(this::synchronizeParameters));
    }
    
    /**
     * getter : parametersProperty
     * @return ReadOnlyObjectProperty<PanoramaUserParameters>
     */
    public ReadOnlyObjectProperty<PanoramaUserParameters> parametersProperty() {
        return parametersProperty;
    }
    
    /**
     * getter : observerLongitudeProperty
     * @return ObjectProperty<Integer>
     */
    public ObjectProperty<Integer> observerLongitudeProperty() {
        return observerLongitudeProperty;
    }
    
    /**
     * getter : observerLatitudeProperty
     * @return ObjectProperty<Integer>
     */
    public ObjectProperty<Integer> observerLatitudeProperty() {
        return observerLatitudeProperty;
    }
    
    /**
     * getter : observerElevationProperty
     * @return ObjectProperty<Integer>
     */
    public ObjectProperty<Integer> observerElevationProperty() {
        return observerElevationProperty;
    }
    
    /**
     * getter : centerAzimuthProperty
     * @return ObjectProperty<Integer>
     */
    public ObjectProperty<Integer> CenterAzimuthProperty() {
        return centerAzimuthProperty;
    }
    
    /**
     * getter : horizontalFieldOfViewProperty
     * @return ObjectProperty<Integer>
     */
    public ObjectProperty<Integer> horizontalFieldOfViewProperty() {
        return horizontalFieldOfViewProperty;
    }
    
    /**
     * getter : maxDistanceProperty
     * @return ObjectProperty<Integer>
     */
    public ObjectProperty<Integer> maxDistanceProperty() {
        return maxDistanceProperty;
    }
    
    /**
     * getter : widthProperty
     * @return ObjectProperty<Integer>
     */
    public ObjectProperty<Integer> widthProperty() {
        return widthProperty;
    }
    
    /**
     * getter : heightProperty
     * @return ObjectProperty<Integer>
     */
    public ObjectProperty<Integer> heightProperty() {
        return heightProperty;
    }
    
    /**
     * getter : superSamplingExponentProperty
     * @return ObjectProperty<Integer>
     */
    public ObjectProperty<Integer> SuperSamplingExponentProperty() {
        return superSamplingExponentProperty;
    }
    
    /**
     * synchronize all parameters when there is some changes
     */
    private void synchronizeParameters() {
        
        //create a new PanoramaUserParameters
        PanoramaUserParameters nPUP = 
                new PanoramaUserParameters(observerLongitudeProperty.get(),
                                           observerLatitudeProperty.get(),
                                           observerElevationProperty.get(),
                                           centerAzimuthProperty.get(),
                                           horizontalFieldOfViewProperty.get(),
                                           maxDistanceProperty.get(),
                                           widthProperty.get(),
                                           heightProperty.get(),
                                           superSamplingExponentProperty.get());
        
        parametersProperty.set(nPUP);
        
        //update all properties
        observerLongitudeProperty.set(nPUP.getOberserverLong());
        observerLatitudeProperty.set(nPUP.getOberserverLati());
        observerElevationProperty.set(nPUP.getObserverElev());
        centerAzimuthProperty.set(nPUP.getCenterAzim());
        horizontalFieldOfViewProperty.set(nPUP.getHoriFieldOfView());
        maxDistanceProperty.set(nPUP.getMaxDist());
        widthProperty.set(nPUP.getWidth());
        heightProperty.set(nPUP.getHeight());
        superSamplingExponentProperty.set(nPUP.getSuperSamp());
    }  
}
