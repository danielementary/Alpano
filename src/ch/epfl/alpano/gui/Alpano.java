/*
 *	Author:      Samuel Chassot (270955)
 *	Date:        May 8, 2017
 */


package ch.epfl.alpano.gui;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
        
        
        BorderPane root = new BorderPane();
        
        PanoramaParametersBean panoParamBean = new PanoramaParametersBean(PredefinedPanoramas.ALPES_DU_JURA);
        PanoramaComputerBean panoCompBean = new PanoramaComputerBean(cem, summitsList);
        
        ImageView panoView = createImageView(panoParamBean, panoCompBean);
        
    }
    
    private ImageView createImageView(PanoramaParametersBean pUP, PanoramaComputerBean pCB){
        ImageView panoView = new ImageView(pCB.imageProp().get());
        pUP.widthProperty().addListener(panoView.fitWidthProperty());
        
        pCB.imageProp().addListener(panoView.imageProperty());
        
        panoView.setSmooth(true);
        panoView.setPreserveRatio(true);
        
        return panoView;
    }
}
