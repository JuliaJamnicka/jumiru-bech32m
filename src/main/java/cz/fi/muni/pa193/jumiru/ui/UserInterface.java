package cz.fi.muni.pa193.jumiru.ui;


public interface UserInterface {

    /**
     * This method parses the arguments provided to the UserInterface in its
     * constructor via an ArgParser class and performs all the necessary actions
     * specified by the arguments. It also wraps all the functionality and
     * catches defined exceptions to put them to standard error output.
     *
     * @return 0 if specified actions were performed successfully, 1 otherwise
     */
    int entryPointWrapper();
}