package cz.fi.muni.pa193.jumiru.ui;

import static cz.fi.muni.pa193.jumiru.ui.UserInterfaceModule.BIN_DATA_MAX_LENGTH;

public final class ArgParser {
    private final String[] args;
    private int argIndex = 0;
    private boolean encode;
    enum DataPartFormatEnum { BIN, HEX, BASE64 }
    private DataPartFormatEnum dataFormat;
    enum IODestinationEnum { STDIN, ARG, EMPTYARG, FILE, STDOUT }
    private IODestinationEnum inputDestination;
    private IODestinationEnum outputDestination;
    private String inputFileName;
    private String outputFileName;
    private String humanReadablePart;
    private String inputData;
    private boolean errorDetection = false;

    public ArgParser(final String[] args) {
        this.args = args;
    }

    public boolean isEncode() {
        return encode;
    }

    public DataPartFormatEnum getDataFormat() {
        return dataFormat;
    }

    public IODestinationEnum getInputDestination() {
        return inputDestination;
    }

    public IODestinationEnum getOutputDestination() {
        return outputDestination;
    }

    public String getInputFileName() {
        return inputFileName;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public String getHumanReadablePart() {
        return humanReadablePart;
    }

    public String getInputData() {
        return inputData;
    }

    public void setInputData(final String inputData) {
        this.inputData = inputData;
    }

    public boolean isErrorDetection() {
        return errorDetection;
    }

    private void hasNextArg(final String errorMsg) {
        if (args.length <= argIndex) {
            throw new UserInterfaceException(errorMsg);
        }
    }

    private void parseEncodeDecode() {
        hasNextArg("Encode/decode argument is missing");
        switch (args[argIndex]) {
            case "encode" -> encode = true;
            case "decode" -> encode = false;
            default -> throw new UserInterfaceException("Argument " + argIndex + " ("
                    + args[argIndex] + ") must be encode/decode");
        }
        argIndex++;
    }

    private void parseDataFormat() {
        hasNextArg("Data format specification argument (bin/hex/base64) is missing");
        switch (args[argIndex]) {
            case "bin" -> dataFormat = DataPartFormatEnum.BIN;
            case "base64" -> dataFormat = DataPartFormatEnum.BASE64;
            case "hex" -> dataFormat = DataPartFormatEnum.HEX;
            default -> throw new UserInterfaceException("Argument " + argIndex + "("
                    + args[argIndex] + ") must be bin/base64/hex");
        }
        argIndex++;
    }

    private void parseFilename(boolean input) {
        argIndex++;
        if (input) {
            hasNextArg("FileName argument is missing");
        }
        if (input) {
            inputFileName = args[argIndex];
        } else {
            outputFileName = args[argIndex];
        }
    }

    private void parseDataPart() {
        argIndex++;
        hasNextArg("Data part argument is missing");
        if (args[argIndex].length() > BIN_DATA_MAX_LENGTH) {
            throw new UserInterfaceException("The data part in argument exceeds maximal allowed "
                    + "length");
        }
        inputData = args[argIndex];
    }

    private void parseIODestination(final boolean input) {
        hasNextArg("IO destination argument (stdin/file/arg/stdout) is missing");
        switch (args[argIndex]) {
            case "arg" -> {
                if (!input)
                    throw new UserInterfaceException("Error in argument " + argIndex + "("
                            + args[argIndex] + "): arg cannot be selected as output destination");
                inputDestination = IODestinationEnum.ARG;
                parseDataPart();
            }
            case "emptyarg" -> inputData = "";
            case "file" -> {
                if (input)
                    inputDestination = IODestinationEnum.FILE;
                else
                    outputDestination = IODestinationEnum.FILE;
                parseFilename(input);
            }
            case "stdin" -> {
                if (!input)
                    throw new UserInterfaceException("Error in argument " + argIndex + "("
                            + args[argIndex] + "): stdin cannot be selected as output destination");
                inputDestination = IODestinationEnum.STDIN;
            }
            case "stdout" -> {
                if (input)
                    throw new UserInterfaceException("Error in argument " + argIndex + "("
                            + args[argIndex] + "): stdout cannot be selected as input destination");
                outputDestination = IODestinationEnum.STDOUT;
            }
            default -> throw new UserInterfaceException("Argument " + argIndex + "("
                    + args[argIndex] + ") must be arg/stdin/file/stdout");
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
                throw new UserInterfaceException("The optional 'errdetect' argument expected and "
                        + "instead, '" + args[argIndex] + "' was provided");
            }
        }
    }

    public void parse() {
        parseEncodeDecode();
        parseDataFormat();
        parseIODestination(true);
        parseIODestination(false);
        if (encode)
            parseHRP();
        if (!encode)
            parseErrorDetection();
        if (args.length > argIndex) {
            throw new UserInterfaceException("Following argument " + args[argIndex - 1]
                    + ", there is one or more unnecessary arguments.");
        }
    }
}
