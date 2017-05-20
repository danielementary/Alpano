package ch.epfl.alpano.gui;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UserParameterTestSD {

    @Test
    public void enumSanitizeWorksOnTrivialCase() {
        assertEquals(500, UserParameter.MAX_DISTANCE.sanitize(500), 0);
    }
    
    @Test
    public void enumSanitizeWorksOnUnderboundCase() {
        assertEquals(10, UserParameter.MAX_DISTANCE.sanitize(0), 0);
    }
    
    @Test
    public void enumSanitizeWorksOnOverboundCase() {
        assertEquals(600, UserParameter.MAX_DISTANCE.sanitize(1000), 0);
    }

}
