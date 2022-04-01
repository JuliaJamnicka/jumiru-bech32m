package cz.fi.muni.pa193.jumiru.ui;

import cz.fi.muni.pa193.jumiru.Bech32mException;
import cz.fi.muni.pa193.jumiru.UserInterfaceException;

public class UserInterfaceModule implements UserInterface{
    private boolean encode;
    private int argIndex = 0;
    private final String[] args;
    private enum dataPartFormatEnum {bin, hex, base64}
    private dataPartFormatEnum dataPartFormat;
    private enum IODestinationEnum {stdin, arg, file, stdout}
    private IODestinationEnum inputDestination;
    private IODestinationEnum outputDestination;
    private String inputFileName;
    private String outputFileName;
    private String HRP;



    public UserInterfaceModule(String[] args) {
        this.args = args;
    }

    private void hasNextArg(String errorMsg) {
        if (args.length <= argIndex) {
            throw new UserInterfaceException(errorMsg);
        }
    }

    private void parseEncodeDecode() {
        hasNextArg("Encode/decode argument is missing");
        switch (args[argIndex]) {
            case "encode":
                encode = true;
                break;
            case "decode":
                encode = false;
                break;
            default:
                throw new UserInterfaceException("Argument " + argIndex + " (" + args[argIndex] +
                        ") must be encode/decode");
        }
        argIndex++;
    }

    private void parseDataFormat() {
        hasNextArg("Data format specification argument (bin/hex/base64) is missing");
        switch (args[argIndex]) {
            case "bin":
                dataPartFormat = dataPartFormatEnum.bin;
                break;
            case "base64":
                dataPartFormat = dataPartFormatEnum.base64;
                break;
            case "hex":
                dataPartFormat = dataPartFormatEnum.hex;
                break;
            default:
                throw new UserInterfaceException("Argument " + argIndex + "(" + args[argIndex] +
                        ") must be bin/base64/hex");
        }
        argIndex++;
    }

    private void parseFilename(boolean input) {
        if (input){
            hasNextArg("FileName argument is missing");
        }
        if (input){
            inputFileName = args[argIndex];
        } else {
            outputFileName = args[argIndex];
        }
        argIndex++;
    }

    private void parseIODestination(boolean input) {
        hasNextArg("IO destination argument (stdin/file/arg/stdout) is missing");
        switch (args[argIndex]) {
            case "arg":
                if (input)
                    inputDestination = IODestinationEnum.arg;
                else
                    outputDestination = IODestinationEnum.arg;
                break;
            case "file":
                if (input)
                    inputDestination = IODestinationEnum.arg;
                else
                    outputDestination = IODestinationEnum.arg;
                parseFilename(input);
                break;
            case "stdin":
                if (!input)
                    throw new UserInterfaceException("Error in argument " + argIndex + "(" +
                            args[argIndex] + "): stdin cannot be selected for output destination");
                outputDestination = IODestinationEnum.stdin;
                break;
            case "stdout":
                if (input)
                    throw new UserInterfaceException("Error in argument " + argIndex + "(" +
                            args[argIndex] + "): stdout cannot be selected for input destination");
                inputDestination = IODestinationEnum.stdout;
                break;
            default:
                throw new UserInterfaceException("Argument " + argIndex + "(" + args[argIndex] +
                        ") must be arg/stdin/file/stdout");
        }
        argIndex++;
    }

    private void parseHRP() {
        hasNextArg("The HRP argument is missing");
    }

    private void entryPoint() {
        parseEncodeDecode();
        parseDataFormat();
        parseIODestination(true);
        parseIODestination(false);
        if (encode)
            parseHRP();
        if (args.length > argIndex + 1) {
            throw new UserInterfaceException("Following the argument: " + args[argIndex] +
                    ", there is one or more unnecessary arguments.");
        }
    }

    public void entryPointWrapper() {
        try {
            entryPoint();
        }
        catch(UserInterfaceException | Bech32mException exc) {
            System.out.println(exc.getMessage());
        }
    }
}
