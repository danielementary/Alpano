///*
// *	Author:      Samuel Chassot (270955)
// *	Date:        25 avr. 2017
// */
//
//
//package ch.epfl.alpano.gui;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.List;
//
//import org.junit.Test;
//
//import ch.epfl.alpano.PanoramaParameters;
//import ch.epfl.alpano.dem.ContinuousElevationModel;
//import ch.epfl.alpano.dem.DiscreteElevationModel;
//import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
//import ch.epfl.alpano.summit.GazetteerParser;
//import ch.epfl.alpano.summit.Summit;
//
//import javafx.scene.Node;
//import javafx.scene.text.Text;
//import javafx.scene.transform.Rotate;
//import javafx.scene.transform.Translate;
//import javafx.scene.shape.Line;
//
//public class LabelizerTestSD {
//
//    @Test
//    public void test() throws Exception {
//        File HGT_FILE = new File("N46E007.hgt");
//        try (DiscreteElevationModel dDEM =
//                new HgtDiscreteElevationModel(HGT_FILE)) {
//            ContinuousElevationModel cDEM =
//                    new ContinuousElevationModel(dDEM);
//
//
//            List<Summit> list = GazetteerParser.readSummitsFrom(new File("alps.txt"));
//            PanoramaParameters p = PredefinedPanoramas.NIESEN.panoramaParameters();
//            Labelizer l = new Labelizer(cDEM,list);
//            List<Node> n = l.labels(p);
//            
////            for (Node node : n) {
////                System.out.println(n);
////            }
//        }               
//
//    }
//}
