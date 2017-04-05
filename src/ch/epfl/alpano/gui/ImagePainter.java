/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.gui;

import javafx.scene.paint.Color;

@SuppressWarnings("restriction")
public interface ImagePainter {
    
    public abstract Color colorAt(int x, int y);
    
    public static ImagePainter hsb(ChannelPainter hue, 
                                   ChannelPainter saturation,
                                   ChannelPainter brightness, 
                                   ChannelPainter opacity) {
        
        return (x,y)-> Color.hsb(hue.valueAt(x, y),
                                 saturation.valueAt(x, y),
                                 brightness.valueAt(x, y),
                                 opacity.valueAt(x, y));
    }
    
    public static ImagePainter gray(ChannelPainter gray, ChannelPainter opacity) {
        return (x,y)->Color.gray(gray.valueAt(x, y), opacity.valueAt(x, y));
    }
}
