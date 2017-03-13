package ch.epfl.alpano;

/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

import static java.util.Objects.requireNonNull;

public class PanoramaParameters {
    GeoPoint observerPosition;
    //meters
    int observerElevation;
    //radians
    double centerAzimuth;
    double horizontalFieldOfView;
    //meters
    int maxDistance;
    int width;
    int height;
    
    public PanoramaParameters(GeoPoint observerPosition, int observerElevation,
            double centerAzimuth, double horizontalFieldOfView, int maxDistance,
            int width, int height) {
        
        requireNonNull(observerPosition);
        Preconditions.checkArgument(Azimuth.isCanonical(centerAzimuth));
        Preconditions.checkArgument(horizontalFieldOfView > 0 && horizontalFieldOfView < Math2.PI2);
        Preconditions.checkArgument(maxDistance > 0);
        Preconditions.checkArgument(width > 0);
        Preconditions.checkArgument(height > 0);
        
        this.observerPosition = observerPosition;
        this.observerElevation = observerElevation;
        this.centerAzimuth = centerAzimuth;
        this.horizontalFieldOfView = horizontalFieldOfView;
        this.maxDistance = maxDistance;
        this.width = width;
        this.height = height;
    }

    /**
     * @return the observerPosition
     */
    public GeoPoint observerPosition() {
        return observerPosition;
    }

    /**
     * @return the observerElevation
     */
    public int observerElevation() {
        return observerElevation;
    }

    /**
     * @return the centerAzimuth
     */
    public double centerAzimuth() {
        return centerAzimuth;
    }

    /**
     * @return the horizontalFieldOfView
     */
    public double horizontalFieldOfView() {
        return horizontalFieldOfView;
    }

    /**
     * @return the maxDistance
     */
    public int maxDistance() {
        return maxDistance;
    }

    /**
     * @return the width
     */
    public int width() {
        return width;
    }

    /**
     * @return the height
     */
    public int height() {
        return height;
    }
    
    /**
     * @return the verticalFieldOfView
     */
    public double verticalFieldOfView() {
        double verticalFieldOfView = (height-1)/(width-1);
        verticalFieldOfView *= horizontalFieldOfView;
        
        return verticalFieldOfView;
    }
    /**
     * give the azimuth for a given pixel x
     * @param x pixel of the image
     * @return the azimuth
     */
    public double azimuthForX(double x) {
        Preconditions.checkArgument(x >= 0 && x < width);
        
        double aziPerUnit = horizontalFieldOfView/width;
        
        
        return Azimuth.canonicalize((centerAzimuth - (horizontalFieldOfView/2)) + x*aziPerUnit);
    }
    
    public double xForAzimuth(double a){
        Preconditions.checkArgument(Azimuth.isCanonical(a));
        
        double uniPerAzimuth = width/horizontalFieldOfView;
        
        double angle = a - (centerAzimuth - (horizontalFieldOfView/2));
        
        return uniPerAzimuth * angle;
    }
    
    public double altitudeForY(double y){
        Preconditions.checkArgument(y >= 0 && y < height/2 + observerElevation);
        
        double aziPerUnits = verticalFieldOfView()/height;
        
        if (y >= height/2){
            double delta = y- (height/2);
            return delta*aziPerUnits*(-1);
        }else{
            double delta = (height/2)-y;
            return delta*aziPerUnits;
        }
        
    }
}
