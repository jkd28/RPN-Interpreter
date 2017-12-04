import java.util.Scanner;

public class RPN {
    public static int repl() {
        Scanner reader = new Scanner(System.in);
        boolean quitSignal = false;
        int errorReport = 0;
        String input;

        while (!quitSignal) {
            System.out.print("> ");
            input = (reader.nextLine()).trim().toLowerCase();
            Statement line = new Statement(input);

            quitSignal = line.isQuit();
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
