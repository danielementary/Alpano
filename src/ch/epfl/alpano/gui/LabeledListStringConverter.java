/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.gui;

import java.util.Arrays;
import java.util.List;

import javafx.util.StringConverter;

public final class LabeledListStringConverter extends StringConverter<Integer> {
    
    private final List<String> labeledList;
    
    /**
     * create a new instance of LabeledStringConverter with the strings given in argument
     * @param labeledList
     */
    public LabeledListStringConverter(String... labeledList) {
        
        this.labeledList = Arrays.asList(labeledList);
    }

    @Override
    public Integer fromString(String s) {
        
        return labeledList.indexOf(s);
    }

    @Override
    public String toString(Integer n) {
        
        return n == null ? "" : labeledList.get(n);
    }

}
