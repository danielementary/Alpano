/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.gui;

//predefined PanoramaUserParameters whose name is clear
public interface PredefinedPanoramas {
    
    public static final int MAIN_WIDTH = 2500;
    public static final int MAIN_HEIGHT = 800;
    public static final int MAIN_SUPER_SAMP = 0;
    public static final int MAIN_MAX_DIST = 300;
    
    /**
     * return an instance of PanoramaUserParameters
     * with the parameters of the given point of vue
     */
    public static final PanoramaUserParameters NIESEN 
                        = new PanoramaUserParameters(76500, 467300, 600, 180, 110,
                                                     MAIN_MAX_DIST, MAIN_WIDTH,
                                                     MAIN_HEIGHT, MAIN_SUPER_SAMP);
    
    /**
     * return an instance of PanoramaUserParameters
     * with the parameters of the given point of vue
     */
    public static final PanoramaUserParameters ALPES_DU_JURA 
                        = new PanoramaUserParameters(68087, 470085, 1380, 162, 27,
                                                     MAIN_MAX_DIST, MAIN_WIDTH,
                                                     MAIN_HEIGHT, MAIN_SUPER_SAMP);
    
    /**
     * return an instance of PanoramaUserParameters
     * with the parameters of the given point of vue
     */
    public static final PanoramaUserParameters MONT_RACINE 
                        = new PanoramaUserParameters(68200, 470200, 1500, 135, 45,
                                                     MAIN_MAX_DIST, MAIN_WIDTH,
                                                     MAIN_HEIGHT, MAIN_SUPER_SAMP);
    
    /**
     * return an instance of PanoramaUserParameters
     * with the parameters of the given point of vue
     */
    public static final PanoramaUserParameters FINSTERAARHORN
                        = new PanoramaUserParameters(81260, 465371, 4300, 205, 20,
                                                     MAIN_MAX_DIST, MAIN_WIDTH,
                                                     MAIN_HEIGHT, MAIN_SUPER_SAMP);
    
    /**
     * return an instance of PanoramaUserParameters
     * with the parameters of the given point of vue
     */                    
    public static final PanoramaUserParameters TOUR_DE_SAUVABELIN 
                        = new PanoramaUserParameters(66385, 465353, 700, 135, 100,
                                                     MAIN_MAX_DIST, MAIN_WIDTH,
                                                     MAIN_HEIGHT, MAIN_SUPER_SAMP);
    
    /**
     * return an instance of PanoramaUserParameters
     * with the parameters of the given point of vue
     */                    
    public static final PanoramaUserParameters PLAGE_DU_PELICAN 
                        = new PanoramaUserParameters(65728, 465132, 380, 135, 60,
                                                     MAIN_MAX_DIST, MAIN_WIDTH,
                                                     MAIN_HEIGHT, MAIN_SUPER_SAMP);
    
    public static final PanoramaUserParameters BULLE 
                        = new PanoramaUserParameters(70495, 466216, 780, 150, 115,
                                                    MAIN_MAX_DIST, MAIN_WIDTH,
                                                    MAIN_HEIGHT, MAIN_SUPER_SAMP);
    
    public static final PanoramaUserParameters LA_ROCHE 
                        = new PanoramaUserParameters(71301, 466910 , 970, 220, 115,
                                                    MAIN_MAX_DIST, MAIN_WIDTH,
                                                    MAIN_HEIGHT, MAIN_SUPER_SAMP);

    public static final PanoramaUserParameters LE_JORDIL 
                        = new PanoramaUserParameters(68844, 465664, 870, 180, 60,
                                                     MAIN_MAX_DIST, MAIN_WIDTH,
                                                     MAIN_HEIGHT, MAIN_SUPER_SAMP);                        
}
