package cz.fi.muni.pa193.jumiru.ui;

public interface UserInterface {

    class UserInterfaceException extends RuntimeException {
        public UserInterfaceException(String err) {
            super(err);
        }
    }
}
