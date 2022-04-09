package cz.fi.muni.pa193.jumiru.bech32m;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class Bech32mModuleTest {
    private static final Bech32mModule module = new Bech32mModule();

    @Test
    void checkValidStringsTest() {
        for (String testVector : TestVectors.VALID_BECH32M) {
            assertDoesNotThrow(() -> module.checkBech32mString(testVector));
        }
    }

    @Test
    void checkInvalidStringsTest() {
        for (String testVector : TestVectors.INVALID_BECH32M) {
            assertThrows(Bech32mException.class, () -> module.checkBech32mString(testVector));
        }
    }

    @Test
    void checkInvalidChecksumTest() {
        for (String testVector : TestVectors.INVALID_BECH32M_CHECKSUM) {
            assertThrows(Bech32mInvalidChecksumException.class, () -> module
                    .decodeBech32mString(testVector, false));
        }
    }

    @Test
    void errorCorrectionTest() {
        for (int i = 0; i < TestVectors.INVALID_CORRECTS_TO_VALID_BECH32M.length; i++) {
            int finalI = i;
            Exception thrownException = assertThrows(Bech32mInvalidChecksumException.class, () -> module
                    .decodeBech32mString(TestVectors.INVALID_CORRECTS_TO_VALID_BECH32M[finalI], true));
            assertEquals("Provided bech32m string has incorrect checksum. Error correction found " +
                            "these candidates for corrected original string: \n" + TestVectors
                            .VALID_CORRECTED_BECH32M[finalI].toLowerCase(Locale.ENGLISH),
                    thrownException.getMessage());
        }
    }

    @Test
    void decodeValidStringsTest() {
        for (int i = 0; i < TestVectors.VALID_BECH32M.length; i++) {
            assertEquals(module.decodeBech32mString(TestVectors.VALID_BECH32M[i], false),
                    TestVectors.VALID_BECH32_DECODINGS[i]);
        }
    }

    @Test
    void encodeToValidStringsTest() {
        for (int i = 0; i < TestVectors.VALID_BECH32M.length; i++) {
            //test vectors converted to lower case as encoder must always output lower case
            assertEquals(module.encodeBech32mString(TestVectors.VALID_BECH32_DECODINGS[i]),
                    TestVectors.VALID_BECH32M[i].toLowerCase(Locale.ENGLISH));
        }
    }
}
