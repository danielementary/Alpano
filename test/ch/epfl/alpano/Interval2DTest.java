package ch.epfl.alpano;

/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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
    
    @Test
    public void gettersTest(){
        Interval2D inter1 = new Interval2D(inter1D1, inter1D2);
        
        assertTrue(inter1D1.equals(inter1.iX()));
        assertTrue(inter1D2.equals(inter1.iY()));
        assertFalse(inter1D3.equals(inter1.iX()));
    }
    
    @Test
    public void containsWorks(){
        Interval2D inter1 = new Interval2D(inter1D1, inter1D2);
        
        assertTrue(inter1.contains(2, 2));
        assertTrue(inter1.contains(6, -3));
        assertFalse(inter1.contains(-4,-4));
    }
    
    @Test
    public void sizeTest(){
        Interval2D inter1 = new Interval2D(inter1D1, inter1D2);
        Interval2D inter2 = new Interval2D(inter1D3, inter1D3);
        
        assertEquals(inter1D1.size()*inter1D2.size(), inter1.size(), 0);
        assertEquals(inter1D3.size()*inter1D3.size(), inter2.size(), 0);
    }
    
    @Test
    public void sizeOfIntersectionWith(){
        Interval2D inter1 = new Interval2D(inter1D1, inter1D2);
        Interval2D inter2 = new Interval2D(inter1D3, inter1D4);
        
        assertEquals(inter1D1.sizeOfIntersectionWith(inter1D3)*inter1D2.sizeOfIntersectionWith(inter1D4), inter1.sizeOfIntersectionWith(inter2), 0);
    }

}
