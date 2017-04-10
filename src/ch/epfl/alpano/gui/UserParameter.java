/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.gui;

public enum UserParameter {

    OBSERVER_LONGITUDE(6,12),
    OBSERVER_LATITUDE(45, 48),
    OBSERVER_ELEVATION(300, 10_000),
    CENTER_AZIMUTH(0, 359),
    HORIZONTAL_FIELD_OF_VIEW(1,360),
    MAX_DISTANCE(10,600),
    WIDTH(30, 16_000),
    HEIGHT(10, 4_000),
    SUPER_SAMPLING_EXPONENT(0,2);

    private int min;
    private int max;

    private UserParameter(int min, int max) {
        this.min = min;     
        this.max = max;
    }
    
    public int sanitize(int value) {
        if (value > max) {
            return max;
        } else if (value < min) {
            return min;
        }
        
        return value;
    }
}
