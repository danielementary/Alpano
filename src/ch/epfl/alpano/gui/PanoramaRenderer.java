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
        WritableImage img = new WritableImage(pano.parameters().width(), pano.parameters().height());
        PixelWriter writer = img.getPixelWriter();
        
        return null;
    }
}
