package ch.epfl.alpano.gui;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LabeledListStringConverterTestSD {

    @Test
    public void fromStringWorks() {
        LabeledListStringConverter c = new LabeledListStringConverter("zéro", "un", "deux");
        
        assertEquals(2, c.fromString("deux"), 0);
     }
    
    @Test
    public void toStringWorks() {
        LabeledListStringConverter c = new LabeledListStringConverter("zéro", "un", "deux");
        
        assertEquals("zéro", c.toString(0));
     }

}
