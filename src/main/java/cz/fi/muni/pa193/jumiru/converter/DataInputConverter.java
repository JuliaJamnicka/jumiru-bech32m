package cz.fi.muni.pa193.jumiru.converter;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


public class DataInputConverter implements InputConverter {

    private String padZerosToString(String input, int n) {
        int zeroes_to_add = n - (input.length() % n);
        return "0".repeat(zeroes_to_add % n) + input;
    }

    public List<Byte> convertFromBinary(String bech32mDataInput) {
        String data = padZerosToString(bech32mDataInput, 8);

        List<Byte> byteArray = new ArrayList<Byte>();

        for (int i = 0; i < data.length(); i += 8) {
            byteArray.add(Byte.parseByte(data.substring(i, i + 8), 2));
        }

        return byteArray;
    }

    public List<Byte> convertFromHex(String bech32mDataInput) {
        String data = padZerosToString(bech32mDataInput, 2);

        List<Byte> byteArray = new ArrayList<Byte>();

        for (int i = 0; i < data.length(); i += 2) {
            int decimal = Integer.parseInt(data.substring(i, i + 2), 16);
            byteArray.add((byte) decimal);
        }

        return byteArray;
    }

    public List<Byte> convertFromBase64(String bech32mDataInput) {
        byte[] decoded = Base64.getDecoder().decode(bech32mDataInput);
        List<Byte> bytes = new ArrayList<Byte>();

        for (byte b : decoded) {
            bytes.add(b);
        }
        return bytes;
    }
    
}
