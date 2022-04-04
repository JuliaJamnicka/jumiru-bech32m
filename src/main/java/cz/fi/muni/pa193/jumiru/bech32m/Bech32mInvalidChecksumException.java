package cz.fi.muni.pa193.jumiru;

import java.util.List;

public class Bech32mInvalidChecksumException extends Bech32mException {

    public Bech32mInvalidChecksumException() {
        super("Invalid checksum.");
    }

    public Bech32mInvalidChecksumException(List<String> candidates) {
        super("Invalid checksum. Error correction found these candidates for " +
        "corrected original string: \n" + String.join(",\n", candidates));
    }
}
