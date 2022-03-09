import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestJumiru {
    @Test
    public void testValid(){
        assertTrue(true);
        assertEquals("A1LQFN3A", TestVector.VALID_BECH32M[0]);
        assertEquals("A1LQFN3Q", TestVector.VALID_BECH32M[0]);
    }
}
