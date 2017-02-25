package ch.epfl.alpano;

/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

public interface Preconditions {
    
    /**
     * throw an IllegalArgumentExpection without an message if b is false 
     * @param b condition boolean
     */
    public static void checkArgument(boolean b) {
        if (!b) {
            throw new IllegalArgumentException();
        }
    }
    
    /**
     * throw an IllegalArgumentException with message if b is false
     * @param b condition boolean
     * @param message message string
     */
    public static void checkArgument(boolean b, String message) {
        if (!b) {
            throw new IllegalArgumentException(message);
        }
    }
    
    /**
     * throw an NullPointerExpection without a message if b is false 
     * @param b condition boolean
     */
    public static void checkArgumentNullPointer(boolean b) {
        if (!b) {
            throw new NullPointerException();
        }
    }
    
    /**
     * throw an NullPointerExpection with a message if b is false 
     * @param b condition boolean
     */
    public static void checkArgumentNullPointer(boolean b, String message) {
        if (!b) {
            throw new NullPointerException(message);
        }
    }
    
}
