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

    public UserInterfaceModule(String[] args) {
        this.argParser = new ArgParser(args);
    }

    private void loadInputData() {
        if (argParser.inputDestination == ArgParser.IODestinationEnum.STDIN) {
            System.out.println("Enter the data part to be en/decoded:");
            Scanner scanner = new Scanner(System.in);
            argParser.inputData = scanner.nextLine();
            scanner.close();
        }
        if (argParser.inputDestination == ArgParser.IODestinationEnum.FILE) {
            File inputFile =  new File(argParser.inputFileName);
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
                argParser.inputData = new String(chars);
            }
            catch (IOException e) {
                throw new UserInterfaceException("The read from file failed due to the following " +
                        "reason: " + e.getMessage());
            }
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
        FileWriter fw;
        try {
            fw = new FileWriter(argParser.outputFileName);
        } catch (IOException e) {
            throw new UserInterfaceException("The output file could not be created due " +
                    "to the following reason: " + e.getMessage());
        }
        BufferedWriter writer = new BufferedWriter(fw);
        try {
            writer.write(result);
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
