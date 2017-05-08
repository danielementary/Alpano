/*
 *	Author:      Samuel Chassot (270955)
 *	Date:        May 8, 2017
 */


package ch.epfl.alpano.gui;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
        ImageView panoView = createImageView(panoParamBean, panoCompBean);
        
    }
    
    private ImageView createImageView(PanoramaParametersBean pUP, PanoramaComputerBean pCB){
        
        ImageView panoView = new ImageView();
        
        panoView.setImage(pCB.getImage());
        
        //le cast est peut etre degue
        pUP.widthProperty().addListener((p,o,n)->
            panoView.setFitWidth(n));
        
        pCB.imageProp().addListener((p,o,n)-> panoView.setImage(n));
        
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
        labelsPane.getChildren().addAll(pCB.getLabels());
        
        pUP.widthProperty().addListener((p,o,n)->labelsPane.prefWidth(n));
        pUP.heightProperty().addListener((p,o,n)->labelsPane.prefHeight(n));
        
        
    }
    
    private StackPane createPanoGroup(ImageView panoView, Pane labelsPane, PanoramaParametersBean pUP, PanoramaComputerBean pCB){
        //TODO
    }
    
    private ScrollPane createPanoScrollPane(StackPane panoGroup, PanoramaParametersBean pUP, PanoramaComputerBean pCB){
        //TODO
    }
    
    private StackPane createUpdateNotice(Text textUpdate, PanoramaParametersBean pUP, PanoramaComputerBean pCB){
        //TODO
    }
    
    private StackPane createPanoPane(PanoramaParametersBean pUP, PanoramaComputerBean pCB,
            StackPane updateNotice, ScrollPane panoScrollPane){
        //TODO
    }
    
    private GridPane ParamsGrid(PanoramaParametersBean pUP, PanoramaComputerBean pCB, Node ... args){
        //TODO
    }
}
