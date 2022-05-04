package cz.fi.muni.pa193.jumiru.converter;

import static cz.fi.muni.pa193.jumiru.converter.ConverterUtils.convertBits;
import static cz.fi.muni.pa193.jumiru.converter.ConverterUtils.padZerosToString;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public final class DataInputConverter implements InputConverter {
    private static final int BIN_BYTE_LENGTH = 8;
    private static final int HEX_BYTE_LENGTH = 2;
    private static final int BECH32_BYTE_LENGTH = 5;

    public List<Byte> convertFromBinary(final String bech32mDataInput) {
        String data = padZerosToString(bech32mDataInput, BIN_BYTE_LENGTH);

        List<Byte> byteArray = new ArrayList<>();

        try {
            for (int i = 0; i < data.length(); i += BIN_BYTE_LENGTH) {
                String byteSubstring = data.substring(i, i + BIN_BYTE_LENGTH);

                int decimal = Integer.parseInt(byteSubstring, 2);
                byteArray.add((byte) decimal);
            }
        } catch (NumberFormatException e) {
            throw new DataInputException(bech32mDataInput);
        }

        return convertBits(byteArray, BIN_BYTE_LENGTH, BECH32_BYTE_LENGTH, true);
    }

    public List<Byte> convertFromHex(final String bech32mDataInput) {
        String data = padZerosToString(bech32mDataInput, HEX_BYTE_LENGTH);

        List<Byte> byteArray = new ArrayList<>();

        try {
            for (int i = 0; i < data.length(); i += HEX_BYTE_LENGTH) {
                String byteSubstring = data.substring(i, i + HEX_BYTE_LENGTH);

                int decimal = Integer.parseInt(byteSubstring, 16);
                byteArray.add((byte) decimal);
            }
        } catch (NumberFormatException e) {
            throw new DataInputException(bech32mDataInput);
        }

        return convertBits(byteArray, BIN_BYTE_LENGTH, BECH32_BYTE_LENGTH, true);
    }

    public List<Byte> convertFromBase64(final String bech32mDataInput) {
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

        return convertBits(bytes, BIN_BYTE_LENGTH, BECH32_BYTE_LENGTH, true);
    }
}
