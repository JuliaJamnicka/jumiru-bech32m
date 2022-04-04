package cz.fi.muni.pa193.jumiru.bech32m;

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
            assertThrows(Bech32mInvalidChecksumException.class, () -> module
                    .decodeBech32mString(test_vector, false));
        }
    }

    @Test
    public void ErrorCorrectionTest() {
        for (int i = 0; i < TestVectors.INVALID_CORRECTS_TO_VALID_BECH32M.length; i++) {
            int finalI = i;
            Exception thrownException = assertThrows(Bech32mInvalidChecksumException.class, () -> module
                    .decodeBech32mString(TestVectors.INVALID_CORRECTS_TO_VALID_BECH32M[finalI], true));
            assertEquals("Invalid checksum. Error correction found these candidates for " +
                    "corrected original string: \n" + TestVectors.VALID_BECH32M[finalI].toLowerCase(),
                    thrownException.getMessage());
        }
    }

    @Test
    public void DecodeValidStringsTest() {
        for (int i = 0; i < TestVectors.VALID_BECH32M.length; i++) {
            assertEquals(module.decodeBech32mString(TestVectors.VALID_BECH32M[i], false),
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
