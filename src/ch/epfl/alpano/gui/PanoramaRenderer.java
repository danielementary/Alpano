/*
 *	Author:      Samuel Chassot (270955)
 *	Date:        5 avr. 2017
 */


package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import javafx.scene.image.*;

public interface PanoramaRenderer {

    @SuppressWarnings("restriction")
    public static Image renderPanorama(ImagePainter painter, Panorama pano){
        int width = pano.parameters().width();
        int height = pano.parameters().height();
        
        WritableImage img = new WritableImage(width, height);
        
        PixelWriter writer = img.getPixelWriter();
        
        for(int x = 0 ; x < width ; ++x){
            for(int y = 0 ; y < height ; ++y){
                writer.setColor(x, y, painter.colorAt(x, y));
            }
        }
        
        
        return img;
    }
}
