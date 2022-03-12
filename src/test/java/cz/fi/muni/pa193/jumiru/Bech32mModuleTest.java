package cz.fi.muni.pa193.jumiru;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
            assertThrows(IllegalArgumentException.class, () -> module.decodeBech32mString(test_vector));
        }
    }

    @Test
    public void DecodeValidStringsTest() {
        for (int i = 0; i < TestVectors.VALID_BECH32M.length; i++) {
            var decoded = module.decodeBech32mString(TestVectors.VALID_BECH32M[i]);
            assertEquals(decoded,
                    TestVectors.VALID_BECH32_DECODINGS[i]);
        }
    }
}
