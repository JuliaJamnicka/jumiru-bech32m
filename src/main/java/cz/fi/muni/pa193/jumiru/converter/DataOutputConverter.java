package cz.fi.muni.pa193.jumiru.converter;

import cz.fi.muni.pa193.jumiru.bech32m.Bech32mIOData;

import java.util.function.IntFunction;
import java.util.Base64;
import java.util.List;

import static cz.fi.muni.pa193.jumiru.converter.ConverterUtils.convertBits;


public class DataOutputConverter implements OutputConverter {

    private String convert(Bech32mIOData data, IntFunction<String> integerFunction) {
        StringBuilder data_builder = new StringBuilder();
        List<Byte> convertedData = convertBits(data.getDataPart(), 5, 8, false);

        for (byte b : convertedData) {
            data_builder.append(integerFunction.apply(b));
        }
        return data_builder.toString();
    }

    public String convertToBinary(Bech32mIOData data) {
        return convert(data, x -> String
            .format("%8s", Integer.toBinaryString(x & 0xFF))
            .replace(' ', '0')
        );
    }

    public String convertToHex(Bech32mIOData data) {
        return convert(data, x -> String
            .format("%2s", Integer.toHexString(x & 0xFF))
            .replace(' ', '0')
        );
    }

    public String convertToBase64(Bech32mIOData data) {
        List<Byte> convertedData = convertBits(data.getDataPart(), 5, 8, false);

        byte[] dataArray = new byte[convertedData.size()];
        for(int i = 0; i < convertedData.size(); i++) {
            dataArray[i] = convertedData.get(i);
        }

        return Base64.getEncoder().encodeToString(dataArray);
    }
    
}
