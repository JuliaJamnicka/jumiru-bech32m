package cz.fi.muni.pa193.jumiru.converter;

public class DataInputException extends RuntimeException {
    /**
     * The exception thrown if the conversion of the given data fails.
     *
     * @param input Error message
     */
    public DataInputException(final String input) {
        super(input + " is not a valid input and cannot be converted to Bech32m");
    }
}
