package cz.fi.muni.pa193.jumiru.converter;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static cz.fi.muni.pa193.jumiru.converter.ConverterUtils.*;


public class DataInputConverter implements InputConverter {

    public List<Byte> convertFromBinary(String bech32mDataInput) {
        String data = padZerosToString(bech32mDataInput, 8);

        List<Byte> byteArray = new ArrayList<>();

        try {
            for (int i = 0; i < data.length(); i += 8) {
                byteArray.add(Byte.parseByte(data.substring(i, i + 8), 2));
            }
        } catch (NumberFormatException e) {
            throw new DataInputException(bech32mDataInput);
        }

        return convertBits(byteArray, 8, 5, true);
    }

    public List<Byte> convertFromHex(String bech32mDataInput) {
        String data = padZerosToString(bech32mDataInput, 2);

        List<Byte> byteArray = new ArrayList<>();

        try {
            for (int i = 0; i < data.length(); i += 2) {
                int decimal = Integer.parseInt(data.substring(i, i + 2), 16);
                byteArray.add((byte) decimal);
            }
        } catch(NumberFormatException e) {
            throw new DataInputException(bech32mDataInput);
        }

        return convertBits(byteArray, 8, 5, true);
    }

    public List<Byte> convertFromBase64(String bech32mDataInput) {
        byte[] decoded;

        try {
            decoded = Base64.getDecoder().decode(bech32mDataInput);
        } catch (IllegalArgumentException e) {
            throw new DataInputException(bech32mDataInput);
        }
        List<Byte> bytes = new ArrayList<>();

        for (byte b : decoded) {
            bytes.add(b);
        }

        return convertBits(bytes, 8, 5, true);
    }
    
}
