package ch.epfl.alpano;

import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;


public class PanoramaComputer {
    
    ContinuousElevationModel dem;
    
    PanoramaComputer(ContinuousElevationModel dem){
        if(dem == null){
            throw new NullPointerException();
        }
        this.dem = dem;
    }
    
    public Panorama computePanorama(PanoramaParameters parameters){
        Panorama.Builder builder = new Panorama.Builder(parameters);
        
        
    }
    
    public static DoubleUnaryOperator rayToGroundDistance(ElevationProfile profile, double ray0, double raySlope){
        return x -> {
            return ray0 + x*raySlope - profile.elevationAt(x) + ((1-0.13)/2*Distance.EARTH_RADIUS)*Math2.sq(x);
        };
       
    }
    
    

}
