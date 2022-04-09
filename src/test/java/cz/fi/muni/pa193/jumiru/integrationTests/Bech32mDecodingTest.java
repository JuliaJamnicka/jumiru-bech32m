package cz.fi.muni.pa193.jumiru.integrationTests;

import cz.fi.muni.pa193.jumiru.bech32m.TestVectors;
import cz.fi.muni.pa193.jumiru.ui.UserInterfaceModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Locale;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import cz.fi.muni.pa193.jumiru.bech32m.TestVectors.*;

public class Bech32mDecodingTest {
    private static Stream<Arguments> provideValidHexEncodedInputs() {
        return Stream.of(
                Arguments.of("8b08e67499ba084cb632fd291d0efdaa1139372b71dbfc7bf7",
                        "abcdef13vywvayehgyyed3jl5536rha4ggnjdetw8dlc7lhfp7yxd"),
                Arguments.of("cfb7ab193b9f97568674281254a929d0e5d9",
                        "abcdef1e7m6kxfmn7t4dpn59qf9f2ff6rjajwezyya"),
                Arguments.of("93ae3dff08d7a8ba5aaa8c32db009eedbb2e",
                        "abcdef1jwhrmlcg675t5k423sedkqy7akajumkdjwe"),
                Arguments.of("93ae3dff08d7a8ba5aaa8c32db009eedbb2e93ae3dff08d7a8ba5aaa8c32"
                                + "db009eedbb2e93ae3dff08d7a8ba5a93ae3d",
                        "abcdef1jwhrmlcg675t5k423sedkqy7akajayaw8hls34aghfd24rpjmvqfamdm96f6u00lprt63wj6jwhr6fc7dlt"),
                Arguments.of("f2507b165f11c6f0a4",
                        "abcdef17fg8k9jlz8r0pfqtentyq")
        );
    }

    private static Stream<Arguments> provideValidBinEncodedInputs() {
        return Stream.of(
                Arguments.of("1010000110110000011010101010110100110000000011011001001111100101010110010101000"
                                + "01001111001000000001110001101000011010011101110010011111111011001100011101"
                                + "00000001111110110100100101110011100110010110101011101111000000001101001",
                        "n545nggn3gh94h34h15xcx4tfspkf72k2sneqr35xnhylanr5qlkjtnn94w7qxjjpwe0z"),
                Arguments.of("0101110011001001010110111111101100010001000110101010010110011100",
                        "n545nggn3gh94h34h1tny4h7c3r2jeccjhfc2"),
                Arguments.of("0101111000110000011000000101110111001101111011100011000011101110",
                        "n545nggn3gh94h34h1tccxqhwdaccwumwc3vl")
        );
    }

    private static Stream<Arguments> provideValidBase64EncodedInputs() {
        return Stream.of(
                Arguments.of("fMSHuetY98YqS35ClKs7FMPgCH95PBJK7DZjqS0=",
                        "111111111110nzg0w0ttrmuv2jt0epff2emznp7qzrl0y7pyjhvxe36jtgylz8jt"),
                Arguments.of("70sPFRpPbMIQXA==",
                        "11111111111aa9s79g6fakvyyzu3u8p2q"),
                Arguments.of("FjxmKf39TdxQJ50aD2TATlTGoqmLPiMUCHmVpXMYKhYXNg==",
                        "11111111111zc7xv20al4xac5p8n5dq7exqfe2vdg4f3vlzx9qg0x262ucc9gtpwdsmwxnlu"),
                Arguments.of("LuRCKZkk8upCwz2ZjEh+OLWh",
                        "111111111119mjyy2veynew5skr8kvccjr78z66zfyvhl7")
        );
    }

    private void checkDecodedOutput(String[] args, String input, String result) {
        UserInterfaceModule module = new UserInterfaceModule(args);

        // catching standard output
        final ByteArrayOutputStream myOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(myOut));

        module.entryPointWrapper();
        assertEquals(result + System.lineSeparator(), myOut.toString());
    }

    @ParameterizedTest
    @MethodSource("provideValidHexEncodedInputs")
    public void shouldReturnEncodedHex(String result, String input) {
        String[] args = {
                "decode",
                "hex",
                "arg",
                input,
                "stdout"
        };
        checkDecodedOutput(args, input, result);
    }

    @ParameterizedTest
    @MethodSource("provideValidBinEncodedInputs")
    public void shouldReturnEncodedBin(String result, String input) {
        String[] args = {
                "decode",
                "bin",
                "arg",
                input,
                "stdout"
        };
        checkDecodedOutput(args, input, result);
    }

    @ParameterizedTest
    @MethodSource("provideValidBase64EncodedInputs")
    public void shouldReturnEncodedBase64(String result, String input) {
        String[] args = {
                "decode",
                "base64",
                "arg",
                input,
                "stdout"
        };
        checkDecodedOutput(args, input, result);
    }

    private static Stream<Arguments> provideInvalidBech32mAndCorrections() {
        return Stream.of(
                Arguments.of("B1LQFN3A", "a1lqfn3a"),
                Arguments.of("b1lqfn3a", "a1lqfn3a"),
                Arguments.of("an84characterlonghumanreadablepartthatcontainsthetheexcludedcharactersbio"
                        + "andnumber11sg7hg6",
                        "an83characterlonghumanreadablepartthatcontainsthetheexcludedcharactersbioandnumber11sg7hg6"),
                Arguments.of("zbcdef140x77khk82w", "abcdef140x77khk82w"),
                Arguments.of("abcdef1p7aum6echk45nj3s0wdvt2fg8x9yrzpqzd3ryx",
                        "abcdef1l7aum6echk45nj3s0wdvt2fg8x9yrzpqzd3ryx"),
                Arguments.of("split1cheqkupstagehandshakeupstreamerranterredcaperredlc445v",
                        "split1checkupstagehandshakeupstreamerranterredcaperredlc445v"),
                Arguments.of("!1v759aa", "?1v759aa")
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidBech32mAndCorrections")
    public void shouldFailOnInvalidBech32mInputChecksum(String input, String __) {
        String[] args = {
                "decode",
                "hex",
                "arg",
                input,
                "stdout"
        };
        UserInterfaceModule module = new UserInterfaceModule(args);

        final ByteArrayOutputStream myOut = new ByteArrayOutputStream();
        System.setErr(new PrintStream(myOut));

        module.entryPointWrapper();
        assertEquals("Provided bech32m string has incorrect checksum." +
                System.lineSeparator(), myOut.toString());

    }

    @ParameterizedTest
    @MethodSource("provideInvalidBech32mAndCorrections")
    public void shouldCorrectInvalidBech32mInput(String input, String corrected) {
        String[] args = {
                "decode",
                "hex",
                "arg",
                input,
                "stdout",
                "errdetect"
        };
        UserInterfaceModule module = new UserInterfaceModule(args);

        final ByteArrayOutputStream myOut = new ByteArrayOutputStream();
        System.setErr(new PrintStream(myOut));

        module.entryPointWrapper();
        assertTrue(myOut.toString().contains(corrected));
    }
}
