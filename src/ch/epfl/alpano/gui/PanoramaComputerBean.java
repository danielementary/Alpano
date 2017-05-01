/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.gui;

import java.awt.Image;

import ch.epfl.alpano.Panorama;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;

public final class PanoramaComputerBean {
    private ObjectProperty<PanoramaUserParameters> panoramaUserParamProp;
    private ReadOnlyObjectProperty<Panorama> panoramaProp;
    private ReadOnlyObjectProperty<Image> imageProp;
    private ReadOnlyObjectProperty<ObservableList<Node>> labelsProp;
    
    public PanoramaComputerBean() {
        panoramaUserParamProp = new SimpleObjectProperty<>(); 
        panoramaProp = new SimpleObjectProperty<>(); 
        imageProp = new SimpleObjectProperty<>(); 
        labelsProp = new SimpleObjectProperty<>(); 
    }
    
    public ObjectProperty<PanoramaUserParameters> parametersProp() {
        return panoramaUserParamProp;
    }
    
    public PanoramaUserParameters getParameters() {
        return panoramaUserParamProp.get();
    }
    
    public void setParameters(PanoramaUserParameters param) {
        panoramaUserParamProp.set(param);
    }
    
    public ReadOnlyObjectProperty<Panorama> panoramaProp() {
        return panoramaProp;
    }
    
    public Panorama getPanorama() {
        return panoramaProp.get();
    }
    
    public ReadOnlyObjectProperty<ObservableList<Node>> labelsProp() {
        return labelsProp;
    }
    
    public ObservableList<Node> getLabels() {
        return labelsProp.get();
    }
}
