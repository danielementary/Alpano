/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.gui;

import javafx.scene.paint.Color;

@FunctionalInterface
@SuppressWarnings("restriction")
public interface ImagePainter {
    
    /**
     * gives the color of the pixel at position x,y
     * @param x the coordinate x of the pixel
     * @param y the coordinate y of the pixel
     * @return a Color
     */
    public abstract Color colorAt(int x, int y);
    
    
    /**
     * create a new ImagePainter with the 4 channelPainter given in argument. The color is created
     * with the HSB system.
     * @param hue
     * @param saturation
     * @param brightness
     * @param opacity
     * @return a new ImagePainter
     */
    public static ImagePainter hsb(ChannelPainter hue, 
                                   ChannelPainter saturation,
                                   ChannelPainter brightness, 
                                   ChannelPainter opacity) {
        
        return (x,y) -> Color.hsb(hue.valueAt(x, y),
                                   saturation.valueAt(x, y),
                                   brightness.valueAt(x, y),
                                   opacity.valueAt(x, y));
    }
    
    /**
     * create a new imagePainter. the Color is in gray variation.
     * @param gray
     * @param opacity
     * @return a new ImagePainter
     */
    public static ImagePainter gray(ChannelPainter gray, ChannelPainter opacity) {
        return (x,y) -> Color.gray(gray.valueAt(x, y), opacity.valueAt(x, y));
    }
    
    
    
}
