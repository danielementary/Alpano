/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano;

import static java.util.Objects.requireNonNull;

public final class PanoramaParameters {
    
    private GeoPoint observerPosition;
    
    private int observerElevation;
    private double centerAzimuth;
    private double horizontalFieldOfView;
    private int maxDistance;
    private int width;
    private int height;
    
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
            double centerAzimuth, double horizontalFieldOfView, int maxDistance,
            int width, int height) {
        
        requireNonNull(observerPosition);
        Preconditions.checkArgument(Azimuth.isCanonical(centerAzimuth),
                                                    "azimuth not canonical");
        
        Preconditions.checkArgument(horizontalFieldOfView > 0
                                    && horizontalFieldOfView <= Math2.PI2,
                                    "HFOV not in [0, 2pi]");
        
        Preconditions.checkArgument(maxDistance > 0, "maxDistance < 0");
        Preconditions.checkArgument(width > 0, "width < 0");
        Preconditions.checkArgument(height > 0, "height < 0");
        
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
     * the size of the observer's field of view in vertical 
     * (depending of the height and the 
     * horizontal field of view) in radians
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

    /**
     * @param a horizontal azimuth of pixel
     * @return horizontal corresponding angle
     */
    public double xForAzimuth(double a) {
        
        double az = Azimuth.canonicalize(a);
        
        Preconditions.checkArgument(Math.abs(Math2.angularDistance(az, centerAzimuth))
                <= Math.abs(Math2.angularDistance(centerAzimuth, 
                                    centerAzimuth-(horizontalFieldOfView/2)))); 
        
        double uniPerAzimuth = (width-1)/horizontalFieldOfView;
        
        double angle = (centerAzimuth - (horizontalFieldOfView/2));
        
        return uniPerAzimuth * Azimuth.canonicalize(Math2.angularDistance(angle, az));
    }
    
    /**
     * give the azimuth for a given pixel y
     * @param y pixel of the image
     * @return the elevation
     */
    public double altitudeForY(double y) {
        Preconditions.checkArgument(y >= 0 && y < height);
        
        double aziPerUnits = verticalFieldOfView()/(height-1);

        return verticalFieldOfView()/2 -y*aziPerUnits;
        
    }
    
    /**
     * @param an elevation of pixel
     * @return vertical corresponding angle
     */
    public double yForAltitude(double a) {
        Preconditions.checkArgument(a>=(-1)*verticalFieldOfView()/2 
                                            && a<= verticalFieldOfView()/2);
        
        double unitsPerAzi = (height-1)/verticalFieldOfView();
        
        return ((verticalFieldOfView()/2)-a)*unitsPerAzi;
        
    }
    
    /**
     * checks if horizontal index x and vertical index y are in the bounds
     * @param x horizontal index
     * @param y vertical index
     * @return boolean if yes
     */
    //visibility by defaut -> only in the package
    boolean isValidSampleIndex(int x, int y) {
        return (x >= 0 && x < width) && (y >= 0 && y < height); 
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
