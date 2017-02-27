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

public class Interval1DTest {
    private Interval1D inter1 = new Interval1D(1,5);
    private Interval1D inter2 = new Interval1D(2,9);
    private Interval1D inter3 = new Interval1D(-3,4);
    private Interval1D inter4 = new Interval1D(0,0);
    private Interval1D inter5 = new Interval1D(-2,2);
    private Interval1D inter6 = new Interval1D(1,5);
    private Interval1D inter7 = new Interval1D(-6,-2);
    
    @Test(expected = IllegalArgumentException.class)
    public void creationException() {
        new Interval1D(-2,-5);
    }
    
    @Test
    public void creation() {
        new Interval1D(1,4);
    }
    
    @Test 
    public void gettersWork() {
        assertEquals(1, inter1.includedFrom(), 0);
        assertEquals(5, inter1.includedTo(), 0);
    }
    
    @Test
    public void sizeWorks() {
        assertEquals(5, inter1.size(), 0);
        assertEquals(8, inter2.size(), 0);
        assertEquals(8, inter3.size(), 0);
        assertEquals(1, inter4.size(), 0);
        assertEquals(5, inter5.size(), 0);
        assertEquals(5, inter6.size(), 0);
        assertEquals(5, inter7.size(), 0);
    }
    
    @Test
    public void intersectionSizeWorks() {
        assertEquals(4, inter1.sizeOfIntersectionWith(inter2));
        assertEquals(4, inter1.sizeOfIntersectionWith(inter3));
        assertEquals(0, inter1.sizeOfIntersectionWith(inter4));
        assertEquals(5, inter1.sizeOfIntersectionWith(inter1), 0);
        assertEquals(5, inter3.sizeOfIntersectionWith(inter5), 0);
    }
    
    @Test
    public void boundingUnionWorks() {
        assertTrue(inter1.boundingUnion(inter2).equals(new Interval1D(1,9)));
        assertFalse(inter1.boundingUnion(inter2).equals(new Interval1D(-1,9)));
        
        assertTrue(inter2.boundingUnion(inter1).equals(new Interval1D(1,9)));
        assertFalse(inter2.boundingUnion(inter1).equals(new Interval1D(-1,9)));

        assertTrue(inter4.boundingUnion(inter4).equals(new Interval1D(0,0)));
    }
    
    @Test
    public void equalsTest() {
        assertTrue(inter1.equals(inter1));
        assertFalse(inter1.equals(inter2));
        assertTrue(inter1.equals(inter6));
        
    }
    
    @Test
    public void toStringTest() {
        assertEquals("[1..5]", inter1.toString());
        assertEquals("[0..0]", inter4.toString());
        assertEquals("[-2..2]", inter5.toString());
    }
    
    @Test
    public void containsTest() {
        assertTrue(inter1.contains(2));
        assertTrue(inter4.contains(0));
        assertFalse(inter3.contains(1000));
    }
    
    @Test
    public void isUnionableWithTest() {
        assertTrue(inter1.isUnionableWith(inter2));
        assertTrue(inter2.isUnionableWith(inter3));
        
        assertTrue(inter3.isUnionableWith(inter5));
        assertTrue(inter5.isUnionableWith(inter3));
        
        assertTrue(inter3.isUnionableWith(inter3));
        assertTrue(inter4.isUnionableWith(inter4));
        
        assertFalse(inter1.isUnionableWith(inter7));
        
        //intervals can be unionable despite the fact 
        //that their intersection is empty
        assertTrue(inter1.isUnionableWith(inter4));
        assertTrue(inter4.isUnionableWith(inter1));
        
        Interval1D interBonus1 = new Interval1D(0,1);
        Interval1D interBonus2 = new Interval1D(3,5);
        
        assertFalse(interBonus1.isUnionableWith(interBonus2));
        assertFalse(interBonus2.isUnionableWith(interBonus1));
    }
    
    @Test
    public void unionTest() {
        assertTrue((inter1.union(inter2)).equals(new Interval1D(1,9)));
        assertTrue((inter2.union(inter3)).equals(new Interval1D(-3,9)));

        assertTrue((inter2.union(inter2)).equals(inter2));
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void unionErrorTest() {
        inter1.union(inter7);
    }
    
}
