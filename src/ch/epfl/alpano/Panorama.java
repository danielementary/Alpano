

package ch.epfl.alpano;

/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

public final class Panorama {

    PanoramaParameters parameters;
    float[] distance;
    float[] longitude;
    float[] latitude;
    float[] altitude;
    float[] slope;


    public Panorama(PanoramaParameters parameters, float[] distance,
            float[] longitude, float[] latitude, float[] altitude,
            float[] slope) {
        
        this.parameters = parameters;
        this.distance = distance;
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
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

    public float altitudeAt(int x, int y){
    	if(!parameters.isValidSampleIndex(x,y)){
    		throw new IndexOutOfBoundsException();
    	}
        int index = parameters.linearSampleIndex(x, y);
        return altitude[index];
    }

    public float slopeAt(int x, int y){
    	if(!parameters.isValidSampleIndex(x,y)){
    		throw new IndexOutOfBoundsException();
    	}
        int index = parameters.linearSampleIndex(x, y);
        return slope[index];
    }
}
