package cz.fi.muni.pa193.jumiru;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class Bech32mModuleTest {

    @Test
    public void testValid() {
        for (String test_vector : TestVectors.VALID_BECH32M) {
            assertTrue(Bech32mModule.validateBech32mString(test_vector));
        }
    }

    @Test
    public void testInvalid() {
        for (String test_vector : TestVectors.INVALID_BECH32M) {
            assertFalse(Bech32mModule.validateBech32mString(test_vector));
        }
    }

}
