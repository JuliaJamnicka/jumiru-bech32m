package cz.fi.muni.pa193.jumiru.bech32m;

import java.util.List;

public interface Bech32mTransformer {

    /**
     * Validate whether string adheres to Bech32m reference criteria (e.g. length of HRP).
     * DOES NOT CHECK WHETHER CHECKSUM IS CORRECT!
     *
     * @param  str  string to check
     * @return      void
     * @throws Bech32mException for invalid string
     */
    void checkBech32mString(String str);

    /**
     * Verify whether checksum of Bech32m string is correct.
     *
     * @param  hrPart    human-readable part of string to check
     * @param  dataPart  decoded bech32m data part of string (list of bytes with according values)
     * @return           true if checksum of  string is correct, false otherwise
     */
    boolean verifyChecksum(String hrPart, List<Byte> dataPart);

    /**
     * Decode a Bech32m string. If parameter performErrorCorrection is set to true and checksum
     * verification of provided Bech32m string fails, function performs error correction - it provides
     * candidates for provided string that differ from it in one symbol and pass checksum verification.
     * They are specified as part of thrown Bech32mException.
     *
     * @param  str                     string to be decoded
     * @param  performErrorCorrection  specifies whether to perform error correction
     * @return                         content of decoded string as Bech32mIOData structure.
     */
    Bech32mIOData decodeBech32mString(String str, boolean performErrorCorrection);

    /**
     * Encode input data to Bech32m string.
     *
     * @param  input  Bech32mIOData structure containing data to be encoded
     * @return        Bech32m string (lowercase)
     */
    String encodeBech32mString(Bech32mIOData input);

    /**
     * Calculate checksum for Bech32m input data.
     *
     * @param  input  Bech32mIOData structure containing data to compute checksum for
     * @return        checksum as list of bytes
     */
    List<Byte> calculateChecksum(Bech32mIOData input);
}
