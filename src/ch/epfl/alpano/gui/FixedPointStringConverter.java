/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.gui;

import java.math.BigDecimal;

import javafx.util.StringConverter;

public final class FixedPointStringConverter extends StringConverter<Integer> {
    
    private final int dec;
    
    /**
     * create an instance of FixedPointStringConverter
     * with the number of decimal given in argument
     * @param dec
     */
    public FixedPointStringConverter(int dec) {
        this.dec = dec;
    }
    
    @Override
    public Integer fromString(String string) {
        
        return new BigDecimal(string).movePointRight(dec)
                                     .setScale(0, BigDecimal.ROUND_HALF_UP)
                                     .intValueExact();
    }

    @Override
    public String toString(Integer i) {
        
        return i == null ? "" : new BigDecimal(i).movePointLeft(dec)
                                                 .toPlainString();
    }

}
