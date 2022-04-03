package cz.fi.muni.pa193.jumiru;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Bech32mModuleTest {
    private static final Bech32mModule module = new Bech32mModule();

    @Test
    public void CheckValidStringsTest() {
        for (String test_vector : TestVectors.VALID_BECH32M) {
            assertDoesNotThrow(() -> module.checkBech32mString(test_vector));
        }
    }

    @Test
    public void CheckInvalidStringsTest() {
        for (String test_vector : TestVectors.INVALID_BECH32M) {
            assertThrows(Bech32mException.class, () -> module.checkBech32mString(test_vector));
        }
    }

    @Test
    public void CheckInvalidChecksumTest() {
        for (String test_vector : TestVectors.INVALID_BECH32M_CHECKSUM) {
            assertThrows(Bech32mException.class, () -> module.decodeBech32mString(test_vector));
        }
    }

    @Test
    public void DecodeValidStringsTest() {
        for (int i = 0; i < TestVectors.VALID_BECH32M.length; i++) {
            assertEquals(module.decodeBech32mString(TestVectors.VALID_BECH32M[i]),
                    TestVectors.VALID_BECH32_DECODINGS[i]);
        }
    }

    @Test
    public void EncodeToValidStringsTest() {
        for (int i = 0; i < TestVectors.VALID_BECH32M.length; i++) {
            //test vectors converted to lower case as encoder must always output lower case
            assertEquals(module.encodeBech32mString(TestVectors.VALID_BECH32_DECODINGS[i]),
                    TestVectors.VALID_BECH32M[i].toLowerCase());
        }
    }
}
