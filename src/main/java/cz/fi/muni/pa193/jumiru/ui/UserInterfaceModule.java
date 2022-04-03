package cz.fi.muni.pa193.jumiru.ui;

import cz.fi.muni.pa193.jumiru.*;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class UserInterfaceModule implements UserInterface{
    private boolean encode;
    private int argIndex = 0;
    private final String[] args;
    private enum dataPartFormatEnum {BIN, HEX, BASE64}
    private dataPartFormatEnum dataFormat;
    private enum IODestinationEnum {STDIN, ARG, FILE, STDOUT}
    private IODestinationEnum inputDestination;
    private IODestinationEnum outputDestination;
    private String inputFileName;
    private String outputFileName;
    private String humanReadablePart;
    private String inputData;
    private boolean errorDetection = false;


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
                dataFormat = dataPartFormatEnum.BIN;
                break;
            case "base64":
                dataFormat = dataPartFormatEnum.BASE64;
                break;
            case "hex":
                dataFormat = dataPartFormatEnum.HEX;
                break;
            default:
                throw new UserInterfaceException("Argument " + argIndex + "(" + args[argIndex] +
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
                    throw new UserInterfaceException("Error in argument " + argIndex + "(" +
                            args[argIndex] + "): arg cannot be selected as output destination");
                inputDestination = IODestinationEnum.ARG;
                parseDataPart();
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
                    throw new UserInterfaceException("Error in argument " + argIndex + "(" +
                            args[argIndex] + "): stdin cannot be selected as output destination");
                inputDestination = IODestinationEnum.STDIN;
                break;
            case "stdout":
                if (input)
                    throw new UserInterfaceException("Error in argument " + argIndex + "(" +
                            args[argIndex] + "): stdout cannot be selected as input destination");
                outputDestination = IODestinationEnum.STDOUT;
                break;
            default:
                throw new UserInterfaceException("Argument " + argIndex + "(" + args[argIndex] +
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
                throw new UserInterfaceException("The optional 'errdetect' argument expected and " +
                        "instead, '" + args[argIndex] + "' was provided");
            }
        }
    }

    private void loadInputData() {
        if (inputDestination == IODestinationEnum.STDIN) {
            System.out.println("Enter the data part to be encoded:");
            Scanner scanner = new Scanner(System.in);
            inputData = scanner.nextLine();
            scanner.close();
        }
        if (inputDestination == IODestinationEnum.FILE) {
            File inputFile =  new File(inputFileName);
            if (!inputFile.isFile())
                throw new UserInterfaceException("The provided input file does not exist");
            try {
                FileInputStream fileInputStream = new FileInputStream(inputFile);
                byte[] chars = new byte[(int) inputFile.length()];
                int readBytes = fileInputStream.read(chars);
                if (readBytes != (int) inputFile.length())
                    throw new UserInterfaceException("Some data from file could not be read " +
                            "successfully");
                fileInputStream.close();
                inputData = new String(chars);
            }
            catch (IOException e) {
                throw new UserInterfaceException("The read from file failed due to the following " +
                        "reason: " + e.getMessage());
            }
        }
    }

    private void entryPoint() {
        parseEncodeDecode();
        parseDataFormat();
        parseIODestination(true);
        parseIODestination(false);
        if (encode)
            parseHRP();
        if (!encode)
            parseErrorDetection();
        if (args.length > argIndex) {
            throw new UserInterfaceException("Following argument " + args[argIndex-1] +
                    ", there is one or more unnecessary arguments.");
        }
        loadInputData();

        String result;
        if (encode){
            //TODO put this into separate method
            Bech32mIOData bech32mIOData;
            DataInputConverter inputConverter = new DataInputConverter();
            switch (dataFormat) {
                case HEX:
                    bech32mIOData = new Bech32mIOData(humanReadablePart, inputData);
                    break;
                case BIN:
                    String dataPartHex;
                    dataPartHex = inputConverter.convertFromBinary(inputData);
                    bech32mIOData = new Bech32mIOData(humanReadablePart, dataPartHex);
                    break;
                case BASE64:
                    List<Byte> dataPartBytes;
                    dataPartBytes = inputConverter.convertFromBase64(inputData);
                    bech32mIOData = new Bech32mIOData(humanReadablePart, dataPartBytes);
                    break;
                default:
                    throw new UserInterfaceException("Unsupported input type encountered while" +
                            " converting input data part");
            }
            result = new Bech32mModule().encodeBech32mString(bech32mIOData);
        } else {
            Bech32mIOData bech32mIOData = new Bech32mModule().decodeBech32mString(inputData); //TODO add the error detection
            DataOutputConverter outputConverter = new DataOutputConverter();
            switch (dataFormat) {
                case HEX:
                    result = outputConverter.convertToHex(bech32mIOData);
                    break;
                case BIN:
                    result = outputConverter.convertToBinary(bech32mIOData);
                    break;
                case BASE64:
                    result = outputConverter.convertToBase64(bech32mIOData);
                    break;
                default:
                    throw new UserInterfaceException("Unsupported output type encountered while" +
                            " converting output data");
            }
        }

        switch (outputDestination) { //TODO put this into separate method
            case STDOUT:
                System.out.println(result);
                break;
            case FILE:
                String str = "Hello";
                FileWriter fw;
                try {
                     fw = new FileWriter(outputFileName);
                } catch (IOException e) {
                    throw new UserInterfaceException("The output file could not be created due " +
                            "to the following reason: " + e.getMessage());
                }
                BufferedWriter writer = new BufferedWriter(fw);
                try {
                    writer.write(str);
                } catch (IOException e) {
                    throw new UserInterfaceException("Writing into the output file failed for the" +
                            " following reason: " + e.getMessage());
                }
                try {
                    writer.close();
                } catch (IOException e) {
                    throw new UserInterfaceException("The output file could not be closed for the" +
                            " following reason: " + e.getMessage());
                }
                break;
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
