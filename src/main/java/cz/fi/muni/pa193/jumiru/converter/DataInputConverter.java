package cz.fi.muni.pa193.jumiru.converter;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


public class DataInputConverter implements InputConverter {

    private String padZerosToString(String input, int n) {
        int zeroes_to_add = n - (input.length() % n);
        return "0".repeat(zeroes_to_add % n) + input;
    }

    public String convertFromBinary(String bech32mDataInput) {
        String data = padZerosToString(bech32mDataInput, 8);

        StringBuilder hexBuilder = new StringBuilder();

        for (int i = 0; i < data.length(); i += 8) {
            int decimal = Integer.parseInt(data.substring(i, i + 8), 2);
            String hex = String.format("%2s", Integer.toString(decimal, 16)).replace(' ', '0');
            hexBuilder.append(hex);
        }

        return hexBuilder.toString();
    }

    public List<Byte> convertFromBase64(String bech32mDataInput) {
        byte[] decoded = Base64.getDecoder().decode(bech32mDataInput);
        List<Byte> bytes = new ArrayList<Byte>();

        for (byte b : decoded) {
            bytes.add(Byte.valueOf(b));
        }
        return bytes;
    }
    
}
