package cz.fi.muni.pa193.jumiru;

import java.util.function.IntFunction;
import java.util.Base64;
import java.util.List;

public class DataOutputConverter implements OutputConverter {

    private String convert(Bech32mIOData data, IntFunction<String> integerFunction) {
        StringBuilder data_builder = new StringBuilder();
        for (byte b : data.getDataPart()) {
            data_builder.append(integerFunction.apply(b));
        }
        return data_builder.toString();
    }

    public String convertToBinary(Bech32mIOData data) {
        return convert(data, x -> String.format("%8s", Integer.toBinaryString(x & 0xFF)).replace(' ', '0'));
    }

    public String convertToHex(Bech32mIOData data) {
        return convert(data, x -> String.format("%2s", Integer.toHexString(x & 0xFF)).replace(' ', '0'));
    }

    public String convertToBase64(Bech32mIOData data) {
        List<Byte> dataPart = data.getDataPart();

        byte[] dataArray = new byte[data.getDataPart().size()];
        for(int i = 0; i < dataPart.size(); i++) {
            dataArray[i] = dataPart.get(i).byteValue();
        }

        return Base64.getEncoder().encodeToString(dataArray);
    }
    
}
