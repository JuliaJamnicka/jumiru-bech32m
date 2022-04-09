package cz.fi.muni.pa193.jumiru.converter;

import java.util.ArrayList;
import java.util.List;

public final class ConverterUtils {
    private ConverterUtils() { }

    static String padZerosToString(final String input, final int n) {
        int zeroesToAdd = n - (input.length() % n);
        return "0".repeat(zeroesToAdd % n) + input;
    }

    static List<Byte> convertBits(final List<Byte> bytes,
                                            final int fromBits,
                                            final int toBits,
                                            final boolean pad) {
        List<Byte> convertedBytes = new ArrayList<>();

        int acc = 0;
        int bits = 0;
        int maxValue = (1 << toBits) - 1;
        int maxAcc = (1 << (fromBits + toBits - 1)) - 1;

        for (Byte b : bytes) {
            int unsigned = b & 0xff;
            if (unsigned >> fromBits != 0) {
                throw new DataInputException(bytes.toString());
            }

            acc = ((acc << fromBits) | unsigned) & maxAcc;
            bits += fromBits;

            while (bits >= toBits) {
                bits -= toBits;
                convertedBytes.add((byte) ((acc >> bits) & maxValue));
            }

        }
        if (pad) {
            if (bits != 0) {
                Byte byteToAdd = (byte) ((acc << (toBits - bits)) & maxValue);
                convertedBytes.add(byteToAdd);
            }
        } else {
            if (bits >= fromBits
                || ((acc << (toBits - bits)) & maxValue) != 0) {
                throw new DataInputException(bytes.toString());
            }
        }

        return convertedBytes;
    }
}
