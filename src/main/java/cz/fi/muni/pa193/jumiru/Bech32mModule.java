package cz.fi.muni.pa193.jumiru;

import java.util.ArrayList;
import java.util.List;

public final class Bech32mModule implements Bech32mTransformer {

    private static final int BECH32M_CHECKSUM_CONSTANT = 0x2bc830a3;
    private static final String CHARSET = "qpzry9x8gf2tvdw0s3jn54khce6mua7l";
    private static final byte[] CHARSET_REV = {
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            15, -1, 10, 17, 21, 20, 26, 30,  7,  5, -1, -1, -1, -1, -1, -1,
            -1, 29, -1, 24, 13, 25,  9,  8, 23, -1, 18, 22, 31, 27, 19, -1,
            1,  0,  3, 16, 11, 28, 12, 14,  6,  4,  2, -1, -1, -1, -1, -1,
            -1, 29, -1, 24, 13, 25,  9,  8, 23, -1, 18, 22, 31, 27, 19, -1,
            1,  0,  3, 16, 11, 28, 12, 14,  6,  4,  2, -1, -1, -1, -1, -1
    };

    private int bech32mPolymod(List<Byte> expandedParts) {
        int c = 1;
        for (byte value: expandedParts) {
            byte c0 = (byte) (c >>> 25);

            c = ((c & 0x1ffffff) << 5) ^ value;

            if ((c0 & 1) == 1) {
                c ^= 0x3b6a57b2;
            }

            if ((c0 & 2) == 2) {
                c ^= 0x26508e6d;
            }

            if ((c0 & 4) == 4) {
                c ^= 0x1ea119fa;
            }

            if ((c0 & 8) == 8) {
                c ^= 0x3d4233dd;
            }

            if ((c0 & 16) == 16) {
                c ^= 0x2a1462b3;
            }
        }
        return c;
    }

    private List<Byte> expandHrPart(String hrPart) {
        hrPart = hrPart.toLowerCase();
        ArrayList<Byte> expandedHrPart = new ArrayList<>();
        expandedHrPart.ensureCapacity(2 * hrPart.length() + 1);
        for (int i = 0; i < hrPart.length(); i++) {
            expandedHrPart.add((byte) (hrPart.charAt(i) >>> 5));
        }
        expandedHrPart.add((byte) 0);
        for (int i = 0; i < hrPart.length(); i++) {
            expandedHrPart.add((byte) (hrPart.charAt(i) & 0x1f));
        }
        return expandedHrPart;
    }

    private List<Byte> decodeDataPart(String dataPart) {
        //dataPart = dataPart.toLowerCase(); // handled by CHARSET_REV
        ArrayList<Byte> data = new ArrayList<>();
        data.ensureCapacity(dataPart.length());
        for (int i = 0; i < dataPart.length(); i++) {
            data.add(CHARSET_REV[dataPart.charAt(i)]);
        }
        return data;
    }

    public boolean verifyChecksum(String hrPart, List<Byte> data) {
        List<Byte> expandedHrPart = expandHrPart(hrPart);
        expandedHrPart.addAll(data);
        return bech32mPolymod(expandedHrPart) == BECH32M_CHECKSUM_CONSTANT;
    }

    public boolean checkBech32mString(String str) {

        if (str == null) {
            return false;
        }

        if (!str.equals(str.toLowerCase()) &&
                !str.equals(str.toUpperCase())) {
            return false;
        }

        str = str.toLowerCase();

        boolean dataPart = true;
        int separatorPos = 0;
        for (int i = str.length() - 1; i >= 0; i--) {

            if (str.charAt(i) == '1' && dataPart) {
                dataPart = false;
                separatorPos = i;
                continue;
            }

            if (dataPart) {
                if (CHARSET.indexOf(str.charAt(i)) == -1) {
                    return false;
                }
            } else {
                if (str.charAt(i) < 33 || str.charAt(i) > 126) {
                    return false;
                }
            }
        }

        if (dataPart) {
            return false;
        }

        if (separatorPos == 0 || separatorPos >= 84) {
            return false;
        }

        if (str.length() - separatorPos < 7) {
            return false;
        }

        return true;
    }

    public Bech32mData decodeBech32mString(String str) {

        if (!checkBech32mString(str)) {
            return new Bech32mData(null, null);
        }

        int separatorPos = str.lastIndexOf('1');

        String hrPart = str.substring(0, separatorPos);
        List<Byte> data = decodeDataPart(str.substring(separatorPos + 1));

        if (!verifyChecksum(hrPart, data)) {
            return new Bech32mData(null, null);
        }

        return new Bech32mData(hrPart, data);
    }
}
