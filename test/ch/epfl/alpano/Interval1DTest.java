/*
 *	Author:      Samuel Chassot (270955)
 *	Date:        23 févr. 2017
 */


package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;

public class Interval1DTest {
    
    private Interval1D inter1 = new Interval1D(1,5);
    private Interval1D inter2 = new Interval1D(2,9);
    private Interval1D inter3 = new Interval1D(-3,4);
    private Interval1D inter4 = new Interval1D(0,0);
    private Interval1D inter5 = new Interval1D(-2,2);
    private Interval1D inter6 = new Interval1D(1,5);
    
    @Test(expected = IllegalArgumentException.class)
    public void creationException(){
        new Interval1D(-2,-5);
    }
    
    @Test
    public void creation(){
        new Interval1D(1,4);
    }
    
    @Test 
    public void getterSetterWorks(){
        
        assertEquals(1, inter1.includedFrom(), 0);
        assertEquals(5, inter1.includedTo(), 0);
        
    }
    

    @Test
    public void intersectionSizeWorks() {       
        
        assertEquals(4, inter1.sizeOfIntersectionWith(inter2));
        assertEquals(4, inter1.sizeOfIntersectionWith(inter3));
        assertEquals(0, inter1.sizeOfIntersectionWith(inter4));
        assertEquals(inter1.size(), inter1.sizeOfIntersectionWith(inter1), 0);
        assertEquals(inter5.size(), inter3.sizeOfIntersectionWith(inter5), 0);
        
    }
    
    @Test
    public void boundingUnionWorks(){

        assertTrue(inter1.boundingUnion(inter2).equals(new Interval1D(1,9)));
        assertFalse(inter1.boundingUnion(inter2).equals(new Interval1D(-1,9)));
        
    }
    
    @Test
    public void equalsTest(){
        
        assertTrue(inter1.equals(inter1));
        assertFalse(inter1.equals(inter2));
        assertTrue(inter1.equals(inter6));
        
    }
}
