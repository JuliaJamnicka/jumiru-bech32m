package cz.fi.muni.pa193.jumiru.bech32m;

import java.util.List;

public class Bech32mInvalidChecksumException extends Bech32mException {

    public Bech32mInvalidChecksumException() {
        super("Provided bech32m string has incorrect checksum.");
    }

    public Bech32mInvalidChecksumException(List<String> candidates) {
        super("Provided bech32m string has incorrect checksum. Error correction found these " +
                "candidates for corrected original string: \n" + String.join(",\n", candidates));
    }
}
