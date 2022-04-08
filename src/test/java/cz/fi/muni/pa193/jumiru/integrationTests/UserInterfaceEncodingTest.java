package cz.fi.muni.pa193.jumiru.integrationTests;

import cz.fi.muni.pa193.jumiru.ui.UserInterfaceModule;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserInterfaceEncodingTest {
    private static final String helpMessage = System.lineSeparator() + "For correct usage, see README.md file" +
            System.lineSeparator();

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


    private void checkEncodedBech32mOutput(String[] args, String input, String result) {
        UserInterfaceModule module = new UserInterfaceModule(args);

        // catching standard output
        final ByteArrayOutputStream myOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(myOut));

        module.entryPointWrapper();
        assertEquals("Encoded bech32m string is: " + result + System.lineSeparator(), myOut.toString());
    }


    @ParameterizedTest
    @MethodSource("provideValidHexEncodedInputs")
    public void shouldReturnEncodedHex(String input, String result) {
        String[] args = {
                "encode",
                "hex",
                "arg",
                input,
                "stdout",
                "abcdef"
        };
        checkEncodedBech32mOutput(args, input, result);
    }

    @ParameterizedTest
    @MethodSource("provideValidBinEncodedInputs")
    public void shouldReturnEncodedBin(String input, String result) {
        String[] args = {
                "encode",
                "bin",
                "arg",
                input,
                "stdout",
                "n545nggn3gh94h34h"
        };
        checkEncodedBech32mOutput(args, input, result);
    }

    @ParameterizedTest
    @MethodSource("provideValidBase64EncodedInputs")
    public void shouldReturnEncodedBase64(String input, String result) {
        String[] args = {
                "encode",
                "base64",
                "arg",
                input,
                "stdout",
                "1111111111"
        };
        checkEncodedBech32mOutput(args, input, result);
    }


    private static Stream<Arguments> provideMapOfFormatsAndData() {
        return Stream.of(
                Arguments.of("hex", "fe10d154"),
                Arguments.of("bin", "0010110111110110111000001011010011001011"),
                Arguments.of("base64", "fMSHuetY98YqS35ClKs7FMPgCH95PBJK7DZjqS0=")
        );
    }

    private void checkEmptyHRPartInput(String[] args) {
        UserInterfaceModule module = new UserInterfaceModule(args);

        final ByteArrayOutputStream myOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(myOut));

        module.entryPointWrapper();
        assertEquals("The HRP argument is missing" + System.lineSeparator()
                + helpMessage, myOut.toString());
    }

    @ParameterizedTest
    @MethodSource("provideMapOfFormatsAndData")
    public void noHRPartShouldFail(String format, String input) {
        String[] args = {
                "encode",
                format,
                "arg",
                input,
                "stdout"
        };
        checkEmptyHRPartInput(args);
    }

    @ParameterizedTest
    @MethodSource("provideMapOfFormatsAndData")
    public void EmptyHRPartShouldFail(String format, String input) {
        String[] args = {
                "encode",
                format,
                "arg",
                input,
                "stdout",
                ""
        };
        checkEmptyHRPartInput(args);
    }

    private static Stream<Arguments> provideMapOfFormatsAndTooLongData() {
        return Stream.of(
                Arguments.of("hex", "36644e9c6502630ab3df1ff7df72657724de31590ba1f5f0ca48fb347788a6aef7df726"
                        + "57726630a59ff36644e9c6502630ab3df1ff7df72657724de31590ba1f5f0ca48fb347788a6aef7df726"
                        + "57726630a59ff"),
                Arguments.of("hex", "36644e9c6502630ab3df1ff7df72657724de31590ba1f5f0ca48fb347788a6aef7df726"
                        + "57726630a59ff"),
                Arguments.of("bin", "01001010000101100000100100000100100000011100001000010010110001000110100"
                        + "111011000010110011001111010111010110001001000000100001111011000011110000011000100"
                        + "100110011000010011010111110010010100100101001100100110001111110010011001011010011"
                        + "110001000010000100101011111100011010000111101100001111000001100010000001100010010"
                        + "0110011000010011010111110010010100100101001100100110001111"),
                Arguments.of("base64", "qdWcPfOOwRrusxbt2f6OONr49Msjlp6hiELn+ZEg5qnVnD3zjsEa7rMW7dn+jjja+PTL"
                        + "I5aeoYhC5/mRIOY=")
        );
    }

    @ParameterizedTest
    @MethodSource("provideMapOfFormatsAndTooLongData")
    public void shouldFailOnTooLongInput(String format, String input) {
        String[] args = {
                "encode",
                format,
                "arg",
                input,
                "stdout",
                ""
        };
        UserInterfaceModule module = new UserInterfaceModule(args);

        final ByteArrayOutputStream myOut = new ByteArrayOutputStream();
        System.setErr(new PrintStream(myOut));

        module.entryPointWrapper();
        assertEquals("The data part exceeds maximal allowed length" + System.lineSeparator()
                + helpMessage, myOut.toString());
    }

    public void shouldFailOnInvalidMode() {
        String wrongMode = "dance";
        String[] args = {
                wrongMode,
                "hex",
                "arg",
                "88388c736cf405b18569fab538188f5f5f",
                "stdout",
                ""
        };

        UserInterfaceModule module = new UserInterfaceModule(args);

        final ByteArrayOutputStream myOut = new ByteArrayOutputStream();
        System.setErr(new PrintStream(myOut));

        module.entryPointWrapper();
        assertEquals("Argument 0(" + wrongMode + ") must be encode/decode"
                + System.lineSeparator() + helpMessage, myOut.toString());
    }

    public void shouldFailOnInvalidInputFormat() {
        String wrongFormat = "bass64";
        String[] args = {
                "dance",
                wrongFormat,
                "arg",
                "88388c736cf405b18569fab538188f5f5f",
                "stdout",
                ""
        };

        UserInterfaceModule module = new UserInterfaceModule(args);

        final ByteArrayOutputStream myOut = new ByteArrayOutputStream();
        System.setErr(new PrintStream(myOut));

        module.entryPointWrapper();
        assertEquals("Argument 1(" + wrongFormat + ") must be bin/base64/hex"
                + System.lineSeparator() + helpMessage, myOut.toString());

    }

}
