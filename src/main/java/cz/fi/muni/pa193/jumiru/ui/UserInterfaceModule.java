package cz.fi.muni.pa193.jumiru.ui;

import cz.fi.muni.pa193.jumiru.bech32m.Bech32mException;
import cz.fi.muni.pa193.jumiru.bech32m.Bech32mIOData;
import cz.fi.muni.pa193.jumiru.bech32m.Bech32mModule;
import cz.fi.muni.pa193.jumiru.converter.DataInputConverter;
import cz.fi.muni.pa193.jumiru.converter.DataOutputConverter;

import java.io.*;
import java.util.List;
import java.util.Scanner;



public class UserInterfaceModule{
    ArgParser argParser;
    final int MAX_FILE_SIZE = 704; //The biggest possible legitimate file here is the one where it
                                    // represents the data part in binary format (8 bits per byte).
                                    // The longest data part can be is 88 characters long -> 8*88.

    public UserInterfaceModule(String[] args) {
        this.argParser = new ArgParser(args);
    }

    private String readDataFromFile(String filename){
        File inputFile =  new File(filename);
        if (!inputFile.isFile())
            throw new UserInterfaceException("The provided input file does not exist");
        try (FileInputStream fileInputStream = new FileInputStream(inputFile)) {
            byte[] chars = new byte[(int) inputFile.length()];
            int readBytes = fileInputStream.read(chars);
            if (readBytes != (int) inputFile.length())
                throw new UserInterfaceException("Some data from file could not be read " +
                        "successfully");
            fileInputStream.close();
            return new String(chars);
        }
        catch (IOException e) {
            throw new UserInterfaceException("The read from file failed due to the following " +
                    "reason: " + e.getMessage());
        }
    }

    private void loadInputData() {
        if (argParser.inputDestination == ArgParser.IODestinationEnum.STDIN) {
            System.out.println("Enter the data part to be en/decoded:");
            Scanner scanner = new Scanner(System.in);
            argParser.inputData = scanner.nextLine();
            scanner.close();
        }
        if (argParser.inputDestination == ArgParser.IODestinationEnum.FILE) {
            argParser.inputData = readDataFromFile(argParser.inputFileName);
        }
    }

    private Bech32mIOData convertFormatEncode(){
        Bech32mIOData bech32mIOData;
        DataInputConverter inputConverter = new DataInputConverter();
        switch (argParser.dataFormat) {
            case HEX:
                bech32mIOData = new Bech32mIOData(argParser.humanReadablePart,
                        argParser.inputData);
                break;
            case BIN:
                String dataPartHex;
                dataPartHex = inputConverter.convertFromBinary(argParser.inputData);
                bech32mIOData = new Bech32mIOData(argParser.humanReadablePart, dataPartHex);
                break;
            case BASE64:
                List<Byte> dataPartBytes;
                dataPartBytes = inputConverter.convertFromBase64(argParser.inputData);
                bech32mIOData = new Bech32mIOData(argParser.humanReadablePart, dataPartBytes);
                break;
            default:
                throw new UserInterfaceException("Unsupported input type encountered while" +
                        " converting input data part");
        }
        return bech32mIOData;
    }

    private String convertFormatDecode(Bech32mIOData bech32mIOData){
        String result;
        DataOutputConverter outputConverter = new DataOutputConverter();
        switch (argParser.dataFormat) {
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
        return result;
    }

    private void fileWriteResult(String result){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(argParser.outputFileName))) {
            writer.write(result);
        } catch (IOException e) {
            throw new UserInterfaceException("Writing into the output file failed for the" +
                    " following reason: " + e.getMessage());
        }
        System.out.println("File " + argParser.outputFileName + " was created successfully");
    }

    private void outputResult(String result){
        switch (argParser.outputDestination) {
            case STDOUT:
                System.out.println("Decoded data part is: " + result);
                break;
            case FILE:
                fileWriteResult(result);
                break;
        }
    }

    private void entryPoint() {
        argParser.parse();
        loadInputData();
        String result;
        if (argParser.encode){
            Bech32mIOData bech32mIOData = convertFormatEncode();
            result = new Bech32mModule().encodeBech32mString(bech32mIOData);
        } else {
            Bech32mIOData bech32mIOData = new Bech32mModule().decodeBech32mString(
                    argParser.inputData, argParser.errorDetection);
            result = convertFormatDecode(bech32mIOData);
        }
        outputResult(result);
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
