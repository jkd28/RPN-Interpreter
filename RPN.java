import java.util.Scanner;
import java.util.HashMap;
import java.math.BigInteger;
import java.util.ArrayList;

public class RPN {
    public static int replMode() {
        Scanner reader = new Scanner(System.in);
        HashMap<String,BigInteger> variableMap = new HashMap<String, BigInteger>();
        boolean quitSignal = false;
        int lineNum = 1;
        String input;

        while (!quitSignal) {
            System.out.print("> ");
            input = (reader.nextLine()).trim().toLowerCase();
            if (input.equals("")) {
                lineNum++;
                continue;
            }
            Statement line = new Statement(lineNum, variableMap);
            line.parseString(input);
            String output = line.getOutput();
            if (line.isLet() && !line.isError()) {
                variableMap.put(line.getVariable(), new BigInteger(output));
            }

            if (line.isError()) {
                System.err.println(output);
            } else {
                System.out.println(output);
            }

            quitSignal = line.isQuit();
            lineNum++;
        }
        return 0;
    }

    public static int fileMode(String[] args) {
        int errorCode = 0;
        FileConcatenator fileCat = new FileConcatenator();

        for (String filename : args) {
            fileCat.addFile(filename);
            if (fileCat.getErrorCode() != 0) {
                System.out.println(fileCat.getErrorMessage());
                return fileCat.getErrorCode();
            }
            return fileCat.getErrorCode();
        }

        return errorCode;
    }

    public static void main(String[] args) {
        try {
            int error = 0;
            if (args.length == 0) {
                error = replMode();
            } else {
                //TODO add file handling
                error = fileMode(args);
            }
        } catch (RuntimeException e ) {
            System.err.println("An unexpected error occurred.  Exiting...");
            System.exit(5);
        }
    }
}
