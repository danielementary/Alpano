/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.gui;

import static javafx.collections.FXCollections.observableArrayList;
import static javafx.collections.FXCollections.unmodifiableObservableList;

import java.awt.Image;
import java.util.List;

import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.dem.ContinuousElevationModel;
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
    private final ContinuousElevationModel cem;
    
    public PanoramaComputerBean(ContinuousElevationModel cem, List<Node> summitsList) {
        panoramaUserParamProp = null;
        panoramaProp = null; 
        imageProp = null;
        
        this.cem = cem;
        labelsProp = new SimpleObjectProperty<>(unmodifiableObservableList(observableArrayList(summitsList)));
        
        panoramaUserParamProp.addListener((p, o, n) -> update());
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
    
    public ReadOnlyObjectProperty<Image> imageProp() {
        return imageProp;
    }
    
    public Image getImage() {
        return imageProp.get();
    }
    
    public ReadOnlyObjectProperty<ObservableList<Node>> labelsProp() {
        return labelsProp;
    }
    
    public ObservableList<Node> getLabels() {
        return labelsProp.get();
    }
    
    private void update() {
        System.out.println("update sa mère");
    }
}
