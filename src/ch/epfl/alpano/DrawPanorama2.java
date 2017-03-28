package ch.epfl.alpano;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.awt.Color;
import java.awt.image.BufferedImage;

import static java.lang.Math.*;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

import java.io.File;

import javax.imageio.ImageIO;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;

final class DrawPanorama2 {
    final static File HGT_FILE1 = new File("N46E006.hgt");
    final static File HGT_FILE2 = new File("N46E007.hgt");
    final static File HGT_FILE3 = new File("N46E008.hgt");
    
    final static File HGT_FILE4 = new File("N45E006.hgt");
    final static File HGT_FILE5 = new File("N45E007.hgt");
    final static File HGT_FILE6 = new File("N45E008.hgt");
    
    final static File HGT_FILE7 = new File("N47E006.hgt");
    final static File HGT_FILE8 = new File("N47E007.hgt");
    final static File HGT_FILE9 = new File("N47E008.hgt");


    final static int IMAGE_WIDTH = 9000;
    final static int IMAGE_HEIGHT = 4000;

    final static double ORIGIN_LON = Math.toRadians(6.88440);
    final static double ORIGIN_LAT = Math.toRadians(46.56642 );
    final static int ELEVATION = 2005;
    final static double CENTER_AZIMUTH = Math.toRadians(175.00);
    final static double HORIZONTAL_FOV = Math.toRadians(120);
    final static int MAX_DISTANCE = 175_000;

    final static PanoramaParameters PARAMS =
      new PanoramaParameters(new GeoPoint(ORIGIN_LON,
                                          ORIGIN_LAT),
                             ELEVATION,
                             CENTER_AZIMUTH,
                             HORIZONTAL_FOV,
                             MAX_DISTANCE,
                             IMAGE_WIDTH,
                             IMAGE_HEIGHT);

    public static void main(String[] as) throws Exception {
        
             try{
                 DiscreteElevationModel dDEM1 =
                         new HgtDiscreteElevationModel(HGT_FILE1);
                 DiscreteElevationModel dDEM2 =
                         new HgtDiscreteElevationModel(HGT_FILE2);
                 DiscreteElevationModel dDEM3 =
                         new HgtDiscreteElevationModel(HGT_FILE3);
                 DiscreteElevationModel dDEM4 =
                         new HgtDiscreteElevationModel(HGT_FILE4);
                 DiscreteElevationModel dDEM5 =
                         new HgtDiscreteElevationModel(HGT_FILE5);
                 DiscreteElevationModel dDEM6 =
                         new HgtDiscreteElevationModel(HGT_FILE6);
                 DiscreteElevationModel dDEM7 =
                         new HgtDiscreteElevationModel(HGT_FILE7);
                 DiscreteElevationModel dDEM8 =
                         new HgtDiscreteElevationModel(HGT_FILE8);
                 DiscreteElevationModel dDEM9 =
                         new HgtDiscreteElevationModel(HGT_FILE9);
                 
                 
                 
                 DiscreteElevationModel dDEM123 = dDEM1.union(dDEM2).union(dDEM3);
                 DiscreteElevationModel dDEM456 = dDEM4.union(dDEM5).union(dDEM6);
                 DiscreteElevationModel dDEM789 = dDEM7.union(dDEM8).union(dDEM9);
                 
                 DiscreteElevationModel dDEM_final = dDEM123.union(dDEM456).union(dDEM789);
                 
                 ContinuousElevationModel cDEM =
                       new ContinuousElevationModel(dDEM_final);
                     Panorama p = new PanoramaComputer(cDEM)
                       .computePanorama(PARAMS);
             

             BufferedImage i =
               new BufferedImage(IMAGE_WIDTH,
                                 IMAGE_HEIGHT,
                                 TYPE_INT_RGB);

             for (int x = 0; x < IMAGE_WIDTH; ++x) {
               for (int y = 0; y < IMAGE_HEIGHT; ++y) {
                 float d = p.distanceAt(x, y);
                 float s = p.slopeAt(x, y);
                 int c = (d == Float.POSITIVE_INFINITY)
                   ? 0x87_CE_EB
//                   : gray((d - 2_000) / 40_000);
                   :color2(d,s);
                 
                 i.setRGB(x, y, c);
               }
             }
             

        ImageIO.write(i, "png", new File("Zenias_view_newColors.png"));
        
        dDEM1.close();
        dDEM2.close();
        dDEM3.close();
        dDEM4.close();
        dDEM5.close();
        dDEM6.close();
        dDEM7.close();
        dDEM8.close();
        dDEM9.close();
        
        dDEM123.close();
        dDEM456.close();
        dDEM789.close();
      }finally{
        
      }
        }
    
    
    private static int gray(double v) {
        double clampedV = max(0, min(v, 1));
        int gray = (int) (255.9999 * clampedV);
        return (gray << 16) | (gray << 8) | gray;
      }
    
    private static int color (double d, double s){
        Color color;
        
        if(d < 2_000){
            color = new Color(200,255,255);
        }else if(d < 10_000){
            color = new Color(100,255,100);
        }else if (d < 30_000){
            color = new Color(255,150,100);
        }else if (d < 50_000){
            color = new Color(0,255,255);
        }else if (d < 120_000){
            color = new Color(255,0,0);
        }else{
            color = new Color(0,175,180);
        }
        
        color = new Color(Math.max(0, color.getRed() - (int) (s*230)),
                Math.max(0, color.getGreen() - (int) (s*230)),
                Math.max(0, color.getBlue() - (int) (s*230)));
        
        return color.getRGB();
    }
    
    private static int color2(double d, double s){
        Color color;
        color = Color.getHSBColor((float)(d/MAX_DISTANCE), 0.6f, (float)(1-s/(Math.PI/2)));

       


        return color.getRGB();
    }
  }