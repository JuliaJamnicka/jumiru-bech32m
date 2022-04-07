package cz.fi.muni.pa193.jumiru;

import cz.fi.muni.pa193.jumiru.ui.UserInterfaceModule;

public class Jumiru {
    public static void main(String[] args){
        int returnCode = new UserInterfaceModule(args).entryPointWrapper();
        System.exit(returnCode);
    }
}
