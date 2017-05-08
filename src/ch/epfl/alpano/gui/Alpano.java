/*
 *	Author:      Samuel Chassot (270955)
 *	Date:        May 8, 2017
 */


package ch.epfl.alpano.gui;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.Scrollable;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView ;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.beans.binding.Bindings; 
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Background;
import javafx.scene.text.TextAlignment;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class Alpano extends Application{
    public static void main(String[] args){
        Application.launch(args);
        
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        
        ContinuousElevationModel cem;
        
        try(
        DiscreteElevationModel dDem1 = new HgtDiscreteElevationModel(new File("N45E006.hgt"));
        DiscreteElevationModel dDem2 = new HgtDiscreteElevationModel(new File("N45E007.hgt"));
        DiscreteElevationModel dDem3 = new HgtDiscreteElevationModel(new File("N45E008.hgt"));
        DiscreteElevationModel dDem4 = new HgtDiscreteElevationModel(new File("N45E009.hgt"));
        DiscreteElevationModel dDem5 = new HgtDiscreteElevationModel(new File("N46E006.hgt"));
        DiscreteElevationModel dDem6 = new HgtDiscreteElevationModel(new File("N46E007.hgt"));
        DiscreteElevationModel dDem7 = new HgtDiscreteElevationModel(new File("N46E008.hgt"));
        DiscreteElevationModel dDem8 = new HgtDiscreteElevationModel(new File("N46E009.hgt"))){
            
            DiscreteElevationModel dDem12 = dDem1.union(dDem2);
            DiscreteElevationModel dDem34 = dDem3.union(dDem4);
            DiscreteElevationModel dDem56 = dDem5.union(dDem6);
            DiscreteElevationModel dDem78 = dDem7.union(dDem8);
            
            DiscreteElevationModel dDem1234 = dDem12.union(dDem34);
            DiscreteElevationModel dDem5678 = dDem56.union(dDem78);
            
            DiscreteElevationModel dDemAll = dDem1234.union(dDem5678);
            cem = new ContinuousElevationModel(dDemAll);
        }
        List<Summit> summitsList = GazetteerParser.readSummitsFrom(new File("alps.txt"));
        
        PanoramaParametersBean panoParamBean = new PanoramaParametersBean(PredefinedPanoramas.ALPES_DU_JURA);
        PanoramaComputerBean panoCompBean = new PanoramaComputerBean(cem, summitsList);
        
        
        
        
        BorderPane root = new BorderPane();
        
        Scene scene = new Scene(root);

        primaryStage.setTitle("Alpano");
        primaryStage.setScene(scene);
        primaryStage.show();

        ImageView panoView = createImageView(panoParamBean, panoCompBean);
        Pane labelsPane = createLabelsPane(panoParamBean, panoCompBean);
        StackPane panoGroup = createPanoGroup(panoView, labelsPane, panoParamBean, panoCompBean);
        ScrollPane scrollPane = createPanoScrollPane(panoGroup, panoParamBean, panoCompBean);
        StackPane updateNotice = createUpdateNotice(panoParamBean, panoCompBean);
        StackPane panoPane = createPanoPane(panoParamBean, panoCompBean, updateNotice, scrollPane);
        
        root.getChildren().add(panoPane);
        
    }
    
    private ImageView createImageView(PanoramaParametersBean pUP, PanoramaComputerBean pCB){
        
        ImageView panoView = new ImageView();
        
//        panoView.setImage(pCB.getImage());
        
        panoView.fitWidthProperty().bind(pUP.widthProperty());

        
        pCB.parametersProp().addListener((p,o,n)-> panoView.setImage(pCB.getImage()));
        
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
//        labelsPane.getChildren().addAll(pCB.getLabels());
        
//        pUP.widthProperty().addListener((p,o,n)->labelsPane.prefWidth(n));
//        pUP.heightProperty().addListener((p,o,n)->labelsPane.prefHeight(n));
        
        labelsPane.prefHeightProperty().bind(pUP.heightProperty());
        labelsPane.prefWidthProperty().bind(pUP.heightProperty());
        
//        pCB.labelsProp().addListener((p,o,n) -> Bindings.bindContent(labelsPane.getChildren(), n));
        
//        Bindings.bindContent(labelsPane.getChildren(), pCB.getLabels());
        
        labelsPane.setMouseTransparent(true);
        
        return labelsPane;
        
    }
    
    private StackPane createPanoGroup(ImageView panoView, Pane labelsPane, PanoramaParametersBean pUP, PanoramaComputerBean pCB){
        StackPane panoGroup = new StackPane();
        panoGroup.getChildren().add(panoView);
        panoGroup.getChildren().add(labelsPane);
        
        return panoGroup;
    }
    
    private ScrollPane createPanoScrollPane(StackPane panoGroup, PanoramaParametersBean pUP, PanoramaComputerBean pCB){
        ScrollPane panoScrollPane = new ScrollPane(panoGroup);
        
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
        
        updateNotice.visibleProperty().bind(pUP.parametersProperty().isNotEqualTo(pCB.parametersProp()));
        
        updateNotice.setOnMouseClicked((event)-> pCB.setParameters(pUP.parametersProperty().get()));
        
        
//        Background backg = new Background(new BackgroundFill(Color.WHITE));
//        updateNotice.setBackground(backg);
        
        return updateNotice;
    }
    
    private StackPane createPanoPane(PanoramaParametersBean pUP, PanoramaComputerBean pCB,
            StackPane updateNotice, ScrollPane panoScrollPane){
        
        StackPane panoPane = new StackPane();
        panoPane.getChildren().add(panoScrollPane);
        panoPane.getChildren().add(updateNotice);
        
        return panoPane;
        
        
    }
    
    private GridPane createParamsGrid(PanoramaParametersBean pUP, PanoramaComputerBean pCB, Node ... args){
       Label latLab = new Label("Latitude (°) :");
       Label longLab = new Label("Longitude (°) :");
       Label azLab = new Label("Azimuth (°) : ");
       Label viewAngleLab = new Label("Angle de vue (°) :");
       Label altLab = new Label("Altitude (m) :");
       Label visiLab = new Label("Visibilité (km) :");
       Label widthLab = new Label("Largeur (px) :");
       Label heightLab = new Label("Hauteur (px) :");
       Label superSamplingLab = new Label("Suréchantillonnage :");
       
       TextField latField = new TextField();
       TextField longField = new TextField();
       TextField azField = new TextField();
       TextField viewAngleField = new TextField();
       TextField altField = new TextField();
       TextField visiField = new TextField();
       TextField widthField = new TextField();
       TextField heightField = new TextField();
       
       StringConverter<Integer> stringConverter = ;
       TextFormatter<Integer> formatter =
         new TextFormatter<>(stringConverter);

       
    }
}
