package ch.epfl.alpano;

/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

import static java.util.Objects.requireNonNull;

public final class PanoramaParameters {
    private GeoPoint observerPosition;
    //meters
    private int observerElevation;
    //radians
    private double centerAzimuth;
    private double horizontalFieldOfView;
    //meters
    private int maxDistance;
    private int width;
    private int height;
    
    /**
     * instantiate a panorama
     * @param observerPosition
     * @param observerElevation 
     * @param centerAzimuth
     * @param horizontalFieldOfView
     * @param maxDistance
     * @param width
     * @param height
     */
    public PanoramaParameters(GeoPoint observerPosition, int observerElevation,
            double centerAzimuth, double horizontalFieldOfView, int maxDistance,
            int width, int height) {
        
        requireNonNull(observerPosition);
        Preconditions.checkArgument(Azimuth.isCanonical(centerAzimuth));
        Preconditions.checkArgument(horizontalFieldOfView > 0 && horizontalFieldOfView <= Math2.PI2);
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
        
        double verticalFieldOfView = (double)(height-1)/(width-1);
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
        
        double aziPerUnit = horizontalFieldOfView/(width-1);
        
        
        return Azimuth.canonicalize((centerAzimuth - (horizontalFieldOfView/2)) + x*aziPerUnit);
    }

// changer ces 3 méthodes !!!
    
    
    /**
     * @param a horizontal azimuth of pixel
     * @return horizontal corresponding angle
     */
    public double xForAzimuth(double a) {
        double az = Azimuth.canonicalize(a);
        
        Preconditions.checkArgument(Math.abs(Math2.angularDistance(az, centerAzimuth)) <=
                Math.abs(Math2.angularDistance(centerAzimuth, centerAzimuth-(horizontalFieldOfView/2)))); 
        
        double uniPerAzimuth = (width-1)/horizontalFieldOfView;
        
        double angle = az - (centerAzimuth - (horizontalFieldOfView/2));
        
        return uniPerAzimuth * angle;
    }
    
    /**
     * give the azimuth for a given pixel y
     * @param y pixel of the image
     * @return the elevation
     */
    public double altitudeForY(double y) {
        Preconditions.checkArgument(y >= 0 && y < height);
        
        double aziPerUnits = verticalFieldOfView()/(height-1);

        return ((height-1)/2)*aziPerUnits -y*aziPerUnits;
        
    }
    
    /**
     * @param an elevation of pixel
     * @return vertical corresponding angle
     */
    public double yForAltitude(double a) {
        Preconditions.checkArgument(a>=(-1)*verticalFieldOfView()/2 && a<= verticalFieldOfView()/2);
        
        double unitsPerAzi = (height-1)/verticalFieldOfView();
        
        return ((height-1)/2) - a*unitsPerAzi;
        
    }
    
    /**
     * checks if horizontal index x and vertical index y are in the bounds
     * @param x horizontal index
     * @param y vertical index
     * @return boolean if yes
     */
    //visibility by defaut -> only in the package
    boolean isValidSampleIndex(int x, int y) {
        if ((x >= 0 && x < width) && (y >= 0 && y < height)) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * one-dimension index
     * @param x horizontal index
     * @param y vertical index
     * @return int
     */
  //visibility by defaut -> only in the package
    int linearSampleIndex(int x, int y) {
        return y*width + x;
    }
}
