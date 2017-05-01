/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.gui;

public interface PredefinedPanoramas {
    public static int mainWidth = 2500;
    public static int mainHeight = 800;
    public static int mainSuperSamp = 0;
    public static int mainMaxDist = 300;
    
    //predefined PanoramaUserParameters whose name is clear
    
    public static PanoramaUserParameters niesen 
                      = new PanoramaUserParameters(76500, 467300, 600, 180, 110,
                                                   mainMaxDist, mainWidth,
                                                   mainHeight, mainSuperSamp);
    
    public static PanoramaUserParameters alpesDuJura 
                        = new PanoramaUserParameters(68087, 470085, 1380, 162, 27,
                                                     mainMaxDist, mainWidth,
                                                     mainHeight, mainSuperSamp);
    
    public static PanoramaUserParameters montRacine 
                        = new PanoramaUserParameters(68200, 470200, 1500, 135, 45,
                                                     mainMaxDist, mainWidth,
                                                     mainHeight, mainSuperSamp);
    
    public static PanoramaUserParameters finsteraarhorn 
                        = new PanoramaUserParameters(81260, 465371, 4300, 205, 20,
                                                     mainMaxDist, mainWidth,
                                                     mainHeight, mainSuperSamp);
                        
    public static PanoramaUserParameters tourDeSauvabelin 
                        = new PanoramaUserParameters(66385, 465353, 700, 135, 100,
                                                     mainMaxDist, mainWidth,
                                                     mainHeight, mainSuperSamp);
                        
    public static PanoramaUserParameters plageDuPelican 
                        = new PanoramaUserParameters(65728, 465132, 380, 135, 60,
                                                     mainMaxDist, mainWidth,
                                                     mainHeight, mainSuperSamp);
                        
}
