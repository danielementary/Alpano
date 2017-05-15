/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.gui;

import java.util.ArrayList;
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
import javafx.application.Platform;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.property.SimpleBooleanProperty;




public final class PanoramaComputerBean {
    
    private ObjectProperty<PanoramaUserParameters> panoramaUserParamProperty;
    private ObjectProperty<Panorama> panoramaProperty;
    private ObjectProperty<Image> imageProperty;
    private ObjectProperty<ObservableList<Node>> labelsProperty;
    private final ContinuousElevationModel cem; 
    private final List<Summit> summitsList;
    private final PanoramaComputer panoComp;
    private final Labelizer labelizer;
    private SimpleBooleanProperty computeInProg;
    
    /***
     * constructs an instance of drawed panorama parameters
     * @param cem
     * @param summitsList
     */
    public PanoramaComputerBean(ContinuousElevationModel cem, List<Summit> summitsList) {
        
        this.panoramaUserParamProperty = new SimpleObjectProperty<>(null);
        this.panoramaUserParamProperty.addListener((p, o, n) -> update());
        
        this.computeInProg = new SimpleBooleanProperty(false);
        
        this.panoramaProperty = new SimpleObjectProperty<>(); 
        this.imageProperty = new SimpleObjectProperty<>();
        this.labelsProperty = new SimpleObjectProperty<>(FXCollections.observableArrayList());
        
        this.cem = cem;
        this.summitsList = Objects.requireNonNull(summitsList);
        
        this.panoComp = new PanoramaComputer(cem);
        this.labelizer = new Labelizer(cem, this.summitsList);
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
        return FXCollections.unmodifiableObservableList(labelsProperty.get());
    }
    
    public ObservableBooleanValue getComputeInProg(){
        return computeInProg;
    }
    
    /**
     * 
     */
    private void update() {
        double time = System.currentTimeMillis();
        computeInProg.set(true);
        
        PanoramaParameters panoramaParameters = panoramaUserParamProperty.get().panoramaParameters();
        
        
        Panorama newPano = panoComp.computePanorama(panoramaParameters);
        panoramaProperty.set(newPano);
        
        List<Node> newList = labelizer.labels(panoramaUserParamProperty.get().panoramaDisplayParameters());
        
        Platform.runLater(new Runnable() {

            public void run() {
                imageProperty.set(PanoramaRenderer.renderPanorama(imgPainter(newPano), newPano));
                labelsProperty.get().setAll(FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(newList)));
                
            }
        });
        
        computeInProg.set(false);

        System.out.println(System.currentTimeMillis()-time);
        
        
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
