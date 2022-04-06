package cz.fi.muni.pa193.jumiru.converter;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


public class DataInputConverter implements InputConverter {

    private String padZerosToString(String input, int n) {
        int zeroes_to_add = n - (input.length() % n);
        return "0".repeat(zeroes_to_add % n) + input;
    }

    private List<Byte> convertBits(List<Byte> bytes) {
        List<Byte> convertedBytes = new ArrayList<>();

        int fromBits = 8;
        int toBits = 5;

        int acc = 0;
        int bits = 0;

        int maxValue = (1 << toBits) - 1;
        int maxAcc = (1 << (fromBits + toBits - 1)) - 1;

        for (Byte b : bytes) {
            if ((int) b < 0) {
                throw new DataInputException(bytes.toString()); //this is ugly
            }

            acc = ((acc << fromBits) | b) & maxAcc;
            bits += fromBits;

            while (bits >= toBits) {
                bits -= toBits;
                convertedBytes.add((byte) ((acc >> bits) & maxValue));
            }

        }

        if (bits != 0) {
            convertedBytes.add((byte) ((acc << (toBits - bits)) & maxValue));
        }

        return convertedBytes;
    }


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

        return convertBits(byteArray);
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

        return convertBits(byteArray);
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

        return convertBits(bytes);
    }
    
}
