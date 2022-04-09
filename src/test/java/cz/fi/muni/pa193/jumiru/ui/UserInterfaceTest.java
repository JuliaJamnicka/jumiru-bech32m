package cz.fi.muni.pa193.jumiru.ui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserInterfaceTest {
    private static final String helpMessage = System.lineSeparator() + "For correct usage, see README.md file" +
            System.lineSeparator();

    private static Stream<Arguments> provideMapOfFormatsAndData() {
        return Stream.of(
                Arguments.of("hex", "fe10d154"),
                Arguments.of("bin", "0010110111110110111000001011010011001011"),
                Arguments.of("base64", "fMSHuetY98YqS35ClKs7FMPgCH95PBJK7DZjqS0=")
        );
    }

    private void checkEmptyHRPartInput(String[] args, String expected) {
        UserInterfaceModule module = new UserInterfaceModule(args);

        final ByteArrayOutputStream myOut = new ByteArrayOutputStream();
        System.setErr(new PrintStream(myOut));

        module.entryPointWrapper();
        assertEquals(expected, myOut.toString());
    }

    @ParameterizedTest
    @MethodSource("provideMapOfFormatsAndData")
    void noHRPartShouldFail(String format, String input) {
        String[] args = {
                "encode",
                format,
                "arg",
                input,
                "stdout"
        };
        checkEmptyHRPartInput(args, "The HRP argument is missing" + System.lineSeparator()
                + helpMessage);
    }

    @ParameterizedTest
    @MethodSource("provideMapOfFormatsAndData")
    void emptyHRPartShouldFail(String format, String input) {
        String[] args = {
                "encode",
                format,
                "arg",
                input,
                "stdout",
                ""
        };
        checkEmptyHRPartInput(args, "Invalid Bech32m to decode: Missing human readable part"
                + System.lineSeparator());
    }

    @Test
    void shouldFailOnInvalidMode() {
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
        assertEquals("Argument 0 (" + wrongMode + ") must be encode/decode"
                + System.lineSeparator() + helpMessage, myOut.toString());
    }

    @Test
    void shouldFailOnInvalidInputFormat() {
        String wrongFormat = "bass64";
        String[] args = {
                "encode",
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

    @Test
    void shouldFailOnInvalidOutput() {
        String wrongOutput = "error";
        String[] args = {
                "encode",
                "bin",
                "arg",
                "88388c736cf405b18569fab538188f5f5f",
                wrongOutput
        };

        UserInterfaceModule module = new UserInterfaceModule(args);

        final ByteArrayOutputStream myOut = new ByteArrayOutputStream();
        System.setErr(new PrintStream(myOut));

        module.entryPointWrapper();
        assertEquals("Argument 4(" + wrongOutput + ") must be arg/stdin/file/stdout"
                + System.lineSeparator() + helpMessage, myOut.toString());

    }

    @Test
    void shouldFailOnInvalidErrorDetect() {
        String[] args = {
                "decode",
                "hex",
                "arg",
                "88388c736cf405b18569fab538188f5f5f",
                "stdout",
                "detect"
        };

        UserInterfaceModule module = new UserInterfaceModule(args);

        final ByteArrayOutputStream myOut = new ByteArrayOutputStream();
        System.setErr(new PrintStream(myOut));

        module.entryPointWrapper();
        assertEquals("The optional 'errdetect' argument expected and "
                + "instead, '" + "detect" + "' was provided"
                + System.lineSeparator() + helpMessage, myOut.toString());
    }

    @Test
    void shouldFailOnTooManyArguments() {
        String[] args = {
                "decode",
                "hex",
                "arg",
                "88388c736cf405b18569fab538188f5f5f",
                "stdout",
                "errdetect",
                "hello",
        };

        UserInterfaceModule module = new UserInterfaceModule(args);

        final ByteArrayOutputStream myOut = new ByteArrayOutputStream();
        System.setErr(new PrintStream(myOut));

        module.entryPointWrapper();
        assertEquals("Following argument errdetect"
                + ", there is one or more unnecessary arguments."
                + System.lineSeparator() + helpMessage, myOut.toString());

    }

}
