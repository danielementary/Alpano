

package ch.epfl.alpano;

import java.util.Arrays;

/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

public final class Panorama {

    private PanoramaParameters parameters;

    private float[] distance;
    private float[] longitude;
    private float[] latitude;
    private float[] elevation;
    private float[] slope;


    public Panorama(PanoramaParameters parameters, float[] distance,
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


    public float distanceAt(int x, int y){
    	if(!parameters.isValidSampleIndex(x,y)){
    		throw new IndexOutOfBoundsException();
    	}
        int index = parameters.linearSampleIndex(x, y);
        return distance[index];


    }public float longitudeAt(int x, int y){
    	if(!parameters.isValidSampleIndex(x,y)){
    		throw new IndexOutOfBoundsException();
    	}
        int index = parameters.linearSampleIndex(x, y);
        return longitude[index];
    }

    public float latitudeAt(int x, int y){
    	if(!parameters.isValidSampleIndex(x,y)){
    		throw new IndexOutOfBoundsException();
    	}
        int index = parameters.linearSampleIndex(x, y);
        return latitude[index];
    }

    public float elevationAt(int x, int y){
    	if(!parameters.isValidSampleIndex(x,y)){
    		throw new IndexOutOfBoundsException();
    	}
        int index = parameters.linearSampleIndex(x, y);
        return elevation[index];
    }

    public float slopeAt(int x, int y){
    	if(!parameters.isValidSampleIndex(x,y)){
    		throw new IndexOutOfBoundsException();
    	}
        int index = parameters.linearSampleIndex(x, y);
        return slope[index];
    }
    
    public float distanceAt(int x, int y, float d){
        if(!parameters.isValidSampleIndex(x,y)){
            return d;
        }
        int index = parameters.linearSampleIndex(x, y);
        return distance[index];
    }
    
    
    
    public static final class Builder{
        
        private PanoramaParameters parameters;
        private float[] distance;
        private float[] longitude;
        private float[] latitude;
        private float[] elevation;
        private float[] slope;

        private boolean FLAG = false;
        

        public Builder(PanoramaParameters parameters) {
            
            Preconditions.checkArgument(parameters != null);
            this.parameters = parameters;
            
            distance = new float[parameters.width()*parameters.height()];
            longitude = new float[parameters.width()*parameters.height()];
            latitude = new float[parameters.width()*parameters.height()];
            elevation = new float[parameters.width()*parameters.height()];
            slope = new float[parameters.width()*parameters.height()];


            
            Arrays.fill(distance, Float.POSITIVE_INFINITY);
                                
        }
        
        public Builder setDistanceAt(int x, int y, float distance){
        	if(FLAG){
        		throw new IllegalStateException();
        	}
        	this.distance[parameters.linearSampleIndex(x,y)] = distance;

        	return this;
        }
        public Builder setLongitudeAt(int x, int y, float longitude){
        	if(FLAG){
        		throw new IllegalStateException();
        	}
        	this.longitude[parameters.linearSampleIndex(x,y)] = longitude;

        	return this;
        }
        public Builder setLatitudeAt(int x, int y, float latitude){
        	if(FLAG){
        		throw new IllegalStateException();
        	}
        	this.latitude[parameters.linearSampleIndex(x,y)] = latitude;

        	return this;
        }
        public Builder setElevationAt(int x, int y, float elevation){
        	if(FLAG){
        		throw new IllegalStateException();
        	}
        	this.elevation[parameters.linearSampleIndex(x,y)] = elevation;

        	return this;
        }
        public Builder setSlopeAt(int x, int y, float slope){
        	if(FLAG){
        		throw new IllegalStateException();
        	}
        	this.slope[parameters.linearSampleIndex(x,y)] = slope;

        	return this;
        }
        
        public Panorama build(){
            if (FLAG){
                throw new IllegalStateException();
            }
            FLAG = true;
            return new Panorama(parameters, distance, longitude, latitude, elevation, slope);
        }
        
        
        
    }
}
