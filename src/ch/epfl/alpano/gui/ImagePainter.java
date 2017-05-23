/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import javafx.scene.paint.Color;

@FunctionalInterface
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
     * this method is never used but was asked to be done
     * @param gray
     * @param opacity
     * @return a new ImagePainter
     */
    public static ImagePainter gray(ChannelPainter gray, ChannelPainter opacity) {
        
        return (x,y) -> Color.gray(gray.valueAt(x, y),
                                   opacity.valueAt(x, y));
    }
    
    /**
     * personalized ImagePainter
     * @param panorama the ImagePainter will color according to
     * @return ImagePainter for panorama draw corresponding to teacher's parameters
     */
    public static ImagePainter stdPainter(Panorama p){
            
        ChannelPainter hue, saturation, brightness, opacity;

        hue = ChannelPainter.distance(p)
                            .div(100000)
                            .cycle()
                            .mul(360);

        saturation = ChannelPainter.distance(p)
                                   .div(200000)
                                   .clamp()
                                   .invert();

        brightness = ChannelPainter.slope(p)
                                   .mul(2)
                                   .div((float) Math.PI)
                                   .invert()
                                   .mul((float) 0.7)
                                   .add((float) 0.3);

        opacity = ChannelPainter.opacity(p);

        return ImagePainter.hsb(hue, saturation, brightness, opacity);
    }
    
    /**
     * return a new ImagePainter drawing lines (given at step 7)
     * @param p
     * @return a new ImagePainter
     */
    public static ImagePainter linePainter(Panorama p){
        ChannelPainter gray =
                ChannelPainter.maxDistanceToNeighbors(p)
                .sub(500)
                .div(4500)
                .clamp()
                .invert();

        ChannelPainter distance = p::distanceAt;
        ChannelPainter opacity =
                distance.map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);

        return ImagePainter.gray(gray, opacity);


    }
    
    /**
     * personalized ImagePainter
     * @param panorama the ImagePainter will color according to
     * @return ImagePainter for panorama draw corresponding to a very colorful painter
     */
    public static ImagePainter colorPainter(Panorama p){
        
        ChannelPainter hue, saturation, brightness, opacity;

        hue = ChannelPainter.distance(p)
                            .div(100000)
                            .cycle()
                            .mul(36000);

        saturation = ChannelPainter.distance(p)
                                   .div(200000)
                                   .clamp()
                                   .invert();

        brightness = ChannelPainter.slope(p)
                                   .mul(2)
                                   .div((float) Math.PI)
                                   .invert()
                                   .mul((float) 0.7)
                                   .add((float) 0.3);

        opacity = ChannelPainter.opacity(p);

        return ImagePainter.hsb(hue, saturation, brightness, opacity);
    }
    
    public static ImagePainter rndPainter(Panorama p){
        
        ChannelPainter hue, saturation, brightness, opacity;

        hue = (x,y)-> (int) Math.floor(Math.random() * 360);

        saturation = ChannelPainter.distance(p)
                                   .div(200000)
                                   .clamp()
                                   .invert();

        brightness = ChannelPainter.slope(p)
                                   .mul(2)
                                   .div((float) Math.PI)
                                   .invert()
                                   .mul((float) 0.7)
                                   .add((float) 0.3);

        opacity = ChannelPainter.opacity(p);

        return ImagePainter.hsb(hue, saturation, brightness, opacity);
    }
    
    public static ImagePainter smallPainter(Panorama p){
        
        ChannelPainter hue, saturation, brightness, opacity;

        hue = ChannelPainter.distance(p)
                            .div(100000)
                            .cycle()
                            .mul(1500);
        
        saturation = ChannelPainter.distance(p)
                                   .div(200000)
                                   .clamp()
                                   .invert();

        brightness = ChannelPainter.slope(p)
                                   .mul(2)
                                   .div((float) Math.PI)
                                   .invert()
                                   .mul((float) 0.7)
                                   .add((float) 0.3);
        

        opacity = ChannelPainter.opacity(p);

        return ImagePainter.hsb(hue, saturation, brightness, opacity);
    }
    
    public static ImagePainter verticalPainter(Panorama p){

        ChannelPainter hue, saturation, brightness, opacity;

        hue = ChannelPainter.elevation(p)
                .div(100000)
                .cycle()
                .mul(9000);

        saturation = ChannelPainter.distance(p)
                .div(200000)
                .clamp()
                .invert();

        brightness = ChannelPainter.slope(p)
                .mul(2)
                .div((float) Math.PI)
                .invert()
                .mul((float) 0.7)
                .add((float) 0.3);


        opacity = ChannelPainter.opacity(p);

        return ImagePainter.hsb(hue, saturation, brightness, opacity);
    }
    
    /**
     * personalized ImagePainter
     * @param panorama the ImagePainter will color according to
     * @return ImagePainter for panorama draw corresponding to teacher's parameters
     */
    public static ImagePainter chessboardPainter(Panorama p){
            
        ChannelPainter hue, saturation, brightness, opacity;

        hue = ChannelPainter.distance(p)
                            .div(100000)
                            .cycle()
                            .mul(360);

        saturation = ChannelPainter.longLat(p);

        brightness = ChannelPainter.slope(p)
                                   .mul(2)
                                   .div((float) Math.PI)
                                   .invert()
                                   .mul((float) 0.7)
                                   .add((float) 0.3);

        opacity = ChannelPainter.opacity(p);

        return ImagePainter.hsb(hue, saturation, brightness, opacity);
    }

}
