/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.gui;

import static javafx.collections.FXCollections.observableArrayList;
import static javafx.collections.FXCollections.unmodifiableObservableList;

import javafx.scene.image.Image;
import java.util.List;
import java.util.Objects;

import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.gui.PanoramaRenderer;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.summit.Summit;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;

public final class PanoramaComputerBean {
    
    private ObjectProperty<PanoramaUserParameters> panoramaUserParamProp;
    private ReadOnlyObjectProperty<Panorama> panoramaProp;
    private ReadOnlyObjectProperty<Image> imageProp;
    private ReadOnlyObjectProperty<ObservableList<Node>> labelsProp;
    private final List<Summit> summitsList;
    private final ContinuousElevationModel cem; 
    
    public PanoramaComputerBean(ContinuousElevationModel cem, List<Summit> summitsList) {
        panoramaUserParamProp = new SimpleObjectProperty<>();
        panoramaProp = null; 
        imageProp = null;
        Objects.requireNonNull(summitsList);
        this.summitsList = summitsList;
        this.cem = cem;
//        labelsProp = new SimpleObjectProperty<>(unmodifiableObservableList(observableArrayList(summitsList)));
        
        
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
        PanoramaComputer newPanoComputer = new PanoramaComputer(cem);
        
        PanoramaParameters panoramaParameters = panoramaUserParamProp.get().panoramaParameters();
        Panorama newPano = newPanoComputer.computePanorama(panoramaParameters);
        
        panoramaProp = new SimpleObjectProperty<>(newPano);
        
        imageProp = new SimpleObjectProperty<>(PanoramaRenderer.renderPanorama(imgPainter(newPano), newPano));
        
        Labelizer lab = new Labelizer(cem, summitsList);
        List<Node> newList = lab.labels(panoramaParameters);
        
        labelsProp = new SimpleObjectProperty(newList);
        
        
    }
    
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
