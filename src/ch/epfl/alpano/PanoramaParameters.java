/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano;

public final class PanoramaParameters {
    
    private final GeoPoint observerPosition;
    private final double centerAzimuth, horizontalFieldOfView, verticalFieldOfView;
    private final int observerElevation, maxDistance, width, height;
    
    /**
     * instantiate a panorama
     * @param observerPosition
     * @param observerElevation 
     * @param centerAzimuth direction of the look
     * @param horizontalFieldOfView in radian : the size of the observer's field of view
     * @param maxDistance the max distance the observer see
     * @param width of the panorama in pixels
     * @param height of the panorama in pixels
     */
    public PanoramaParameters(GeoPoint observerPosition, int observerElevation,
                              double centerAzimuth, double horizontalFieldOfView,
                              int maxDistance, int width, int height) {
        
        this.observerPosition = observerPosition;
        this.observerElevation = observerElevation;
        this.centerAzimuth = centerAzimuth;
        this.horizontalFieldOfView = horizontalFieldOfView;
        this.maxDistance = maxDistance;
        this.width = width;
        this.height = height;
        this.verticalFieldOfView = ((double)(height-1)/(width-1))*horizontalFieldOfView;
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
     * the size of the observer's field of view in vertical 
     * (depending of the height and the 
     * horizontal field of view) in radians
     */
    public double verticalFieldOfView() {
        return verticalFieldOfView;
    }
    
    /**
     * give the azimuth for a given pixel x
     * @param x pixel of the image
     * @return the azimuth
     */
    public double azimuthForX(double x) {
        
        double aziPerUnit = horizontalFieldOfView()/(width()-1);
        
        return Azimuth.canonicalize((centerAzimuth()-(horizontalFieldOfView()/2))+x*aziPerUnit);
    }

    /**
     * @param a horizontal azimuth of pixel
     * @return horizontal corresponding angle
     */
    public double xForAzimuth(double a) {
        
        double az = Azimuth.canonicalize(a);
        double hFOVOverT = horizontalFieldOfView()/2;
        double uniPerAzimuth = (width-1)/horizontalFieldOfView;
        double angle = (centerAzimuth-hFOVOverT);
        
        return uniPerAzimuth*Azimuth.canonicalize(Math2.angularDistance(angle, az));
    }
    
    /**
     * give the azimuth for a given pixel y
     * @param y pixel of the image
     * @return the elevation
     */
    public double altitudeForY(double y) {
        double aziPerUnits = verticalFieldOfView()/(height-1);

        return verticalFieldOfView()/2 -y*aziPerUnits;  
    }
    
    /**
     * @param an elevation of pixel
     * @return vertical corresponding angle
     */
    public double yForAltitude(double a) {
        
        double vFOVOverT = verticalFieldOfView()/2;
        double unitsPerAzi = (height-1)/verticalFieldOfView();
        
        return (vFOVOverT-a)*unitsPerAzi;        
    }
    
    /**
     * checks if horizontal index x and vertical index y are in the bounds
     * @param x horizontal index
     * @param y vertical index
     * @return boolean if yes
     */
    //visibility by defaut -> only in the package
    boolean isValidSampleIndex(int x, int y) {
        return (x >= 0 && x < width()) && (y >= 0 && y < height()); 
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
