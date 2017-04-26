package ch.epfl.alpano.gui;

import static org.junit.Assert.*;

import org.junit.Test;

public class FixedPointStringConverterTestSD {

    @Test
    public void fromStringWorks() {
        FixedPointStringConverter c = new FixedPointStringConverter(1);
        
        assertEquals(120, c.fromString("12"), 0);
        assertEquals(123, c.fromString("12.3"), 0);
        assertEquals(123, c.fromString("12.34"), 0);
        assertEquals(124, c.fromString("12.35"), 0);
        assertEquals(124, c.fromString("12.36789"), 0);
        
        assertEquals("67.8", c.toString(678));
     }

}
