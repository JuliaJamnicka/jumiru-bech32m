package cz.fi.muni.pa193.jumiru.converter;

import static cz.fi.muni.pa193.jumiru.converter.ConverterUtils.convertBits;

import cz.fi.muni.pa193.jumiru.bech32m.Bech32mIOData;
import java.util.Base64;
import java.util.List;
import java.util.function.IntFunction;

public final class DataOutputConverter implements OutputConverter {
    private static final int BYTE_LENGTH = 8;
    private static final int BECH32_BYTE_LENGTH = 5;

    private String convert(final Bech32mIOData data, final IntFunction<String> integerFunction) {
        StringBuilder dataBuilder = new StringBuilder();
        List<Byte> convertedData = convertBits(data.getDataPart(),
                BECH32_BYTE_LENGTH, BYTE_LENGTH, false);

        for (byte b : convertedData) {
            dataBuilder.append(integerFunction.apply(b));
        }
        return dataBuilder.toString();
    }

    public String convertToBinary(final Bech32mIOData data) {
        return convert(data, x -> String
            .format("%8s", Integer.toBinaryString(x & 0xFF))
            .replace(' ', '0')
        );
    }

    public String convertToHex(final Bech32mIOData data) {
        return convert(data, x -> String
            .format("%2s", Integer.toHexString(x & 0xFF))
            .replace(' ', '0')
        );
    }

    public String convertToBase64(final Bech32mIOData data) {
        List<Byte> convertedData = convertBits(data.getDataPart(),
                BECH32_BYTE_LENGTH, BYTE_LENGTH, false);

        byte[] dataArray = new byte[convertedData.size()];
        for (int i = 0; i < convertedData.size(); i++) {
            dataArray[i] = convertedData.get(i);
        }

        return Base64.getEncoder().encodeToString(dataArray);
    }
}
