/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public interface PanoramaRenderer {
   
    /**
     * create a new Image with the imagePainter and the panorama given in argument.
     * @param painter
     * @param pano
     * @return a Image
     */
    public static Image renderPanorama(ImagePainter painter, Panorama pano) {
        
        int width = pano.parameters().width();
        int height = pano.parameters().height();
        
        WritableImage img = new WritableImage(width, height);
        
        PixelWriter writer = img.getPixelWriter();
        
        for (int x = 0 ; x < width ; ++x) {
            for (int y = 0 ; y < height ; ++y) {
                
                writer.setColor(x, y, painter.colorAt(x, y));
            }
        }

        return img;
    }
}
