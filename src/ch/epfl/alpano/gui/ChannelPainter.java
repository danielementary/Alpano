/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.gui;

import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.Panorama;

@FunctionalInterface
public interface ChannelPainter {

    /**
     * return the value of the channel at the position x,y  
     * @param x coordinate x
     * @param y coordinate y
     * @return the value at (x,y) 
     */
    public abstract float valueAt(int x, int y);
    
    /**
     * return the greatest difference between the distance of the pixel (x,y) 
     * and the distance of his neighbours
     * @param pano the panorama in which you want to work
     * @return the max difference distance
     */
    public static ChannelPainter maxDistanceToNeighbors(Panorama pano) {

        return (x,y) -> Math.max(Math.max(pano.distanceAt(x, y-1, 0), pano.distanceAt(x, y+1, 0)),
                                 Math.max(pano.distanceAt(x+1, y, 0), pano.distanceAt(x-1, y, 0)))
                        -pano.distanceAt(x, y);
    }
    
    /**
     * return the distance of the pixel(x,y) from the observator
     * @param pano the panorama in which you want to work
     * @return a distance
     */
    public static ChannelPainter distance(Panorama pano) {

        return (x,y) -> pano.distanceAt(x, y);
    }
    
    /**
     * return the slope of the pixel (x,y)
     * @param pano the panorama in which you want to work
     * @return a slope
     */
    public static ChannelPainter slope(Panorama pano) {

        return (x,y) -> pano.slopeAt(x, y);
    }
    
    /**
     * return the opacity corresponding to the distance of pixel (x,y)
     * @param pano the panorama in which you want to work
     * @return 0 if the distance is infinite, 1 otherwhise
     */
    public static ChannelPainter opacity(Panorama pano) {

        return (x,y) -> (pano.distanceAt(x, y) == Float.POSITIVE_INFINITY) ? 0 : 1;
    }
    
    /**
     * add the constant to the value of the channel
     * @param constant the constant
     * @return a new Channel
     */
    public default ChannelPainter add(float constant) {
        
        return (x,y) -> valueAt(x,y) + constant;  
    }
    
    /**
     * multiply by a constant to the value of the channel
     * @param constant the constant
     * @return a new Channel
     */
    public default ChannelPainter mul(float constant) {
        
        return (x,y) -> valueAt(x,y) * constant;  
    }
    
    /**
     * substract a constant to the value of the channel
     * @param constant the constant
     * @return a new Channel
     */
    public default ChannelPainter sub(float constant) {
        
        return (x,y) -> valueAt(x,y) - constant;  
    }
   
    /**
     * divide by a constant to the value of the channel
     * @param constant the constant
     * @return a new Channel
     */
    public default ChannelPainter div(float constant) {
        
        return (x,y) -> valueAt(x,y)  / constant;  
    }
    
    /**
     * multiply by a constant to the value of the channel
     * @param constant the constant
     * @return a new Channel
     */
    public default ChannelPainter map(DoubleUnaryOperator op) {
        
        return (x,y) -> (float) op.applyAsDouble(valueAt(x,y));
    }
    
    /**
     * gives the max between 0 and the minimum of the valueAt(x,y) and 1
     * @return a new ChannelPainter
     */
    public default ChannelPainter clamp() {
        
        return (x,y) -> Math.max(0, Math.min(valueAt(x,y), 1));
    }
    
    /**
     * gives the 1-valueAt(x,y)
     * @return a new ChannelPainter
     */
    public default ChannelPainter invert() {
        
        return (x,y) -> 1 - valueAt(x,y);
    }
    
    /**
     * give the valueAt(x,y) mod 1
     * @return a new ChannelPainter
     */
    public default ChannelPainter cycle() {
        
        return (x,y) -> valueAt(x,y) % 1;
    }
    
   
}
