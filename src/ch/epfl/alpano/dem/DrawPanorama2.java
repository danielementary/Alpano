/*
 *	Author:      Samuel Chassot (270955)
 *	Date:        21 mars 2017
 */


package ch.epfl.alpano.dem;

import static java.lang.Math.max;
import static java.lang.Math.min;

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

    final static int IMAGE_WIDTH = 3000;
    final static int IMAGE_HEIGHT = 1200;

    final static double ORIGIN_LON = Math.toRadians(7.05097);
    final static double ORIGIN_LAT = Math.toRadians(46.62305);
    final static int ELEVATION = 800;
    final static double CENTER_AZIMUTH = Math.toRadians(174.95);
    final static double HORIZONTAL_FOV = Math.toRadians(80);
    final static int MAX_DISTANCE = 100_000;

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
        try (DiscreteElevationModel dDEM =
                new HgtDiscreteElevationModel(HGT_FILE)) {
             ContinuousElevationModel cDEM =
               new ContinuousElevationModel(dDEM);
             Panorama p = new PanoramaComputer(cDEM)
               .computePanorama(PARAMS);

             BufferedImage i =
               new BufferedImage(IMAGE_WIDTH,
                                 IMAGE_HEIGHT,
                                 TYPE_INT_RGB);

             for (int x = 0; x < IMAGE_WIDTH; ++x) {
               for (int y = 0; y < IMAGE_HEIGHT; ++y) {
                 float d = p.distanceAt(x, y);
                 int c = (d == Float.POSITIVE_INFINITY)
                   ? 0x87_CE_EB
                   : gray((d - 2_000) / 15_000);
                 i.setRGB(x, y, c);
               }
             }

        ImageIO.write(i, "png", new File("test.png"));
      }
    }
    
    private static int gray(double v) {
        double clampedV = max(0, min(v, 1));
        int gray = (int) (255.9999 * clampedV);
        return (gray << 16) | (gray << 8) | gray;
      }
  }