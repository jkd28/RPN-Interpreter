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
            Statement line = new Statement(lineNum, variableMap);
            String output = line.parseString(input);

            if (line.isLet() && !line.isError()) {
                variableMap.put(line.getVariable(), new BigInteger(output));
            }

            System.out.println(line.getOutput());
            quitSignal = line.isQuit();
            lineNum++;
        }
        return errorReport;
    }

    public static void main(String[] args) {
        int error = 0;
        if (args.length == 0) {
            error = repl();
        }
    }
}
