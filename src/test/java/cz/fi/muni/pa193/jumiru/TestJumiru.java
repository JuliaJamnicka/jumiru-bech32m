package cz.fi.muni.pa193.jumiru;

import cz.fi.muni.pa193.jumiru.Bech32mModule;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class TestJumiru {

    @Test
    public void testValid() {
        for (String test_vector : TestVector.VALID_BECH32M) {
            assertTrue(Bech32mModule.validateBech32mString(test_vector));
        }
    }

    @Test
    public void testInvalid() {
        for (String test_vector : TestVector.INVALID_BECH32M) {
            assertFalse(Bech32mModule.validateBech32mString(test_vector));
        }
    }

}
