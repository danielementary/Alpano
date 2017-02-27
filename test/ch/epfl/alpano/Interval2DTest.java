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
    public void creationWorkst() {
        new Interval2D(inter1D1, inter1D2);
    }
    
    @Test (expected = NullPointerException.class)
    public void creationFails() {
        new Interval2D(null, inter1D1);
    }
    
    @Test
    public void gettersWork() {
        Interval2D inter1 = new Interval2D(inter1D1, inter1D2);
        
        assertTrue(inter1D1.equals(inter1.iX()));
        assertTrue(inter1D2.equals(inter1.iY()));
        assertFalse(inter1D3.equals(inter1.iX()));
    }
    
    @Test
    public void containsWorks() {
        Interval2D inter1 = new Interval2D(inter1D1, inter1D2);
        
        assertTrue(inter1.contains(2, 2));
        assertTrue(inter1.contains(6, -3));
        assertFalse(inter1.contains(-4,-4));
    }
    
    @Test
    public void sizeWorks() {
        Interval2D inter1 = new Interval2D(inter1D1, inter1D2);
        Interval2D inter2 = new Interval2D(inter1D3, inter1D3);
        
        assertEquals(inter1D1.size()*inter1D2.size(), inter1.size(), 0);
        assertEquals(inter1D3.size()*inter1D3.size(), inter2.size(), 0);
    }
    
    @Test
    public void sizeOfIntersectionWithWorks() {
        Interval2D inter1 = new Interval2D(inter1D1, inter1D2);
        Interval2D inter2 = new Interval2D(inter1D3, inter1D4);
        
        assertEquals(inter1D1.sizeOfIntersectionWith(inter1D3)*inter1D2.sizeOfIntersectionWith(inter1D4), inter1.sizeOfIntersectionWith(inter2), 0);
    }
    
    @Test
    public void boundingUnionWorks() {
        Interval2D inter1 = new Interval2D(inter1D1, inter1D2);
        Interval2D inter2 = new Interval2D(inter1D3, inter1D3);
        
        Interval2D inter3 = new Interval2D(inter1D1.boundingUnion(inter1D3), inter1D2.boundingUnion(inter1D3));
        
        Interval2D inter4 = new Interval2D(inter1D1, inter1D2);
        Interval2D inter5 = new Interval2D(inter1D3, inter1D4);
        
        Interval2D inter6 = new Interval2D(inter1D1.boundingUnion(inter1D3), inter1D2.boundingUnion(inter1D4));
        
        Interval2D inter7 = new Interval2D(inter1D3, inter1D3);
        Interval2D inter8 = new Interval2D(inter1D3, inter1D3);
        
        Interval2D inter9 = new Interval2D(inter1D3.boundingUnion(inter1D3), inter1D3.boundingUnion(inter1D3));
        
        assertTrue(inter3.equals(inter1.boundingUnion(inter2)));
        assertTrue(inter6.equals(inter4.boundingUnion(inter5)));
        assertTrue(inter9.equals(inter7.boundingUnion(inter8)));
    }
    
    @Test
    public void isUnionableWithWorks() {
        Interval2D inter1 = new Interval2D(inter1D1, inter1D2);
        Interval2D inter2 = new Interval2D(inter1D3, inter1D3);
        Interval2D inter3 = new Interval2D(inter1D3, inter1D4);
        Interval2D inter4 = new Interval2D(inter1D2, inter1D2);
        Interval2D inter5 = new Interval2D(inter1D3, inter1D3);
        
        assertTrue(inter1.isUnionableWith(inter2));
        assertFalse(inter2.isUnionableWith(inter3));
        assertFalse(inter3.isUnionableWith(inter1));
        
        assertTrue(inter4.isUnionableWith(inter5));
        assertTrue(inter5.isUnionableWith(inter4));
    }
    
    @Test
    public void unionWorks() {
        Interval2D inter1 = new Interval2D(inter1D2, inter1D2);
        Interval2D inter2 = new Interval2D(inter1D3, inter1D3);
        
        Interval2D inter3 = new Interval2D(inter1D2.union(inter1D3), inter1D2.union(inter1D3));
        
        assertTrue(inter3.equals(inter1.union(inter2)));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void unionFails() {
        Interval2D inter1 = new Interval2D(inter1D1, inter1D2);
        Interval2D inter2 = new Interval2D(inter1D3, inter1D4);
        
        inter1.union(inter2);
    }
    
    @Test
    public void equalsWorks() {
        Interval2D inter1 = new Interval2D(inter1D1, inter1D2);
        Interval2D inter2 = new Interval2D(new Interval1D(1, 6), new Interval1D(-3, 3));
        Interval2D inter3 = new Interval2D(new Interval1D(1, 6), inter1D2);
        Interval2D inter4 = new Interval2D(inter1D1, new Interval1D(-3, 3));
        
        assertTrue(inter1.equals(inter2));
        assertTrue(inter2.equals(inter3));
        assertTrue(inter3.equals(inter4));
        assertTrue(inter4.equals(inter1));
    }
    
    @Test
    public void equalsFails() {
        Interval2D inter1 = new Interval2D(inter1D1, inter1D2);
        Interval2D inter2 = new Interval2D(new Interval1D(1, 6), new Interval1D(-3, 2));
        
        assertFalse(inter1.equals(inter2));
    }
    
    @Test
    public void toStringWorks() {
        Interval2D inter1 = new Interval2D(inter1D1, inter1D2);
        Interval2D inter2 = new Interval2D(inter1D3, inter1D3);
        Interval2D inter3 = new Interval2D(inter1D3, inter1D4);
        Interval2D inter4 = new Interval2D(inter1D2, inter1D2);
        Interval2D inter5 = new Interval2D(inter1D3, inter1D3);
        
        assertEquals("[1..6]x[-3..3]", inter1.toString());
        assertEquals("[0..0]x[0..0]", inter2.toString());
        assertEquals("[0..0]x[12..19]", inter3.toString());
        assertEquals("[-3..3]x[-3..3]", inter4.toString());
        assertEquals("[0..0]x[0..0]", inter5.toString());
    }
    
}
