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
    final static File HGT_FILE = new File("N46E007.hgt");
    final static File HGT_FILE2 = new File("N46E006.hgt");
    final static File HGT_FILE3 = new File("N45E007.hgt");
    final static File HGT_FILE4 = new File("N45E006.hgt");

    final static int IMAGE_WIDTH = 4000;
    final static int IMAGE_HEIGHT = 1500;

    final static double ORIGIN_LON = Math.toRadians(6.88440);
    final static double ORIGIN_LAT = Math.toRadians(46.56642 );
    final static int ELEVATION = 900;
    final static double CENTER_AZIMUTH = Math.toRadians(178.18);
    final static double HORIZONTAL_FOV = Math.toRadians(100);
    final static int MAX_DISTANCE = 150_000;

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
                         new HgtDiscreteElevationModel(HGT_FILE);
                 DiscreteElevationModel dDEM2 =
                         new HgtDiscreteElevationModel(HGT_FILE2);
                 DiscreteElevationModel dDEM3 =
                         new HgtDiscreteElevationModel(HGT_FILE3);
                 DiscreteElevationModel dDEM4 =
                         new HgtDiscreteElevationModel(HGT_FILE4);
                 
                 
                 DiscreteElevationModel dDEM12 = dDEM1.union(dDEM2);
                 DiscreteElevationModel dDEM34 = dDEM3.union(dDEM4);
                 
                 DiscreteElevationModel dDEM_final = dDEM12.union(dDEM34);
                 
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
                   :color(d,s);
                 
                 i.setRGB(x, y, c);
               }
             }
             

        ImageIO.write(i, "png", new File("Zenias_view.png"));
        
        dDEM1.close();
        dDEM2.close();
        dDEM3.close();
        dDEM4.close();
        dDEM12.close();
        dDEM34.close();
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
        }else{
            color = new Color(0,175,180);
        }
        
        color = new Color(Math.max(0, color.getRed() - (int) (s*230)),
                Math.max(0, color.getGreen() - (int) (s*230)),
                Math.max(0, color.getBlue() - (int) (s*230)));
        
        return color.getRGB();
    }
  }