package cz.fi.muni.pa193.jumiru;

import java.util.List;

public interface InputConverter {
    /**
     * Given a string with the data part in binary representation,
     * converts the data part to hexadecimal string 
     * (-> to be passed to Bech32mIOData constructor hex variant)
     *
     * @param  bech32mDataInput     string to convert
     * @return                      the Bech32m string with converted data part
     */
    String convertFromBinary(String bech32mDataInput);

    /**
     * Given a Bech32m string with the data part in Base64 representation,
     * converts the data part to a list of bytes
     * (to be passed to the Bech32mIOData contructor List<Byte> variant)
     *
     * @param  bech32mDataInput     string to convert
     * @return                      the Bech32m string with converted data part
     */
    List<Byte> convertFromBase64(String bech32mDataInput);

}
