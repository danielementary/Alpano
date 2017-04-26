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
    
    public FixedPointStringConverter(int dec) {
        this.dec = dec;
    }
    
    @Override
    public Integer fromString(String string) {
        return new BigDecimal(string).movePointRight(dec)
                                     .setScale(0, BigDecimal.ROUND_HALF_UP)
                                     .intValue();
    }

    @Override
    public String toString(Integer i) {
        return new BigDecimal(i).movePointLeft(dec)
                                .toPlainString();
    }

}
