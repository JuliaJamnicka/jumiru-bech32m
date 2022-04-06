package cz.fi.muni.pa193.jumiru.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Stream;

import cz.fi.muni.pa193.jumiru.bech32m.Bech32mIOData;
import cz.fi.muni.pa193.jumiru.converter.DataInputConverter;
import cz.fi.muni.pa193.jumiru.converter.DataOutputConverter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


public class Bech32mInputConverterTest {
    private static final DataInputConverter converter = new DataInputConverter();
    private static final DataOutputConverter outputConverter = new DataOutputConverter();

    @ParameterizedTest
    @MethodSource("provideBinParams")
    public void CheckValidFromBinConversion(String hex, String bin) {
        List<Byte> byteArray = converter.convertFromBinary(bin);
        Bech32mIOData data = new Bech32mIOData("", byteArray);
        assertEquals(hex, outputConverter.convertToHex(data));
    }

    private static Stream<Arguments> provideBinParams() {
        return Stream.of(
            Arguments.of("1f1e1d1c1b1a191817161514131211100f0e0d0c0b0a09080706050403020100",
                            "000111110001111000011101000111000001101100011010000110010001100" + 
                            "0000101110001011000010101000101000001001100010010000100010001000" + 
                            "0000011110000111000001101000011000000101100001010000010010000100" + 
                            "00000011100000110000001010000010000000011000000100000000100000000"),
            Arguments.of("1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1" +
            "f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1" +
            "f1f1f1f1f1f1f1f1f1f1f1f1f",
                        "000111110001111100011111000111110001111100011111000111110001111100011" + 
                        "1110001111100011111000111110001111100011111000111110001111100011111000" + 
                        "11111000111110001111100011111000111110001111100011111000111110001111100" + 
                        "01111100011111000111110001111100011111000111110001111100011111000111110" + 
                        "00111110001111100011111000111110001111100011111000111110001111100011111" + 
                        "000111110001111100011111000111110001111100011111000111110001111100011111" + 
                        "0001111100011111000111110001111100011111000111110001111100011111000111110" + 
                        "0011111000111110001111100011111000111110001111100011111000111110001111100" + 
                        "01111100011111000111110001111100011111000111110001111100011111000111110001" + 
                        "111100011111"),
            Arguments.of("18171918161c01100b1d0819171d130d10171d16191c01100b0" +
                            "3191d1b1903031d130b190303190d181d01190303190d",
                        "00011000000101110001100100011000000101100001110000000001000100000000101100" + 
                        "011101000010000001100100010111000111010001001100001101000100000001011100011" +
                        "101000101100001100100011100000000010001000000001011000000110001100100011101" + 
                        "000110110001100100000011000000110001110100010011000010110001100100000011000" + 
                        "0001100011001000011010001100000011101000000010001100100000011000000110001100100001101"),
            Arguments.of("150f061e1e", "0001010100001111000001100001111000011110")
        );
    }

    @ParameterizedTest
    @MethodSource("provideBinParams")
    public void CheckValidFromHexConversion(String hex, String __) {
        List<Byte> byteArray = converter.convertFromHex(hex);
        Bech32mIOData data = new Bech32mIOData("", byteArray);
        assertEquals(hex, outputConverter.convertToHex(data));
    }

    @ParameterizedTest
    @MethodSource("provideBase64Params")
    public void CheckValidFromBase64Conversion(String hex, String base64string) {
        List<Byte> byteArray = converter.convertFromBase64(base64string);
        Bech32mIOData data = new Bech32mIOData("", byteArray);
        assertEquals(hex, outputConverter.convertToHex(data));
    }

    private static Stream<Arguments> provideBase64Params() {
        return Stream.of(
            Arguments.of("1f1e1d1c1b1a191817161514131211100f0e0d0c0b0a09080706050403020100", 
                        "Hx4dHBsaGRgXFhUUExIREA8ODQwLCgkIBwYFBAMCAQA="),
            Arguments.of("1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1" +
                        "f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1" +
                        "f1f1f1f1f1f1f1f1f1f1f1f1f",
                        "Hx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8f" + 
                        "Hx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHw=="),
            Arguments.of("18171918161c01100b1d0819171d130d10171d16191c01100b0" +
                        "3191d1b1903031d130b190303190d181d01190303190d",
                        "GBcZGBYcARALHQgZFx0TDRAXHRYZHAEQCwMZHRsZAwMdEwsZAwMZDRgdARkDAxkN")
        );
    }

    
}
