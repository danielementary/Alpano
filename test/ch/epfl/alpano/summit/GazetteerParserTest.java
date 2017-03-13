/*
 *	Author:      Samuel Chassot (270955)
 *	Date:        13 mars 2017
 */


package ch.epfl.alpano.summit;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

public class GazetteerParserTest{

    File file = new File("res/alps.txt");
    @Test
    public void test() throws IOException{
        
        List<Summit> list = GazetteerParser.readSummitsFrom(file);
        assertEquals("MONTE CURT (7.4200,45.1403) 1325", list.get(0).toString());
    }

}
