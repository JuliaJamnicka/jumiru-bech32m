package cz.fi.muni.pa193.jumiru.bech32m;

public class Bech32mException extends RuntimeException {
    public Bech32mException(String err) {
        super(err);
    }
}
