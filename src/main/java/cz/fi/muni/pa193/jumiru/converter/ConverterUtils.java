package cz.fi.muni.pa193.jumiru.converter;

import java.util.ArrayList;
import java.util.List;

public class ConverterUtils {
    public static String padZerosToString(String input, int n) {
        int zeroes_to_add = n - (input.length() % n);
        return input + "0".repeat(zeroes_to_add % n);
    }

    public static List<Byte> convertBits(List<Byte> bytes, int fromBits, int toBits, boolean pad) {
        List<Byte> convertedBytes = new ArrayList<>();

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
        if (pad) {
            if (bits != 0) {
                convertedBytes.add((byte) ((acc << (toBits - bits)) & maxValue));
            }
        } else {
            if (bits >= fromBits || ((acc << (toBits - bits)) & maxValue) != 0) {
                throw new DataInputException(bytes.toString());
            }
        }

        return convertedBytes;
    }
}
