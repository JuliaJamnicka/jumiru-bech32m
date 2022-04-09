package cz.fi.muni.pa193.jumiru.converter;

import java.util.List;

public interface InputConverter {
    /**
     * Given a string with the data part in binary representation,
     * converts the data part to hexadecimal string.
     * (-> to be passed to Bech32mIOData constructor)
     *
     * @param  bech32mDataInput     string to convert
     * @return                      the data part converted to a list of bytes
     * @throws DataInputException if the input cannot be converted
     */
    List<Byte> convertFromBinary(String bech32mDataInput);

    /**
     * Given a string with the data part in binary representation,
     * converts the data part to hexadecimal string.
     * (-> to be passed to Bech32mIOData constructor)
     *
     * @param  bech32mDataInput     string to convert
     * @return                      the data part converted to a list of bytes
     * @throws DataInputException if the input cannot be converted
     */
    List<Byte> convertFromHex(String bech32mDataInput);

    /**
     * Given a Bech32m string with the data part in Base64 representation,
     * converts the data part to a list of bytes.
     * (to be passed to the Bech32mIOData contructor)
     *
     * @param  bech32mDataInput     string to convert
     * @return                      the data part converted to a list of bytes
     * @throws DataInputException if the input cannot be converted
     */
    List<Byte> convertFromBase64(String bech32mDataInput);

}
