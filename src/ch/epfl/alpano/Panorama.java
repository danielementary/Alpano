/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;

public final class Panorama {
    
    private PanoramaParameters parameters;

    private final float[] distance, longitude, latitude, elevation , slope;

    /**
     * instantiate a panorama with arrays of all infos listed below
     * it is private to guarantee that it is only manipulated from the builder
     * and maintain privacy and integrity of the arrays and the no-need of
     * copying them
     * @param parameters
     * @param distance
     * @param longitude
     * @param latitude
     * @param elevation
     * @param slope
     */
    private Panorama(PanoramaParameters parameters, float[] distance,
                     float[] longitude, float[] latitude, float[] elevation,
                     float[] slope) {
        
        this.parameters = parameters;
        this.distance = distance;
        this.longitude = longitude;
        this.latitude = latitude;
        this.elevation = elevation;
        this.slope = slope;
    }
    
    /**
     * gives the parameters
     * @return the parameters in a PanoramaParameters instance
     */
    public PanoramaParameters parameters() {
        return parameters;
    }
    
    
    /**
     * evaluate the index with a linearSampledIndex(x,y) 
     * and return the corresponding value in the array
     * @param x
     * @param y
     * @return the for x,y or throw IndexOutOfBoundException 
     * if x,y are not in the array's bounds
     */
    public float distanceAt(int x, int y) {
    	return distance[requireValidIndex(x,y)];
    }
    
    /**
     * evaluate the index with a linearSampledIndex(x,y) 
     * and return the corresponding value in the array
     * @param x
     * @param y
     * @param d what is returned if x,y are not in the array's bounds
     * @return the distance for x,y
     */
    public float distanceAt(int x, int y, float d) {
        if (!parameters.isValidSampleIndex(x,y)) {
            return d;
        }
        
        int index = parameters.linearSampleIndex(x, y);
        
        return distance[index];
    }
    
    /**
     * evaluate the index with a linearSampledIndex(x,y) 
     * and return the corresponding value in the array
     * @param x
     * @param y
     * @return the longitude for x,y 
     */
    public float longitudeAt(int x, int y) {
    	return longitude[requireValidIndex(x,y)];
    }

    /**
     * evaluate the index with a linearSampledIndex(x,y)
     * and return the corresponding value in the array
     * @param x
     * @param y
     * @return the latitude for x,y 
     */
    public float latitudeAt(int x, int y) {
    	return latitude[requireValidIndex(x,y)];
    }

    /**
     * evaluate the index with a linearSampledIndex(x,y)
     * and return the corresponding value in the array
     * @param x
     * @param y
     * @return the elevation for x,y 
     */
    public float elevationAt(int x, int y) {
    	return elevation[requireValidIndex(x,y)];
    }

    /**
     * evaluate the index with a linearSampledIndex(x,y) 
     * and return the corresponding value in the array
     * @param x
     * @param y
     * @return the slope for x,y 
     */
    public float slopeAt(int x, int y) {
    	return slope[requireValidIndex(x,y)];
    }

    public static final class Builder {
        
        private final PanoramaParameters parameters;
        private final float[] distance, longitude, latitude, elevation, slope;
        private boolean flag = false;
        
        /**
         * create a Builder instance to build a parorama
         * @param parameters
         */
        public Builder(PanoramaParameters parameters) {
         
            this.parameters = requireNonNull(parameters);
            int listsLength = parameters.width()*parameters.height();
            
            distance = new float[listsLength];
            longitude = new float[listsLength];
            latitude = new float[listsLength];
            elevation = new float[listsLength];
            slope = new float[listsLength];
            
            Arrays.fill(distance, Float.POSITIVE_INFINITY);
        }
        
        /*
         * the following methods, which fill the arrays,
         * only act if the <flag> is false.
         * <flag> becomes true when we call the build() method
         */
        /**
         * put distance in the array for position x,y in linearIndex 
         * if flag is false
         * @param x
         * @param y
         * @param distance
         * @return the builder with distance in the array
         */
        public Builder setDistanceAt(int x, int y, float distance) {
        	this.distance[requireValidIndex(x,y)] = distance;

        	return this;
        }
        
        /**
         * put longitude in the array for position x,y in linearIndex 
         * if flag is false
         * @param x
         * @param y
         * @param longitude
         * @return the builder with longitude in the array
         */
        public Builder setLongitudeAt(int x, int y, float longitude) {
        	this.longitude[requireValidIndex(x,y)] = longitude;

        	return this;
        }
        
        /**
         * put latitude in the array for position x,y in linearIndex 
         * if flag is false
         * @param x
         * @param y
         * @param latitude
         * @return the builder with latitude in the array
         */
        public Builder setLatitudeAt(int x, int y, float latitude) {
        	this.latitude[requireValidIndex(x,y)] = latitude;

        	return this;
        }
        
        /**
         * put elevation in the array for position x,y in linearIndex 
         * if flag is false
         * @param x
         * @param y
         * @param elevation
         * @return the builder with elevation in the array
         */
        public Builder setElevationAt(int x, int y, float elevation) {
        	this.elevation[requireValidIndex(x,y)] = elevation;

        	return this;
        }
        
        /**
         * put slope in the array for position x,y in linearIndex 
         * if flag is false
         * @param x
         * @param y
         * @param slope
         * @return the builder with slope in the array
         */
        public Builder setSlopeAt(int x, int y, float slope) {
        	this.slope[requireValidIndex(x,y)] = slope;

        	return this;
        }
        
        /**
         * create a panorama instance 
         * with the attributs of the builder (can be done only one time)
         * @return a panorama instance
         */
        public Panorama build() {
            if (flag) {
                throw new IllegalStateException();
            }
            
            flag = true;
            
            return new Panorama(parameters, distance, longitude, 
                                latitude, elevation, slope);
        }  
        
        private int requireValidIndex(int x, int y) {
            if (flag) {
                throw new IllegalStateException();
            }
            
            if (!parameters.isValidSampleIndex(x, y)) {
                throw new IndexOutOfBoundsException();
            }
            
            return parameters.linearSampleIndex(x, y);
        }
        
    }
    
    private int requireValidIndex(int x, int y) {
        if (!parameters.isValidSampleIndex(x,y)) {
            throw new IndexOutOfBoundsException();
        }
        
        return parameters.linearSampleIndex(x, y);
    }
}