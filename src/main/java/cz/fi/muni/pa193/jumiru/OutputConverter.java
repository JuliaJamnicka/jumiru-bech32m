package cz.fi.muni.pa193.jumiru;

public interface OutputConverter {
    /**
     * Given a Bech32mIOData object, returns the Bech32m string with the data part
     * in binary representation
     *
     * @param  data     Bech32m string to convert
     * @return          the Bech32m string with converted data part
     */
    String convertToBinary(Bech32mIOData data);
    
    /**
     * Given a Bech32mIOData object, returns the Bech32m string with the data part
     * in hexadecimal representation
     *
     * @param  data     Bech32m string to convert
     * @return          the Bech32m string with converted data part
     */
    String convertToHex(Bech32mIOData data);

    /**
     * Given a Bech32mIOData object, returns the Bech32m string with the data part
     * in Base64 representation
     *
     * @param  data     Bech32m string to convert
     * @return          the Bech32m string with converted data part
     */
    String convertToBase64(Bech32mIOData data);
}
