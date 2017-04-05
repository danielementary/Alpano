/*
 *	Author:      Samuel Chassot (270955)
 *	Date:        4 avr. 2017
 */


package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;

@FunctionalInterface
public interface ChannelPainter {

    public abstract float valueAt(int x, int y);
    
    public static ChannelPainter maxDistanceToNeighbors(Panorama pano){
        
        return (x,y)->
            Math.max(Math.max(pano.distanceAt(x, y-1), pano.distanceAt(x, y+1)),
                    Math.max(pano.distanceAt(x+1, y), pano.distanceAt(x-1, y))) - pano.distanceAt(x, y);
        
    }
    
    public default ChannelPainter add(float constant){
        return (x,y)-> valueAt(x,y)+ constant;  
    }
    
    public default ChannelPainter mul(float constant){
        return (x,y)-> valueAt(x,y) * constant;  
    }
    
    public default ChannelPainter sub(float constant){
        return (x,y)-> valueAt(x,y) - constant;  
    }
   
    public default ChannelPainter div(float constant){
        return (x,y)-> valueAt(x,y)  / constant;  
    }
    
    
}
