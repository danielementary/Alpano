/*
 *	Author:      Samuel Chassot (270955)
 *	Date:        27 avr. 2017
 */


package ch.epfl.alpano.gui;

import java.io.File;
import java.util.List;

import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.stage.Stage;

public final class JavaFXMainSD extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        File HGT_FILE = new File("N46E007.hgt");
        try (DiscreteElevationModel dDEM =
                new HgtDiscreteElevationModel(HGT_FILE)) {
            ContinuousElevationModel cDEM =
                    new ContinuousElevationModel(dDEM);


            List<Summit> list = GazetteerParser.readSummitsFrom(new File("alps.txt"));
            PanoramaParameters p = PredefinedPanoramas.niesen.panoramaParameters();
            Labelizer l = new Labelizer(cDEM,list);
            List<Node> n = l.labels(p);
            
//            System.out.println(n.size());
            for(int i = 0 ; i< 44 ; ++i)
                System.out.println(n.get(i));
        }               

        Platform.exit();
    }





}
