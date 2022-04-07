package cz.fi.muni.pa193.jumiru.ui;

import cz.fi.muni.pa193.jumiru.bech32m.Bech32mException;
import cz.fi.muni.pa193.jumiru.bech32m.Bech32mIOData;
import cz.fi.muni.pa193.jumiru.bech32m.Bech32mModule;
import cz.fi.muni.pa193.jumiru.converter.DataInputConverter;
import cz.fi.muni.pa193.jumiru.converter.DataOutputConverter;

import java.io.*;
import java.util.List;
import java.util.Scanner;

import static cz.fi.muni.pa193.jumiru.bech32m.Bech32mModule.BENCH32M_MAX_LENGTH;


public class UserInterfaceModule{
    ArgParser argParser;

    public UserInterfaceModule(String[] args) {
        this.argParser = new ArgParser(args);
    }

    private String readDataFromFile(String filename){
        File inputFile =  new File(filename);
        if (!inputFile.isFile())
            throw new UserInterfaceException("The provided input file does not exist");
        /*
            The biggest possible legitimate file here should always be the data part in its binary
            form. This is binary data in regular text file, so each symbol 1/0 is 8 bytes and the
            maximal amount of them is the maximal bech32m length -1 for smallest possible HRP and
            -1 for the separator.
         */
        int binaryDataPartMaxLength = (BENCH32M_MAX_LENGTH-2)*8;
        if (inputFile.length() > binaryDataPartMaxLength){
            throw new UserInterfaceException("The file exceeds maximal allowed length");
        }
        try (FileInputStream fileInputStream = new FileInputStream(inputFile)) {
            byte[] chars = new byte[(int) inputFile.length()];
            int readBytes = fileInputStream.read(chars);
            if (readBytes != (int) inputFile.length())
                throw new UserInterfaceException("Some data from file could not be read " +
                        "successfully");
            return new String(chars);
        }
        catch (IOException e) {
            throw new UserInterfaceException("The read from file failed due to the following " +
                    "reason: " + e.getMessage());
        }
    }

    private void loadInputData() {
        switch (argParser.getInputDestination()) {
            case STDIN -> {
                System.out.println("Enter the data part to be en/decoded:");
                Scanner scanner = new Scanner(System.in);
                argParser.setInputData(scanner.nextLine());
                scanner.close();
            }
            case FILE -> argParser.setInputData(readDataFromFile(argParser.getInputFileName()));
        }
    }

    private Bech32mIOData convertFormatEncode(){
        Bech32mIOData bech32mIOData;
        DataInputConverter inputConverter = new DataInputConverter();
        switch (argParser.getDataFormat()) {
            case HEX -> bech32mIOData = new Bech32mIOData(argParser.getHumanReadablePart(),
                    argParser.getInputData());
            case BIN -> {
                String dataPartHex;
                dataPartHex = inputConverter.convertFromBinary(argParser.getInputData());
                bech32mIOData = new Bech32mIOData(argParser.getHumanReadablePart(), dataPartHex);
            }
            case BASE64 -> {
                List<Byte> dataPartBytes;
                dataPartBytes = inputConverter.convertFromBase64(argParser.getInputData());
                bech32mIOData = new Bech32mIOData(argParser.getHumanReadablePart(), dataPartBytes);
            }
            default -> throw new UserInterfaceException("Unsupported input type encountered while" +
                    " converting input data part");
        }
        return bech32mIOData;
    }

    private String convertFormatDecode(Bech32mIOData bech32mIOData){
        String result;
        DataOutputConverter outputConverter = new DataOutputConverter();
        result = switch (argParser.getDataFormat()) {
            case HEX -> outputConverter.convertToHex(bech32mIOData);
            case BIN -> outputConverter.convertToBinary(bech32mIOData);
            case BASE64 -> outputConverter.convertToBase64(bech32mIOData);
        };
        return result;
    }

    private void fileWriteResult(String outputFileName, String result){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            writer.write(result);
        } catch (IOException e) {
            throw new UserInterfaceException("Writing into the output file failed for the" +
                    " following reason: " + e.getMessage());
        }
        System.out.println("File " + outputFileName + " was created successfully");
    }

    private void outputResult(String result){
        switch (argParser.getOutputDestination()) {
            case STDOUT -> System.out.println("Decoded data part is: " + result);
            case FILE -> fileWriteResult(argParser.getOutputFileName(), result);
        }
    }

    private void entryPoint() {
        argParser.parse();
        loadInputData();
        String result;
        if (argParser.isEncode()){
            Bech32mIOData bech32mIOData;
            try {
                bech32mIOData = convertFormatEncode();
            }
            catch (NumberFormatException e) {
                throw new UserInterfaceException("The conversion of input data failed with for " +
                        "the following reason: " + e.getMessage());
            }
            result = new Bech32mModule().encodeBech32mString(bech32mIOData);
        } else {
            Bech32mIOData bech32mIOData = new Bech32mModule().decodeBech32mString(
                    argParser.getInputData(), argParser.isErrorDetection());
            result = convertFormatDecode(bech32mIOData);
        }
        outputResult(result);
    }

    public int entryPointWrapper() {
        try {
            entryPoint();
            return 0;
        }
        catch(UserInterfaceException | Bech32mException exc) {
            System.err.println(exc.getMessage());
            return 1;
        }
    }
}
