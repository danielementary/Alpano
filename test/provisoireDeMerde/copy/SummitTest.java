package provisoireDeMerde.copy;

import static java.lang.Math.toRadians;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.summit.Summit;

public class SummitTest {
    @Test(expected = NullPointerException.class)
    public void constructorFailsWithNullName() {
        new Summit(null, new GeoPoint(0, 0), 1);
    }

    @Test(expected = NullPointerException.class)
    public void constructorFailsWithNullPosition() {
        new Summit("sommet", null, 1);
    }

    @Test
    public void nameReturnsName() {
        String n = "sommet";
        Summit s = new Summit(n, new GeoPoint(0,0), 1);
        assertEquals(n, s.name());
    }

    @Test
    public void positionReturnsPosition() {
        GeoPoint p = new GeoPoint(toRadians(3), toRadians(5));
        Summit s = new Summit("sommet", p, 1);
        assertEquals(p, s.position());
    }

    @Test
    public void elevationReturnsElevation() {
        int e = 1234;
        Summit s = new Summit("sommet", new GeoPoint(0, 0), e);
        assertEquals(e, s.elevation());
    }
}
