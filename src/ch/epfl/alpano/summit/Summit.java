package ch.epfl.alpano.summit;

/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

import ch.epfl.alpano.GeoPoint;

public final class Summit {
    
    private String name;
    private GeoPoint position;
    private int elevation;
    
    public Summit(String name, GeoPoint position, int elevation) {
        this.name = name;
        this.position = position;
        this.elevation = elevation;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the position
     */
    public GeoPoint getPosition() {
        return position;
    }

    /**
     * @return the elevation
     */
    public int getElevation() {
        return elevation;
    }
    
    @Override
    public String toString(){
        return String.format(name + " " + position.toString() + " " + elevation);
    }
}
