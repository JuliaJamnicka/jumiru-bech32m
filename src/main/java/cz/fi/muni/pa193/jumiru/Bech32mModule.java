package cz.fi.muni.pa193.jumiru;

import java.util.Locale;

public final class Bech32mModule implements Bech32mTransformer {

    private static final String CHARSET = "qpzry9x8gf2tvdw0s3jn54khce6mua7l";

    public static boolean validateBech32mString(String str) {

        if (str == null) {
            return false;
        }

        if (!str.equals(str.toLowerCase()) &&
                !str.equals(str.toUpperCase())) {
            return false;
        }

        str = str.toLowerCase();

        boolean data_part = true;
        int separator_pos = 0;
        for (int i = str.length() - 1; i >= 0; i--) {

            if (str.charAt(i) == '1' && data_part) {
                data_part = false;
                separator_pos = i;
                continue;
            }

            if (data_part) {
                if (CHARSET.indexOf(str.charAt(i)) == -1) {
                    return false;
                }
            } else {
                if (str.charAt(i) < 33 || str.charAt(i) > 126) {
                    return false;
                }
            }
        }

        if (data_part) {
            return false;
        }

        if (separator_pos == 0 || separator_pos >= 84) {
            return false;
        }

        return str.length() - separator_pos >= 7;
    }
}
