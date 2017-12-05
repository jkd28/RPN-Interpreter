import java.util.ArrayList;
import java.math.BigInteger;
import java.util.HashMap;

public class Statement {
    private String statementOutput;
    private Expression expression;
    private boolean isQuitStatement;
    private boolean isLetStatement;
    private boolean isPrintStatement;
    private boolean isError;
    private String variable;
    private int lineNumber;
    private HashMap<String, BigInteger> variableMap;

    public Statement(int lineNum, HashMap<String, BigInteger> variables){
        statementOutput = "";
        isQuitStatement = false;
        isLetStatement = false;
        isPrintStatement = false;
        isError = false;
        variable = "";
        lineNumber = lineNum;
        variableMap = variables;
    }

    // GETTERS
    public boolean isQuit() {
        return this.isQuitStatement;
    }

    public String getOutput() {
        return this.statementOutput;
    }

    public String getVariable() {
        return this.variable;
    }

    public boolean isError() {
        return this.isError;
    }

    public boolean isLet() {
        return this.isLetStatement;
    }

    public boolean isPrint() {
        return this.isPrintStatement;
    }

    // SETTERS
    private void setQuit() {
        this.isQuitStatement = true;
    }

    private void setLet() {
        this.isLetStatement = true;
    }

    private void setPrint() {
        this.isPrintStatement = true;
    }

    private void setError() {
        this.isError = true;
    }

    // Helper methods
    private String buildNewString(int startIndex, String[] array) {
        StringBuilder newLine = new StringBuilder();
        for (int i = startIndex; i < array.length; i++){
            newLine.append(array[i]);
            newLine.append(" ");
        }
        return new String(newLine);
    }

    private String handleLet(String[] brokenLine) {
        String builtOutput = "";

        if (brokenLine.length <= 2) {
            setError();
            builtOutput = "Line " + lineNumber + ": Operator LET applied to empty stack";
        }
        else if (!brokenLine[1].matches("^[A-z]$")) {
            setError();
            builtOutput = "Line " + lineNumber + ": Invalid variable name '" + brokenLine[1] + "'"  ;
        } else {
            this.variable = brokenLine[1];

            String newLine = buildNewString(2, brokenLine);

            expression = new Expression();
            String replacedLine = expression.replaceVariables(newLine, variableMap);
            expression.evaluate(replacedLine);

            if (expression.error()) {
                setError();
                builtOutput = "Line " + lineNumber + ": " + expression.getErrorMessage();
            } else {
                builtOutput = expression.getResult();
            }
        }
        return builtOutput;
    }

    private String handlePrint(String[] brokenLine) {
        String builtOutput = "";
        // We have a PRINT keyword, the rest of the line is the expression
        String newLine = buildNewString(1, brokenLine);
        expression = new Expression();
        String replacedLine = expression.replaceVariables(newLine, variableMap);
        expression.evaluate(replacedLine);

        if (expression.error()) {
            setError();
            builtOutput = "Line " + lineNumber + ": " + expression.getErrorMessage();
        } else {
            builtOutput = expression.getResult();
        }

        return builtOutput;
    }

    private String handleNoRecognizedKeyWord(String line) {
        String builtOutput = "";
        String[] lineArray = line.split(" ");

        // Check that the first entry contains letters and is larger than 1 character long (can't be a variable then)
        if ((lineArray[0].matches(".*[a-z].*")) && (lineArray[0].length() != 1)) {
            setError();
            builtOutput = "Line " + lineNumber + ": Unknown keyword " + lineArray[0];
            return builtOutput;
        }

        expression = new Expression();
        String replacedLine = expression.replaceVariables(line, variableMap);
        expression.evaluate(replacedLine);
        if (expression.error()) {
            setError();
            builtOutput = "Line " + lineNumber + ": " + expression.getErrorMessage();
        } else {
            builtOutput = expression.getResult();
        }
        return builtOutput;
    }

    // Evaluation Methods
    public String parseString(String line){
        String[] brokenLine = line.split(" ");

        // check for a keyword
        if (brokenLine[0].equals("let")) {
            setLet();
            this.statementOutput = handleLet(brokenLine);
        } else if (brokenLine[0].equals("quit")){
            setQuit();
            this.statementOutput = "";
        } else if (brokenLine[0].equals("print")) {
            setPrint();
            this.statementOutput = handlePrint(brokenLine);
        } else {
            this.statementOutput = handleNoRecognizedKeyWord(line);
        }
        return statementOutput;
    }
}
