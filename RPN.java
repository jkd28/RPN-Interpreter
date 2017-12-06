import java.util.Scanner;
import java.util.HashMap;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Locale;

public class RPN {
    public static int replMode() {
        Scanner reader = new Scanner(System.in);
        reader.useLocale(Locale.US);

        HashMap<String,BigInteger> variableMap = new HashMap<String, BigInteger>();
        boolean quitSignal = false;
        long lineNum = 1;
        String input;

        while (!quitSignal) {
            System.out.print("> ");
            input = (reader.nextLine()).trim().toLowerCase(Locale.US);
            if (input.equals("")) {
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
        HashMap<String,BigInteger> variableMap = new HashMap<String, BigInteger>();
        FileConcatenator fileCat = new FileConcatenator();

        // Create an array of lines for processing
        for (String filename : args) {
            fileCat.addFile(filename);

            // Handle potential errors in filereading
            if (fileCat.getErrorCode() != 0) {
                System.err.println(fileCat.getErrorMessage());
                return fileCat.getErrorCode();
            }
        }

        boolean quitSignal = false;
        long lineNum = 1;
        ArrayList<String> toEvaluate = fileCat.getCurrentFileString();
        // Evaluate each line
        for (String line : toEvaluate) {
            Statement eval = new Statement(lineNum, variableMap);
            eval.parseString(line);

            String output = eval.getOutput();
            if (eval.isLet() && !eval.isError()) {
                variableMap.put(eval.getVariable(), new BigInteger(output));
            }
            // Handle potential errors
            if (eval.isError()) {
                System.err.println(output);
                errorCode = eval.getErrorCode();
                break;
            } else if (eval.isPrint()){
                System.out.println(output);
            }
            quitSignal = eval.isQuit();
            if (quitSignal) {
                break;
            }
            lineNum++;
        }

        return errorCode;
    }

    public static void main(String[] args) {
        // Use try-catch to gracefully exit in case of unforseen errors
        try {
            int error = 0;
            if (args.length == 0) {
                error = replMode();
            } else {
                error = fileMode(args);
            }
            System.exit(error);
        } catch(OutOfMemoryError e) {
            System.err.println("ERROR: Size of file exceeds allotted memory.");
            System.exit(5);
        } catch (RuntimeException e ) {
            System.err.println("ERROR: An unexpected problem occurred.  Exiting...");
            System.exit(5);
        }
    }
}
