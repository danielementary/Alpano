/*
 *	Author:      Samuel Chassot (270955)
 *	Date:        May 8, 2017
 */


package ch.epfl.alpano.gui;

import java.awt.GridBagConstraints;
import java.io.File;
import java.util.List;
import java.util.Observable;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView ;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ChoiceBox;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import static javafx.scene.paint.Color.color;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ListChangeListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.property.ObjectProperty;



public class Alpano extends Application{
    public static void main(String[] args){
        Application.launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {

        ContinuousElevationModel cem;
        
        DiscreteElevationModel dDem1 = new HgtDiscreteElevationModel(new File("N45E006.hgt"));
        DiscreteElevationModel dDem2 = new HgtDiscreteElevationModel(new File("N45E007.hgt"));
        DiscreteElevationModel dDem3 = new HgtDiscreteElevationModel(new File("N45E008.hgt"));
        DiscreteElevationModel dDem4 = new HgtDiscreteElevationModel(new File("N45E009.hgt"));
        DiscreteElevationModel dDem5 = new HgtDiscreteElevationModel(new File("N46E006.hgt"));
        DiscreteElevationModel dDem6 = new HgtDiscreteElevationModel(new File("N46E007.hgt"));
        DiscreteElevationModel dDem7 = new HgtDiscreteElevationModel(new File("N46E008.hgt"));
        DiscreteElevationModel dDem8 = new HgtDiscreteElevationModel(new File("N46E009.hgt"));

        DiscreteElevationModel dDem12 = dDem1.union(dDem2);
        DiscreteElevationModel dDem34 = dDem3.union(dDem4);
        DiscreteElevationModel dDem56 = dDem5.union(dDem6);
        DiscreteElevationModel dDem78 = dDem7.union(dDem8);

        DiscreteElevationModel dDem1234 = dDem12.union(dDem34);
        DiscreteElevationModel dDem5678 = dDem56.union(dDem78);

        DiscreteElevationModel dDemAll = dDem1234.union(dDem5678);
        
        cem = new ContinuousElevationModel(dDemAll);
        List<Summit> summitsList = GazetteerParser.readSummitsFrom(new File("alps.txt"));

        PanoramaParametersBean panoParamBean = new PanoramaParametersBean(PredefinedPanoramas.ALPES_DU_JURA);
        PanoramaComputerBean panoCompBean = new PanoramaComputerBean(cem, summitsList);

//        panoCompBean.setParameters(panoParamBean.parametersProperty().get());

        ImageView panoView = createImageView(panoParamBean, panoCompBean);
        Pane labelsPane = createLabelsPane(panoParamBean, panoCompBean);
        
        StackPane panoGroup = createPanoGroup(panoView, labelsPane, panoParamBean, panoCompBean);
        ScrollPane scrollPane = createPanoScrollPane(panoGroup, panoParamBean, panoCompBean);
        
        StackPane updateNotice = createUpdateNotice(panoParamBean, panoCompBean);
        StackPane panoPane = createPanoPane(panoParamBean, panoCompBean, updateNotice, scrollPane);

        GridPane paramsGrid = createParamsGrid(panoParamBean, panoCompBean);

        BorderPane root = new BorderPane();
        root.setCenter(panoPane);
        root.setBottom(paramsGrid);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Alpano");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private ImageView createImageView(PanoramaParametersBean pUP, PanoramaComputerBean pCB){
        
        ImageView panoView = new ImageView();
        
        panoView.fitWidthProperty().bind(pUP.widthProperty());
        
        panoView.imageProperty().bind(pCB.imageProp());
        
        panoView.setSmooth(true);
        panoView.setPreserveRatio(true);

        panoView.setOnMouseMoved((event)->{
            event.getSceneX();
            event.getSceneY();
            //a continuer
        });
        
        panoView.setOnMouseClicked((event)->{
            //ouvrir l'url  
        });
        
        return panoView;
    }
    
    private Pane createLabelsPane(PanoramaParametersBean pUP, PanoramaComputerBean pCB){
        
        Pane labelsPane = new Pane();
        
        labelsPane.prefHeightProperty().bind(pUP.heightProperty());
        labelsPane.prefWidthProperty().bind(pUP.widthProperty());
                
        Bindings.bindContent(labelsPane.getChildren(), pCB.labelsProp().get());
        
                
        labelsPane.setMouseTransparent(true);
        
        return labelsPane;
    }
    
    private StackPane createPanoGroup(ImageView panoView, Pane labelsPane, PanoramaParametersBean pUP, PanoramaComputerBean pCB){
        StackPane panoGroup = new StackPane();
        panoGroup.getChildren().addAll(panoView, labelsPane);
        
        return panoGroup;
    }
    
    private ScrollPane createPanoScrollPane(StackPane panoGroup, PanoramaParametersBean pUP, PanoramaComputerBean pCB){
        ScrollPane panoScrollPane = new ScrollPane();
        panoScrollPane.setContent(panoGroup);
        
        return panoScrollPane;
    }
    
    private StackPane createUpdateNotice(PanoramaParametersBean pUP, PanoramaComputerBean pCB){
        double textSize = 40;
        StackPane updateNotice = new StackPane();
        
        Text text = new Text();
        text.setText("Les paramètres du panorama ont changé. Cliquez ici pour mettre le dessin à jour.");
        text.setFont(new Font(textSize));
        text.setTextAlignment(TextAlignment.CENTER);
        
        updateNotice.getChildren().add(text);
        
        BooleanBinding booleanCondition = pUP.parametersProperty().isNotEqualTo(pCB.parametersProp());
        
        updateNotice.visibleProperty().bind(booleanCondition);
        
        updateNotice.setOnMouseClicked((event)-> pCB.setParameters(pUP.parametersProperty().get()));
        
        Background backg = new Background(new BackgroundFill(color(1,1,1,0.9), CornerRadii.EMPTY, Insets.EMPTY));
        updateNotice.setBackground(backg);
        
        return updateNotice;
    }
    
    private StackPane createPanoPane(PanoramaParametersBean pUP, PanoramaComputerBean pCB,
                                     StackPane updateNotice, ScrollPane panoScrollPane) {
        
        StackPane panoPane = new StackPane();
        panoPane.getChildren().addAll(panoScrollPane, updateNotice);
        
        return panoPane;
    }
    
    private GridPane createParamsGrid(PanoramaParametersBean pUP, PanoramaComputerBean pCB){
        GridPane paramsGrid = new GridPane();

        Label latLab = new Label("Latitude (°) :");
        Label longLab = new Label("Longitude (°) :");
        Label azLab = new Label("Azimuth (°) : ");
        Label viewAngleLab = new Label("Angle de vue (°) :");
        Label altLab = new Label("Altitude (m) :");
        Label visiLab = new Label("Visibilité (km) :");
        Label widthLab = new Label("Largeur (px) :");
        Label heightLab = new Label("Hauteur (px) :");
        Label superSamplingLab = new Label("Suréchantillonnage :");
        
        latLab.setTextAlignment(TextAlignment.RIGHT);
        longLab.setTextAlignment(TextAlignment.RIGHT);
        azLab.setTextAlignment(TextAlignment.RIGHT);
        viewAngleLab.setTextAlignment(TextAlignment.RIGHT);
        altLab.setTextAlignment(TextAlignment.RIGHT);
        visiLab.setTextAlignment(TextAlignment.RIGHT);
        widthLab.setTextAlignment(TextAlignment.RIGHT);
        heightLab.setTextAlignment(TextAlignment.RIGHT);
        superSamplingLab.setTextAlignment(TextAlignment.RIGHT);
        
        StringConverter<Integer> stringConverterFixedPoint = new FixedPointStringConverter(4);
        StringConverter<Integer> stringConverterFixedPointZero = new FixedPointStringConverter(0);

        
        TextField latField = createTextField(stringConverterFixedPoint, 7, pUP.observerLatitudeProperty());
        TextField longField = createTextField(stringConverterFixedPoint, 7, pUP.observerLongitudeProperty());
        TextField azField = createTextField(stringConverterFixedPointZero, 3, pUP.CenterAzimuthProperty());
        TextField viewAngleField = createTextField(stringConverterFixedPointZero, 3, pUP.horizontalFieldOfViewProperty());
        TextField altField = createTextField(stringConverterFixedPointZero, 4, pUP.observerElevationProperty());
        TextField visiField = createTextField(stringConverterFixedPointZero, 3, pUP.maxDistanceProperty());
        TextField widthField = createTextField(stringConverterFixedPointZero, 4, pUP.widthProperty());
        TextField heightField = createTextField(stringConverterFixedPointZero, 4, pUP.heightProperty());
        
        
        
        ChoiceBox superSamplingBox = new ChoiceBox<>();
        superSamplingBox.getItems().addAll(0,1,2);

        StringConverter<Integer> stringConverterSampling = new LabeledListStringConverter("non", "2x", "4x");

        

        superSamplingBox.valueProperty().bindBidirectional(pUP.SuperSamplingExponentProperty());
        
        superSamplingBox.setConverter(stringConverterSampling);
        
        

        paramsGrid.addRow(0, latLab, latField, longLab, longField, altLab, altField);
        paramsGrid.addRow(1, azLab, azField, viewAngleLab, viewAngleField, visiLab, visiField);
        paramsGrid.addRow(2, widthLab, widthField, heightLab, heightField, superSamplingLab, superSamplingBox);
        
        return paramsGrid;
    }

    
    private TextField createTextField(StringConverter<Integer> strConv, int columnNum, ObjectProperty<Integer> objProp){
        TextField textField = new TextField();
        TextFormatter<Integer> formatter = new TextFormatter<>(strConv);
        formatter.valueProperty().bindBidirectional(objProp);
        textField.setTextFormatter(formatter);
        textField.setAlignment(Pos.CENTER_RIGHT);
        textField.setPrefColumnCount(columnNum);
        
        return textField;
        
        
        
    }
}
