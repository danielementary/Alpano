package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;

public class Interval2DTest {
    
    private Interval1D inter1D1 = new Interval1D(1,6);
    private Interval1D inter1D2 = new Interval1D(-3,3);
    private Interval1D inter1D3 = new Interval1D(0,0);
    private Interval1D inter1D4 = new Interval1D(12,19);
    
    @Test
    public void creationTest() {
        new Interval2D(inter1D1, inter1D2);
    }
    
    @Test (expected = NullPointerException.class)
    public void creationFail(){
        new Interval2D(null, inter1D1);
    }

}
