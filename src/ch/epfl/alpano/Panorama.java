package ch.epfl.alpano;

/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

import java.util.Arrays;
import static java.util.Objects.requireNonNull;

public final class Panorama {

    private PanoramaParameters parameters;

    private float[] distance;
    private float[] longitude;
    private float[] latitude;
    private float[] elevation;
    private float[] slope;

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
    
    public PanoramaParameters parameters() {
        return parameters;
    }
    
    /*
     * the following methods evaluate the index with a
     * linearSampledIndex(x,y) and return the corresponding
     * value 
     */
    public float distanceAt(int x, int y) {
    	if(!parameters.isValidSampleIndex(x,y)) {
    		throw new IndexOutOfBoundsException();
    	}
    	
        int index = parameters.linearSampleIndex(x, y);
        
        return distance[index];
    }
    
    public float distanceAt(int x, int y, float d) {
        if(!parameters.isValidSampleIndex(x,y)) {
            return d;
        }
        
        int index = parameters.linearSampleIndex(x, y);
        
        return distance[index];
    }
    
    public float longitudeAt(int x, int y) {
    	if(!parameters.isValidSampleIndex(x,y)) {
    		throw new IndexOutOfBoundsException();
    	}
    	
        int index = parameters.linearSampleIndex(x, y);
        
        return longitude[index];
    }

    public float latitudeAt(int x, int y) {
    	if(!parameters.isValidSampleIndex(x,y)) {
    		throw new IndexOutOfBoundsException();
    	}
    	
        int index = parameters.linearSampleIndex(x, y);
        
        return latitude[index];
    }

    public float elevationAt(int x, int y) {
    	if(!parameters.isValidSampleIndex(x,y)) {
    		throw new IndexOutOfBoundsException();
    	}
        
    	int index = parameters.linearSampleIndex(x, y);
        
    	return elevation[index];
    }

    public float slopeAt(int x, int y) {
    	if(!parameters.isValidSampleIndex(x,y)) {
    		throw new IndexOutOfBoundsException();
    	}
        
    	int index = parameters.linearSampleIndex(x, y);
        
    	return slope[index];
    }

    public static final class Builder {
        
        private PanoramaParameters parameters;
        private float[] distance;
        private float[] longitude;
        private float[] latitude;
        private float[] elevation;
        private float[] slope;

        private boolean flag = false;
        
        public Builder(PanoramaParameters parameters) {
            
            requireNonNull(parameters);
            this.parameters = parameters;
            
            distance = new float[parameters.width()*parameters.height()];
            longitude = new float[parameters.width()*parameters.height()];
            latitude = new float[parameters.width()*parameters.height()];
            elevation = new float[parameters.width()*parameters.height()];
            slope = new float[parameters.width()*parameters.height()];
            
            Arrays.fill(distance, Float.POSITIVE_INFINITY);
        }
        
        /*
         * the following methods, which fill the arrays,
         * only act if the <flag> is false.
         * <flag> becomes true when we call the build() method
         */
        public Builder setDistanceAt(int x, int y, float distance) {
        	if(flag) {
        		throw new IllegalStateException();
        	}
        	
        	this.distance[parameters.linearSampleIndex(x,y)] = distance;

        	return this;
        }
        
        public Builder setLongitudeAt(int x, int y, float longitude) {
        	if(flag) {
        		throw new IllegalStateException();
        	}
        	
        	this.longitude[parameters.linearSampleIndex(x,y)] = longitude;

        	return this;
        }
        
        public Builder setLatitudeAt(int x, int y, float latitude) {
        	if(flag) {
        		throw new IllegalStateException();
        	}
        	
        	this.latitude[parameters.linearSampleIndex(x,y)] = latitude;

        	return this;
        }
        
        public Builder setElevationAt(int x, int y, float elevation) {
        	if(flag) {
        		throw new IllegalStateException();
        	}
        	
        	this.elevation[parameters.linearSampleIndex(x,y)] = elevation;

        	return this;
        }
        
        public Builder setSlopeAt(int x, int y, float slope) {
        	if(flag) {
        		throw new IllegalStateException();
        	}
        	
        	this.slope[parameters.linearSampleIndex(x,y)] = slope;

        	return this;
        }
        
        public Panorama build() {
            if (flag) {
                throw new IllegalStateException();
            }
            
            flag = true;
            
            return new Panorama(parameters, distance, longitude, latitude, elevation, slope);
        }  
    }
}