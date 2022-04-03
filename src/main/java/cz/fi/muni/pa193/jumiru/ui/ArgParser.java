package cz.fi.muni.pa193.jumiru.ui;

public class ArgParser {
    private final String[] args;
    private int argIndex = 0;
    protected boolean encode;
    protected enum dataPartFormatEnum {BIN, HEX, BASE64}
    protected dataPartFormatEnum dataFormat;
    protected enum IODestinationEnum {STDIN, ARG, EMPTYARG, FILE, STDOUT}
    protected IODestinationEnum inputDestination;
    protected IODestinationEnum outputDestination;
    protected String inputFileName;
    protected String outputFileName;
    protected String humanReadablePart;
    protected String inputData;
    protected boolean errorDetection = false;

    public ArgParser(String[] args) {
        this.args = args;
    }

    private void hasNextArg(String errorMsg) {
        if (args.length <= argIndex) {
            throw new UserInterface.UserInterfaceException(errorMsg);
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
                throw new UserInterface.UserInterfaceException("Argument " + argIndex + " (" + args[argIndex] +
                        ") must be encode/decode");
        }
        argIndex++;
    }

    private void parseDataFormat() {
        hasNextArg("Data format specification argument (bin/hex/base64) is missing");
        switch (args[argIndex]) {
            case "bin":
                dataFormat = dataPartFormatEnum.BIN;
                break;
            case "base64":
                dataFormat = dataPartFormatEnum.BASE64;
                break;
            case "hex":
                dataFormat = dataPartFormatEnum.HEX;
                break;
            default:
                throw new UserInterface.UserInterfaceException("Argument " + argIndex + "(" + args[argIndex] +
                        ") must be bin/base64/hex");
        }
        argIndex++;
    }

    private void parseFilename(boolean input) {
        argIndex++;
        if (input){
            hasNextArg("FileName argument is missing");
        }
        if (input){
            inputFileName = args[argIndex];
        } else {
            outputFileName = args[argIndex];
        }
    }

    private void parseDataPart() {
        argIndex++;
        hasNextArg("Data part argument is missing");
        inputData = args[argIndex];
    }

    private void parseIODestination(boolean input) {
        hasNextArg("IO destination argument (stdin/file/arg/stdout) is missing");
        switch (args[argIndex]) {
            case "arg":
                if (!input)
                    throw new UserInterface.UserInterfaceException("Error in argument " + argIndex + "(" +
                            args[argIndex] + "): arg cannot be selected as output destination");
                inputDestination = IODestinationEnum.ARG;
                parseDataPart();
                break;
            case "emptyarg":
                inputData = "";
                break;
            case "file":
                if (input)
                    inputDestination = IODestinationEnum.FILE;
                else
                    outputDestination = IODestinationEnum.FILE;
                parseFilename(input);
                break;
            case "stdin":
                if (!input)
                    throw new UserInterface.UserInterfaceException("Error in argument " + argIndex + "(" +
                            args[argIndex] + "): stdin cannot be selected as output destination");
                inputDestination = IODestinationEnum.STDIN;
                break;
            case "stdout":
                if (input)
                    throw new UserInterface.UserInterfaceException("Error in argument " + argIndex + "(" +
                            args[argIndex] + "): stdout cannot be selected as input destination");
                outputDestination = IODestinationEnum.STDOUT;
                break;
            default:
                throw new UserInterface.UserInterfaceException("Argument " + argIndex + "(" + args[argIndex] +
                        ") must be arg/stdin/file/stdout");
        }
        argIndex++;
    }

    private void parseHRP() {
        hasNextArg("The HRP argument is missing");
        humanReadablePart = args[argIndex];
        argIndex++;
    }

    private void parseErrorDetection() {
        if (args.length > argIndex) {
            if  (args[argIndex].equals("errdetect")) {
                errorDetection = true;
                argIndex++;
            } else {
                throw new UserInterface.UserInterfaceException("The optional 'errdetect' argument expected and " +
                        "instead, '" + args[argIndex] + "' was provided");
            }
        }
    }

    public void parse(){
        parseEncodeDecode();
        parseDataFormat();
        parseIODestination(true);
        parseIODestination(false);
        if (encode)
            parseHRP();
        if (!encode)
            parseErrorDetection();
        if (args.length > argIndex) {
            throw new UserInterface.UserInterfaceException("Following argument " + args[argIndex-1] +
                    ", there is one or more unnecessary arguments.");
        }
    }
}
