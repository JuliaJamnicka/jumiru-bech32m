package cz.fi.muni.pa193.jumiru.converter;

import cz.fi.muni.pa193.jumiru.bech32m.Bech32mIOData;
import cz.fi.muni.pa193.jumiru.converter.DataOutputConverter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import java.util.List;


public class Bech32mOutputConverterTest {
    private static final DataOutputConverter converter = new DataOutputConverter();

    @ParameterizedTest
    @MethodSource("provideBinParams")
    public void CheckValidBinaryConversion(String expected, List<Byte> toConvert) {
        Bech32mIOData data = new Bech32mIOData("", toConvert);
        assertEquals(converter.convertToBinary(data), expected);
    }

    private static Stream<Arguments> provideBinParams() {
        return Stream.of(
                Arguments.of("000111110001111000011101000111000001101100011010000110010001100" +
                                "0000101110001011000010101000101000001001100010010000100010001000" +
                                "0000011110000111000001101000011000000101100001010000010010000100" +
                                "00000011100000110000001010000010000000011000000100000000100000000",
                        Stream
                                .of(3, 28, 15, 1, 26, 7, 0, 27, 3, 8, 12, 17, 16, 5, 24, 22, 2, 20, 10, 1, 6, 4, 16, 17,
                                        2, 0, 7, 16, 28, 3, 8, 12, 1, 12, 5, 0, 18, 2, 0, 7, 0, 24, 2, 16, 8, 0, 24, 2,
                                        0, 4, 0, 0)
                                .map(Integer::byteValue)
                                .toList()),
                Arguments.of("000111110001111100011111000111110001111100011111000111110001111100011" +
                                "1110001111100011111000111110001111100011111000111110001111100011111000" +
                                "11111000111110001111100011111000111110001111100011111000111110001111100" +
                                "01111100011111000111110001111100011111000111110001111100011111000111110" +
                                "00111110001111100011111000111110001111100011111000111110001111100011111" +
                                "000111110001111100011111000111110001111100011111000111110001111100011111" +
                                "0001111100011111000111110001111100011111000111110001111100011111000111110" +
                                "0011111000111110001111100011111000111110001111100011111000111110001111100" +
                                "01111100011111000111110001111100011111000111110001111100011111000111110001" +
                                "111100011111",
                        Stream
                                .of(3, 28, 15, 17, 30, 7, 24, 31, 3, 28, 15, 17, 30, 7, 24, 31, 3, 28, 15, 17, 30, 7,
                                        24, 31, 3, 28, 15, 17, 30, 7, 24, 31, 3, 28, 15, 17, 30, 7, 24, 31, 3, 28, 15, 17,
                                        30, 7, 24, 31, 3, 28, 15, 17, 30, 7, 24, 31, 3, 28, 15, 17, 30, 7, 24, 31, 3, 28,
                                        15, 17, 30, 7, 24, 31, 3, 28, 15, 17, 30, 7, 24, 31, 3, 28, 15, 17, 30, 7, 24, 31,
                                        3, 28, 15, 17, 30, 7, 24, 31, 3, 28, 15, 17, 30, 7, 24, 31, 3, 28, 15, 17, 30, 7,
                                        24, 31, 3, 28, 15, 17, 30, 7, 24, 31, 3, 28, 15, 17, 30, 7, 24, 31, 3, 28, 15, 16)
                                .map(Integer::byteValue)
                                .toList()),
                Arguments.of("00011000000101110001100100011000000101100001110000000001000100000000101100" +
                                "011101000010000001100100010111000111010001001100001101000100000001011100011" +
                                "101000101100001100100011100000000010001000000001011000000110001100100011101" +
                                "000110110001100100000011000000110001110100010011000010110001100100000011000" +
                                "0001100011001000011010001100000011101000000010001100100000011000000110001100100001101",
                        Stream
                                .of(3, 0, 11, 17, 18, 6, 0, 22, 3, 16, 0, 17, 0, 2, 24, 29, 1, 0, 12, 17, 14, 7, 8, 19,
                                        1, 20, 8, 1, 14, 7, 8, 22, 3, 4, 14, 0, 2, 4, 0, 11, 0, 12, 12, 17, 26, 6, 24,
                                        25, 0, 12, 1, 17, 26, 4, 24, 11, 3, 4, 1, 16, 6, 6, 8, 13, 3, 0, 14, 16, 2, 6, 8,
                                        3, 0, 12, 12, 16, 26)
                                .map(Integer::byteValue)
                                .toList())
        );
    }

    @ParameterizedTest
    @MethodSource("provideHexParams")
    public void CheckValidHexConversion(String expected, List<Byte> toConvert) {
        Bech32mIOData data = new Bech32mIOData("", toConvert);
        assertEquals(expected, converter.convertToHex(data));
    }

