package ch.epfl.alpano;

/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

import org.junit.Test;

import static ch.epfl.alpano.Preconditions.checkArgument;
import static ch.epfl.alpano.Preconditions.checkArgumentNullPointer;

public class PreconditionsTest {
    //checkArgument (1 argument)
    @Test
    public void checkArgument1SucceedsForTrue() {
        checkArgument(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkArgument1ThrowsForFalse() {
        checkArgument(false);
    }

    //checkArgument (2 arguments)
    @Test
    public void checkArgument2SucceedsForTrue() {
        checkArgument(true, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkArgument2ThrowsForFalse() {
        checkArgument(false, "");
    }
    
    //checkArgumentNullPointer (1 argument)
    @Test
    public void checkArgumentNullPointer1SucceedsForTrue() {
        checkArgumentNullPointer(true);
    }

    @Test(expected = NullPointerException.class)
    public void checkArgumentNullPointer1ThrowsForFalse() {
        checkArgumentNullPointer(false);
    }

    //checkArgumentNullPointer (2 arguments)
    @Test
    public void checkArgumentNullPointer2SucceedsForTrue() {
        checkArgumentNullPointer(true, "");
    }

    @Test(expected = NullPointerException.class)
    public void checkArgumentNullPointer2ThrowsForFalse() {
        checkArgumentNullPointer(false, "");
    }
    
}
