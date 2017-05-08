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
import javafx.collections.FXCollections;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;

public final class PanoramaComputerBean {
    
    private ObjectProperty<PanoramaUserParameters> panoramaUserParamProperty;
    private ObjectProperty<Panorama> panoramaProperty;
    private ObjectProperty<Image> imageProperty;
    private ObjectProperty<ObservableList<Node>> labelsProperty;
    private final ContinuousElevationModel cem; 
    private final List<Summit> summitsList;
    
    /***
     * constructs an instance of drawed panorama parameters
     * @param cem
     * @param summitsList
     */
    public PanoramaComputerBean(ContinuousElevationModel cem, List<Summit> summitsList) {
        
        this.panoramaUserParamProperty = new SimpleObjectProperty<>(null);
        this.panoramaUserParamProperty.addListener((p, o, n) -> update());
        
        this.panoramaProperty = null; 
        this.imageProperty = null;
        this.labelsProperty = null;
        
        this.cem = cem;
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
     * @return ReadOnlyObjectProperty<ObservableList<Node>>
     */
    public ReadOnlyObjectProperty<ObservableList<Node>> labelsProp() {
        return labelsProperty;
    }
    
    /**
     * getter : labelsProperty
     * @return ObservableList<Node>
     */
    public ObservableList<Node> getLabels() {
        return labelsProperty.get();
    }
    
    /**
     * 
     */
    private void update() {
        
        PanoramaComputer newPanoComputer = new PanoramaComputer(cem);
        PanoramaParameters panoramaParameters = panoramaUserParamProperty.get().panoramaParameters();
        
        Labelizer lab = new Labelizer(cem, summitsList);
        List<Node> newList = lab.labels(panoramaParameters);
//        labelsProperty = new SimpleObjectProperty<>(FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(newList)));
        labelsProperty.set(FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(newList)));
        
        Panorama newPano = newPanoComputer.computePanorama(panoramaParameters);
//        panoramaProperty = new SimpleObjectProperty<>(newPano);
        panoramaProperty.set(newPano);
        
//        imageProperty = new SimpleObjectProperty<>(PanoramaRenderer.renderPanorama(imgPainter(newPano), newPano));
        imageProperty.set(PanoramaRenderer.renderPanorama(imgPainter(newPano), newPano));
        
    }
    
    /**
     * 
     * @param p
     * @return
     */
    private ImagePainter imgPainter(Panorama p){
        
        ChannelPainter hue, saturation, brightness, opacity;

        hue = ChannelPainter.distance(p)
                            .div(100000)
                            .cycle()
                            .mul(360);

        saturation = ChannelPainter.distance(p)
                                   .div(200000)
                                   .clamp()
                                   .invert();

        brightness = ChannelPainter.slope(p)
                                   .mul(2)
                                   .div((float) Math.PI)
                                   .invert()
                                   .mul((float) 0.7)
                                   .add((float) 0.3);

        opacity = ChannelPainter.opacity(p);

        return ImagePainter.hsb(hue, saturation, brightness, opacity);
    }
}
