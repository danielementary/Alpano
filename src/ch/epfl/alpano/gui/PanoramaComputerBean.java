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
import javafx.application.Platform;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.property.SimpleBooleanProperty;




public final class PanoramaComputerBean {
    
    private final ObjectProperty<PanoramaUserParameters> panoramaUserParamProperty;
    private final ObjectProperty<Panorama> panoramaProperty;
    private final ObjectProperty<Image> imageProperty;
    
    private final ObservableList<Node> labelsList;
    private final ObservableList<Node> labels;
    
    private final ContinuousElevationModel cem; 
    private final List<Summit> summitsList;
    private final PanoramaComputer panoComp;
    private final Labelizer labelizer;
    private SimpleBooleanProperty computeInProg;
    private ObjectProperty<Integer> choicePainterProp;
    private List<ImagePainter> painterList;
    


    /***
     * constructs an instance of drawed panorama parameters
     * @param cem
     * @param summitsList
     */
    public PanoramaComputerBean(ContinuousElevationModel cem, List<Summit> summitsList) {
        
        this.panoramaUserParamProperty = new SimpleObjectProperty<>();
        this.panoramaUserParamProperty.addListener((p, o, n) -> update());

        this.computeInProg = new SimpleBooleanProperty(false);

        this.panoramaProperty = new SimpleObjectProperty<>(); 
        this.imageProperty = new SimpleObjectProperty<>();
        
        this.labels = FXCollections.observableArrayList();
        this.labelsList = FXCollections.unmodifiableObservableList(labels);
        
        this.cem = cem;
        this.summitsList = Objects.requireNonNull(summitsList);

        this.panoComp = new PanoramaComputer(cem);
        this.labelizer = new Labelizer(cem, this.summitsList);
        
        this.choicePainterProp = new SimpleObjectProperty<Integer>();
        
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
    public ObservableList<Node> labelsList() {
        return labelsList;
    }

    //    /**
    //     * getter : labelsProperty
    //     * @return ObservableList<Node>
    //     */
    //    public ObservableList<Node> getLabels() {
    //        return FXCollections.unmodifiableObservableList(labelsList.get());
    //    }

    public ObservableBooleanValue getComputeInProg(){
        return computeInProg;
    }
    
    public ObjectProperty getChoicePainterProp(){
        return choicePainterProp;
    }

    /**
     * update the properties
     */
    private void update() {
        computeInProg.set(true);

        PanoramaParameters panoramaParameters = panoramaUserParamProperty.get().panoramaParameters();


        Panorama newPano = panoComp.computePanorama(panoramaParameters);
        panoramaProperty.set(newPano);

        List<Node> newList = labelizer.labels(panoramaUserParamProperty.get().panoramaDisplayParameters());

        Platform.runLater(new Runnable() {

            public void run() {
                imageProperty.set(PanoramaRenderer.renderPanorama(imgPainter(newPano), newPano));
                labels.setAll(newList);

            }
        });

        computeInProg.set(false);

    }
    
    public void updateImage(){
        imageProperty.set(PanoramaRenderer.renderPanorama(imgPainter(panoramaProperty.get()), panoramaProperty.get()));
    }
    
    //to choose the right painter according to choice property
    private ImagePainter imgPainter(Panorama p){
        switch(choicePainterProp.get()){
        case 0:
            return imgPainterDefault(p);
        case 1:
            return imgPainterLine(p);
        }
        return imgPainterDefault(p);
    }

    /**
     * 
     * @param p
     * @return
     */
    private ImagePainter imgPainterDefault(Panorama p){

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

    private ImagePainter imgPainterLine(Panorama p){
        ChannelPainter gray =
                ChannelPainter.maxDistanceToNeighbors(p)
                .sub(500)
                .div(4500)
                .clamp()
                .invert();

        ChannelPainter distance = p::distanceAt;
        ChannelPainter opacity =
                distance.map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);

        return ImagePainter.gray(gray, opacity);


    }
}
