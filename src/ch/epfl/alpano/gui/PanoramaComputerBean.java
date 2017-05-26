/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.gui;

import java.util.List;
import java.util.Objects;

import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.summit.Summit;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;

public final class PanoramaComputerBean {
    
    private final ObjectProperty<PanoramaUserParameters> panoramaUserParamProperty;
    private final ObjectProperty<Panorama> panoramaProperty;
    private final ObjectProperty<Image> imageProperty;
    
    private final ObservableList<Node> labelsList;
    private final ObservableList<Node> labels;
    
    private final ContinuousElevationModel cem; 
    private final List<Summit> summitsList;
    
    /***
     * constructs an instance of drawed panorama parameters
     * @param cem
     * @param summitsList
     */
    public PanoramaComputerBean(ContinuousElevationModel cem, List<Summit> summitsList) {
        
        this.panoramaUserParamProperty = new SimpleObjectProperty<>();
        this.panoramaUserParamProperty.addListener((p, o, n) -> update());
        
        this.panoramaProperty = new SimpleObjectProperty<>(); 
        this.imageProperty = new SimpleObjectProperty<>();
        
        this.labels = FXCollections.observableArrayList();
        this.labelsList = FXCollections.unmodifiableObservableList(labels);
        
        this.cem = Objects.requireNonNull(cem);
        this.summitsList = Objects.requireNonNull(summitsList);
    }

    /**
     * getter : panoramaUserParamProperty
     * @return ObjectProperty<PanoramaUserParameters>
     */
    public ObjectProperty<PanoramaUserParameters> parametersProp() {
        return panoramaUserParamProperty;
    }

    /**
     * getter : panoramaUserParamProperty
     * @return PanoramaUserParameters
     */
    public PanoramaUserParameters getParameters() {
        return panoramaUserParamProperty.get();
    }
    
    /**
     * set param as new value for panoramaUserParamProperty
     * @param param
     */
    public void setParameters(PanoramaUserParameters param) {
        panoramaUserParamProperty.set(param);
    }
    
    /**
     * getter : panoramaProperty
     * @return ReadOnlyObjectProperty<Panorama>
     */
    public ReadOnlyObjectProperty<Panorama> panoramaProp() {
        return panoramaProperty;
    }
    
    /**
     * getter : PanoramaProperty
     * @return Panorama
     */
    public Panorama getPanorama() {
        return panoramaProperty.get();
    }
    
    /**
     * getter : imageProperty
     * @return ReadOnlyObjectProperty<Image>
     */
    public ReadOnlyObjectProperty<Image> imageProp() {
        return imageProperty;
    }
    
    /**
     * getter : imageProperty
     * @return Image
     */
    public Image getImage() {
        return imageProperty.get();
    }
    
    
    /**
     * getter : labelsProperty
     * @return ObservableList<Node>
     */
    public ObservableList<Node> getLabels() {
        return labelsList;
    }
    
    /**
     * update the properties
     */
    private void update() {
        
        PanoramaComputer newPanoComputer = new PanoramaComputer(cem);
        PanoramaParameters panoramaParameters = panoramaUserParamProperty.get().panoramaParameters();
        
        Panorama newPano = newPanoComputer.computePanorama(panoramaParameters);
        panoramaProperty.set(newPano);
        
        imageProperty.set(PanoramaRenderer.renderPanorama(ImagePainter.stdPainter(newPano), newPano));
        
        Labelizer lab = new Labelizer(cem, summitsList);
        List<Node> newList = lab.labels(panoramaUserParamProperty.get().panoramaDisplayParameters());
        labels.setAll(newList);
    }
}
