import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Scanner;
import java.util.Locale;

public class FileConcatenator {
    ArrayList<String> combinedFileLines;
    String errorMessage;
    int errorCode;

    public FileConcatenator() {
        combinedFileLines = new ArrayList<String>();
        errorMessage = "";
        errorCode = 0;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public ArrayList<String> getCurrentFileString() {
        return this.combinedFileLines;
    }

    public ArrayList<String> addFile(String filename) {
        String entireFile = readLinesFromFile(filename);
        Scanner lineReader = new Scanner(entireFile);
        while(lineReader.hasNextLine()) {
            combinedFileLines.add(lineReader.nextLine());
        }
        return combinedFileLines;
    }

    public String readLinesFromFile(String filename) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(filename));
            String entireFile = new String(encoded, "UTF-8");
            return entireFile.toLowerCase(Locale.US);
        } catch (NoSuchFileException e) {
            this.errorMessage = "Could not find file '" + filename + "'";
            this.errorCode = 5;
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            this.errorMessage = "Error reading from file";
            this.errorCode = 5;
            return "";
        }
    }
}
