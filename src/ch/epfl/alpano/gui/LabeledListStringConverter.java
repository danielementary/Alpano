/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.gui;

import javafx.util.StringConverter;

public final class LabeledListStringConverter extends StringConverter<Integer> {
    
    private final String[] labeledList;
    
    public LabeledListStringConverter(String... labeledList) {
        this.labeledList = labeledList;
    }

    @Override
    public Integer fromString(String s) {
        
        for (int i = 0; i < labeledList.length; ++i) {
            if (labeledList[i].equals(s)) {
                return i;
            }
        }
        
        return -1;
    }

    @Override
    public String toString(Integer n) {
        return labeledList[n];
    }

}
