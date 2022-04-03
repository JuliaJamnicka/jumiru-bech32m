package cz.fi.muni.pa193.jumiru.ui;

import cz.fi.muni.pa193.jumiru.Bech32mException;
import cz.fi.muni.pa193.jumiru.UserInterfaceException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

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
    private String inputDataPart;



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
        inputDataPart = args[argIndex];
    }

    private void parseIODestination(boolean input) {
        hasNextArg("IO destination argument (stdin/file/arg/stdout) is missing");
        switch (args[argIndex]) {
            case "arg":
                if (!input)
                    throw new UserInterfaceException("Error in argument " + argIndex + "(" +
                            args[argIndex] + "): arg cannot be selected as output destination");
                inputDestination = IODestinationEnum.arg;
                parseDataPart();
                break;
            case "file":
                if (input)
                    inputDestination = IODestinationEnum.file;
                else
                    outputDestination = IODestinationEnum.file;
                parseFilename(input);
                break;
            case "stdin":
                if (!input)
                    throw new UserInterfaceException("Error in argument " + argIndex + "(" +
                            args[argIndex] + "): stdin cannot be selected as output destination");
                inputDestination = IODestinationEnum.stdin;
                break;
            case "stdout":
                if (input)
                    throw new UserInterfaceException("Error in argument " + argIndex + "(" +
                            args[argIndex] + "): stdout cannot be selected as input destination");
                outputDestination = IODestinationEnum.stdout;
                break;
            default:
                throw new UserInterfaceException("Argument " + argIndex + "(" + args[argIndex] +
                        ") must be arg/stdin/file/stdout");
        }
        argIndex++;
    }

    private void parseHRP() {
        hasNextArg("The HRP argument is missing");
        HRP = args[argIndex];
        argIndex++;
    }

    private void loadInputData() {
        if (inputDestination == IODestinationEnum.stdin) {
            System.out.println("Enter the data part to be encoded:");
            Scanner scanner = new Scanner(System.in);
            inputDataPart = scanner.nextLine();
            scanner.close();
        }
        if (inputDestination == IODestinationEnum.file) {
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
                inputDataPart = new String(chars);
            }
            catch (IOException e) {
                throw new UserInterfaceException("The read from file failed due to the following exception: " + e.getMessage());
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
        if (args.length > argIndex) {
            throw new UserInterfaceException("Following the argument: " + args[argIndex] +
                    ", there is one or more unnecessary arguments.");
        }

        loadInputData();
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
