package cz.fi.muni.pa193.jumiru;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

public class Bech32mModuleTest {
    private static final Bech32mModule module = new Bech32mModule();

    @Test
    public void ValidStringsTest() {
        for (String test_vector : TestVectors.VALID_BECH32M) {
            assertTrue(module.checkBech32mString(test_vector));
        }
    }

    @Test
    public void InvalidStringsTest() {
        for (String test_vector : TestVectors.INVALID_BECH32M) {
            assertFalse(module.checkBech32mString(test_vector));
        }
    }

    @Test
    public void InvalidChecksumTest() {
        for (String test_vector : TestVectors.INVALID_BECH32M) {
            assertEquals(module.decodeBech32mString(test_vector), new Bech32mData(null, null));
        }
    }
}
