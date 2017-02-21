package ch.epfl.alpano;

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
    
    
}
