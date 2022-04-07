package cz.fi.muni.pa193.jumiru.converter;

public class DataInputException extends RuntimeException{
    public DataInputException(String input) {
        super(input + " is not a valid input and cannot be converted to Bech32m");
    }
}
