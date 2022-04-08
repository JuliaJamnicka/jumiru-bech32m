package cz.fi.muni.pa193.jumiru.bech32m;

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
    public static int BECH32M_MAX_LENGTH = 80;

    private int bech32mPolymod(List<Byte> expandedParts) {
        int c = 1;
        int[] GEN = {0x3b6a57b2, 0x26508e6d, 0x1ea119fa, 0x3d4233dd, 0x2a1462b3};
        for (byte value: expandedParts) {
            byte c0 = (byte) (c >>> 25);
            c = ((c & 0x1ffffff) << 5) ^ value;
            int p = 1;
            for (int i = 0; i<5; i++) {
                if ((c0 & p) == p) {
                    c ^= GEN[i];
                }
                p *= 2;
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

    public void checkBech32mString(String str) {
        if (str == null) throw new Bech32mException("Invalid Bech32m to decode: Null string");
        if (!str.equals(str.toLowerCase()) && !str.equals(str.toUpperCase()))
            throw new Bech32mException("Invalid Bech32m to decode: Mixed case");

        str = str.toLowerCase();

        boolean separatorFound = false;
        int separatorPos = 0;
        for (int i = str.length() - 1; i >= 0; i--) {
            if (str.charAt(i) == '1' && !separatorFound) {
                separatorFound = true;
                separatorPos = i;
                continue;
            }

            if (!separatorFound) {
                if (CHARSET.indexOf(str.charAt(i)) == -1)
                    throw new Bech32mException("Invalid Bech32m to decode: Unsupported character " +
                            "in the data part");
            } else {
                if (str.charAt(i) < 33 || str.charAt(i) > 126)
                    throw new Bech32mException("Invalid Bech32m to decode: Unsupported character " +
                            "in the human readable part");
            }
        }

        if (!separatorFound) throw new Bech32mException("Invalid Bech32m to decode: Missing " +
                "separator");
        if (separatorPos == 0) throw new Bech32mException("Invalid Bech32m to decode: Missing " +
                "human readable part");
        if (separatorPos >= 84) throw new Bech32mException("Invalid Bech32m to decode: String too" +
                " long");
        if (str.length() - separatorPos < 7) throw new Bech32mException("Invalid Bech32m to " +
                "decode: Data part too short");
    }

    public Bech32mIOData decodeBech32mString(String str, boolean performErrorCorrection) {
        checkBech32mString(str);

        int separatorPos = str.lastIndexOf('1');

        String hrPart = str.substring(0, separatorPos).toLowerCase();
        List<Byte> data = decodeDataPart(str.substring(separatorPos + 1));

        if (!verifyChecksum(hrPart, data)) {
            if (!performErrorCorrection) {
                throw new Bech32mInvalidChecksumException();
            } else {
                throw new Bech32mInvalidChecksumException(findPossibleErrorCorrections(hrPart, data));
            }
        }

        return new Bech32mIOData(hrPart, data.subList(0, data.size() - 6));
    }

    private List<String> findPossibleErrorCorrections(String hrPart, List<Byte> data) {
        List<String> candidates = new ArrayList<>();

        List<Byte> dataPart = new ArrayList<>(data);
        for (int i = 0; i < dataPart.size() - 6; i++) {
            byte originalValue = dataPart.get(i);
            for (byte replacement = 0; replacement < 32; replacement++) {
                if (replacement == originalValue) {
                    continue;
                }
                dataPart.set(i, replacement);
                if (verifyChecksum(hrPart, dataPart)) {
                    candidates.add(encodeBech32mString(new Bech32mIOData(hrPart,
                            new ArrayList<>(dataPart.subList(0, dataPart.size() - 6)))));
                }
            }
            dataPart.set(i, originalValue);
        }

        StringBuilder hrPartMutable = new StringBuilder(hrPart);
        for (int i = 0; i < hrPartMutable.length(); i++) {
            char originalValue = hrPartMutable.charAt(i);
            for (char replacement = 33; replacement < 127; replacement++) {
                if (replacement == originalValue || (replacement >= 65 && replacement <= 90)) {
                    continue;
                }
                hrPartMutable.setCharAt(i, replacement);
                if (verifyChecksum(hrPartMutable.toString(), data)) {
                    candidates.add(encodeBech32mString(new Bech32mIOData(hrPartMutable.toString(),
                            new ArrayList<>(data.subList(0, data.size() - 6)))));
                }
            }
            hrPartMutable.setCharAt(i, originalValue);
        }

        return candidates;
    }

    @Override
    public String encodeBech32mString(Bech32mIOData input) {
        String hrPart = input.getHrPart().toLowerCase();

        StringBuilder output = new StringBuilder(hrPart);
        output.append('1');

        List<Byte> data = new ArrayList<>(input.getDataPart());
        data.addAll(calculateChecksum(input));
        for (byte c: data) {
            output.append(CHARSET.charAt(c));
        }

        return output.toString();
    }

    public List<Byte> calculateChecksum(Bech32mIOData input) {
        List<Byte> data = expandHrPart(input.getHrPart());
        data.addAll(input.getDataPart());
        for (int i = 0; i < 6; i++) {
            data.add((byte) 0);
        }
        int result = bech32mPolymod(data) ^ BECH32M_CHECKSUM_CONSTANT;
        ArrayList<Byte> checksum = new ArrayList<>();
        checksum.ensureCapacity(6);
        for (int i = 0; i < 6; i++) {
            checksum.add((byte) ((result >> (5 * (5 - i))) & 31));
        }
        return checksum;
    }
}