    private static Stream<Arguments> provideHexParams() {
        return Stream.of(
                Arguments.of("1f1e1d1c1b1a191817161514131211100f0e0d0c0b0a09080706050403020100",
                        Stream
                                .of(3, 28, 15, 1, 26, 7, 0, 27, 3, 8, 12, 17, 16, 5, 24, 22, 2, 20, 10, 1, 6, 4, 16, 17,
                                        2, 0, 7, 16, 28, 3, 8, 12, 1, 12, 5, 0, 18, 2, 0, 7, 0, 24, 2, 16, 8, 0, 24, 2,
                                        0, 4, 0, 0)
                                .map(Integer::byteValue)
                                .toList()),
                Arguments.of("1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1" +
                                "f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1" +
                                "f1f1f1f1f1f1f1f1f1f1f1f1f",
                        Stream
                                .of(3, 28, 15, 17, 30, 7, 24, 31, 3, 28, 15, 17, 30, 7, 24, 31, 3, 28, 15, 17, 30, 7,
                                        24, 31, 3, 28, 15, 17, 30, 7, 24, 31, 3, 28, 15, 17, 30, 7, 24, 31, 3, 28, 15, 17,
                                        30, 7, 24, 31, 3, 28, 15, 17, 30, 7, 24, 31, 3, 28, 15, 17, 30, 7, 24, 31, 3, 28,
                                        15, 17, 30, 7, 24, 31, 3, 28, 15, 17, 30, 7, 24, 31, 3, 28, 15, 17, 30, 7, 24, 31,
                                        3, 28, 15, 17, 30, 7, 24, 31, 3, 28, 15, 17, 30, 7, 24, 31, 3, 28, 15, 17, 30, 7,
                                        24, 31, 3, 28, 15, 17, 30, 7, 24, 31, 3, 28, 15, 17, 30, 7, 24, 31, 3, 28, 15, 16)
                                .map(Integer::byteValue)
                                .toList()),
                Arguments.of("18171918161c01100b1d0819171d130d10171d16191c01100b0" +
                                "3191d1b1903031d130b190303190d181d01190303190d",
                        Stream
                                .of(3, 0, 11, 17, 18, 6, 0, 22, 3, 16, 0, 17, 0, 2, 24, 29, 1, 0, 12, 17, 14, 7, 8, 19,
                                        1, 20, 8, 1, 14, 7, 8, 22, 3, 4, 14, 0, 2, 4, 0, 11, 0, 12, 12, 17, 26, 6, 24,
                                        25, 0, 12, 1, 17, 26, 4, 24, 11, 3, 4, 1, 16, 6, 6, 8, 13, 3, 0, 14, 16, 2, 6, 8,
                                        3, 0, 12, 12, 16, 26)
                                .map(Integer::byteValue)
                                .toList()),
                Arguments.of("150f061e1e",
                        Stream
                                .of(2, 20, 7, 16, 12, 7, 16, 30)
                                .map(Integer::byteValue)
                                .toList())
        );
    }

    @ParameterizedTest
    @MethodSource("provideBase64Params")
    public void CheckValidBase64Conversion(String expected, List<Byte> toConvert) {
        Bech32mIOData data = new Bech32mIOData("", toConvert);
        assertEquals(expected, converter.convertToBase64(data));
    }

    private static Stream<Arguments> provideBase64Params() {
        return Stream.of(
                Arguments.of("Hx4dHBsaGRgXFhUUExIREA8ODQwLCgkIBwYFBAMCAQA=",
                        Stream
                                .of(3, 28, 15, 1, 26, 7, 0, 27, 3, 8, 12, 17, 16, 5, 24, 22, 2, 20, 10, 1, 6, 4, 16,
                                        17, 2, 0, 7, 16, 28, 3, 8, 12, 1, 12, 5, 0, 18, 2, 0, 7, 0, 24, 2, 16, 8, 0,
                                        24, 2, 0, 4, 0, 0)
                                .map(Integer::byteValue)
                                .toList()),
                Arguments.of("Hx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8f" +
                                "Hx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHw==",
                        Stream
                                .of(3, 28, 15, 17, 30, 7, 24, 31, 3, 28, 15, 17, 30, 7, 24, 31, 3, 28, 15, 17, 30, 7,
                                        24, 31, 3, 28, 15, 17, 30, 7, 24, 31, 3, 28, 15, 17, 30, 7, 24, 31, 3, 28, 15,
                                        17, 30, 7, 24, 31, 3, 28, 15, 17, 30, 7, 24, 31, 3, 28, 15, 17, 30, 7, 24, 31,
                                        3, 28, 15, 17, 30, 7, 24, 31, 3, 28, 15, 17, 30, 7, 24, 31, 3, 28, 15, 17, 30,
                                        7, 24, 31, 3, 28, 15, 17, 30, 7, 24, 31, 3, 28, 15, 17, 30, 7, 24, 31, 3, 28,
                                        15, 17, 30, 7, 24, 31, 3, 28, 15, 17, 30, 7, 24, 31, 3, 28, 15, 17, 30, 7, 24,
                                        31, 3, 28, 15, 16)
                                .map(Integer::byteValue)
                                .toList()),
                Arguments.of("GBcZGBYcARALHQgZFx0TDRAXHRYZHAEQCwMZHRsZAwMdEwsZAwMZDRgdARkDAxkN",
                        Stream
                                .of(3, 0, 11, 17, 18, 6, 0, 22, 3, 16, 0, 17, 0, 2, 24, 29, 1, 0, 12, 17, 14, 7, 8, 19,
                                        1, 20, 8, 1, 14, 7, 8, 22, 3, 4, 14, 0, 2, 4, 0, 11, 0, 12, 12, 17, 26, 6, 24,
                                        25, 0, 12, 1, 17, 26, 4, 24, 11, 3, 4, 1, 16, 6, 6, 8, 13, 3, 0, 14, 16, 2, 6,
                                        8, 3, 0, 12, 12, 16, 26)
                                .map(Integer::byteValue)
                                .toList())
        );
    }

}
