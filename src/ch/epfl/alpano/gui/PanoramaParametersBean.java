/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.gui;

import javafx.beans.property.*;

public final class PanoramaParametersBean {
    private ReadOnlyObjectProperty<PanoramaUserParameters> parametersProp;
    private ObjectProperty<Integer> observerLongitudeProp;
    private ObjectProperty<Integer> observerLatitudeProp;
    private ObjectProperty<Integer> observerElevationProp;
    private ObjectProperty<Integer> centerAzimuthProp;
    private ObjectProperty<Integer> horizontalFieldOfViewProp;
    private ObjectProperty<Integer> maxDistanceProp;
    private ObjectProperty<Integer> widthProp;
    private ObjectProperty<Integer> heightProp;
    private ObjectProperty<Integer> superSamplingExponentProp;
    
    public PanoramaParametersBean() {
        parametersProp = new SimpleObjectProperty<>(); 
        observerLongitudeProp = new SimpleObjectProperty<>();
        observerLatitudeProp = new SimpleObjectProperty<>();
        observerElevationProp = new SimpleObjectProperty<>();
        centerAzimuthProp = new SimpleObjectProperty<>();
        horizontalFieldOfViewProp = new SimpleObjectProperty<>();
        maxDistanceProp = new SimpleObjectProperty<>();
        widthProp = new SimpleObjectProperty<>();
        heightProp = new SimpleObjectProperty<>();
        superSamplingExponentProp = new SimpleObjectProperty<>();
    }
    
    public ReadOnlyObjectProperty<PanoramaUserParameters> ParametersProp() {
        return parametersProp;
    }
    
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

    public void setObserverLongitudeProp(Integer observerLongitudeProp) {
        this.observerLongitudeProp.set(observerLongitudeProp);
    }

    public void setObserverLatitudeProp(int observerLatitudeProp) {
        this.observerLatitudeProp.set(observerLatitudeProp);
    }

    public void setObserverElevationProp(int observerElevationProp) {
        this.observerElevationProp.set(observerElevationProp);
    }

    public void setCenterAzimuthProp(int centerAzimuthProp) {
        this.centerAzimuthProp.set(centerAzimuthProp);
    }

    public void setHorizontalFieldOfViewProp(int horizontalFieldOfViewProp) {
        this.horizontalFieldOfViewProp.set(horizontalFieldOfViewProp);
    }

    public void setMaxDistanceProp(int maxDistanceProp) {
        this.maxDistanceProp.set(maxDistanceProp);
    }

    public void setWidthProp(int widthProp) {
        this.widthProp.set(widthProp);
    }

    public void setHeightProp(int heightProp) {
        this.heightProp.set(heightProp);
    }

    public void setSuperSamplingExponentProp(int superSamplingExponentProp) {
        this.superSamplingExponentProp.set(superSamplingExponentProp);
    }
    
    private void synchronizeParameters() {
        
    }  
}
