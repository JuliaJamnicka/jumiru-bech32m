package cz.fi.muni.pa193.jumiru.ui;

import static cz.fi.muni.pa193.jumiru.bech32m.Bech32mModule.BECH32M_MAX_LENGTH;

import cz.fi.muni.pa193.jumiru.bech32m.Bech32mException;
import cz.fi.muni.pa193.jumiru.bech32m.Bech32mIOData;
import cz.fi.muni.pa193.jumiru.bech32m.Bech32mModule;
import cz.fi.muni.pa193.jumiru.converter.DataInputConverter;
import cz.fi.muni.pa193.jumiru.converter.DataInputException;
import cz.fi.muni.pa193.jumiru.converter.DataOutputConverter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;


public final class UserInterfaceModule implements UserInterface {
    private final ArgParser argParser;
    private final String BASE_DIR = System.getProperty("user.dir");
    /*
        The biggest possible legitimate data prt length should always be the data part in its
        binary form. Each symbol 1/0 is regarded as 8 bits and the maximal amount of them is
        the maximal bech32m length -1 for smallest possible HRP and -1 for the separator.
    */
    public static final int BIN_DATA_MAX_LENGTH = (BECH32M_MAX_LENGTH - 2) * 8;

    public UserInterfaceModule(final String[] args) {
        this.argParser = new ArgParser(args);
    }

    private void validateFilePath (final String filePath){
        try {
            if (!Paths.get(filePath).toAbsolutePath().normalize().startsWith(BASE_DIR))
                throw new UserInterfaceException("The file path (" + filePath + ") must be in the"
                        + " current working directory: " + BASE_DIR);
        } catch (InvalidPathException e) {
            throw new UserInterfaceException("The file path (" + filePath + ") contains invalid"
                    + " characters");
        }
    }

    private String readDataFromFile(final String inputFileName) {
        validateFilePath(inputFileName);
        File inputFile =  new File(inputFileName);
        if (!inputFile.isFile())
            throw new UserInterfaceException("The provided input file does not exist or cannot be "
                    + "accessed");
        if (inputFile.length() > BIN_DATA_MAX_LENGTH) {
            throw new UserInterfaceException("The file exceeds maximal allowed length");
        }
        try (FileInputStream fileInputStream = new FileInputStream(inputFile)) {
            byte[] chars = new byte[(int) inputFile.length()];
            int readBytes = fileInputStream.read(chars);
            if (readBytes != (int) inputFile.length())
                throw new UserInterfaceException("Some data from file could not be read "
                        + "successfully");
            return new String(chars);
        } catch (IOException e) {
            throw new UserInterfaceException("The read from file failed due to the following "
                    + "reason: " + e.getMessage());
        }
    }

    private void loadInputData() {
        switch (argParser.getInputDestination()) {
            case STDIN -> {
                System.out.println("Enter the data part to be en/decoded:");
                Scanner scanner = new Scanner(System.in);
                try {
                    argParser.setInputData(scanner.nextLine());
                } catch (NoSuchElementException e) {
                    throw new UserInterfaceException("Unterminated input is not allowed here");
                }

                scanner.close();
                if (argParser.getInputData().length() > BIN_DATA_MAX_LENGTH) {
                    throw new UserInterfaceException("The data part exceeds maximal allowed "
                            + "length");
                }
            }
            case FILE -> argParser.setInputData(readDataFromFile(argParser.getInputFileName()));
        }
    }

    private Bech32mIOData convertFormatEncode() {
        DataInputConverter inputConverter = new DataInputConverter();
        List<Byte> dataPartBytes;
        switch (argParser.getDataFormat()) {
            case HEX -> dataPartBytes = inputConverter.convertFromHex(argParser.getInputData());
            case BIN -> dataPartBytes = inputConverter.convertFromBinary(argParser.getInputData());
            case BASE64 -> dataPartBytes = inputConverter.convertFromBase64(argParser.getInputData());
            default -> throw new UserInterfaceException("Unsupported input type encountered while"
                    + " converting input data part");
        }
        Bech32mIOData bech32mIOData;
        bech32mIOData = new Bech32mIOData(argParser.getHumanReadablePart(), dataPartBytes);
        return bech32mIOData;
    }

    private String convertFormatDecode(final Bech32mIOData bech32mIOData) {
        String result;
        DataOutputConverter outputConverter = new DataOutputConverter();
        result = switch (argParser.getDataFormat()) {
            case HEX -> outputConverter.convertToHex(bech32mIOData);
            case BIN -> outputConverter.convertToBinary(bech32mIOData);
            case BASE64 -> outputConverter.convertToBase64(bech32mIOData);
        };
        return result;
    }

    private void fileWriteResult(final String outputFileName, final String result) {
        validateFilePath(outputFileName);

        Path outputPath = Paths.get(outputFileName).toAbsolutePath().normalize();
        try {
            Files.createDirectories(outputPath.getParent());
        } catch (IOException e) {
            throw new UserInterfaceException("Creation of directories leading to output file "
                    + "failed");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            writer.write(result);
        } catch (IOException e) {
            throw new UserInterfaceException("Writing into the output file failed for the"
                    + " following reason: " + e.getMessage());
        }
        System.out.println("File " + outputFileName + " was created successfully");
    }

    private void outputResult(final String result) {
        switch (argParser.getOutputDestination()) {
            case STDOUT -> System.out.println(result);
            case FILE -> fileWriteResult(argParser.getOutputFileName(), result);
        }
    }

    private void entryPoint() {
        argParser.parse();
        loadInputData();
        String result;
        if (argParser.isEncode()) {
            Bech32mIOData bech32mIOData;
            try {
                bech32mIOData = convertFormatEncode();
            } catch (DataInputException e) {
                throw new UserInterfaceException("The conversion of input data failed with for "
                        + "the following reason: " + e.getMessage());
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
        } catch (UserInterfaceException exc) {
            System.err.println(exc.getMessage() + System.lineSeparator() + System.lineSeparator()
                    + "For correct usage, see README.md file");
            return 1;
        } catch (Bech32mException exc) {
            System.err.println(exc.getMessage());
            return 1;
        }
    }
}
