import java.util.Scanner;
import java.util.HashMap;
import java.math.BigInteger;

public class RPN {
    public static int repl() {
        Scanner reader = new Scanner(System.in);
        HashMap<String,BigInteger> variableMap = new HashMap<String, BigInteger>();
        boolean quitSignal = false;
        int errorReport = 0;
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
        return errorReport;
    }

    public static void main(String[] args) {
        try {
            int error = 0;
            if (args.length == 0) {
                error = repl();
            } else {
                //TODO add file handling
            }
        } catch (RuntimeException e ) {
            System.err.println("An unexpected error occurred.  Exiting...");
            System.exit(5);
        }
    }
}
