import java.util.ArrayList;
import java.math.BigInteger;
import java.util.HashMap;

public class Statement {
    private String statementOutput;
    private Expression expression;
    private boolean isQuitStatement;
    private boolean isLetStatement;
    private boolean isError;
    private String variable;
    private int lineNumber;
    private HashMap<String, BigInteger> variableMap;

    public Statement(int lineNum, HashMap<String, BigInteger> variables){
        statementOutput = "";
        isQuitStatement = false;
        isLetStatement = false;
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

    // SETTERS
    private void setQuit() {
        this.isQuitStatement = true;
    }

    private void setLet() {
        this.isLetStatement = true;
    }

    private void setError() {
        this.isError = true;
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
        } else if (brokenLine[0].equals("print")) {
            this.statementOutput = handlePrint(brokenLine);
        } else {
            this.statementOutput = handleNoRecognizedKeyWord(line);
        }
        return statementOutput;
    }

    public String handleLet(String[] brokenLine) {
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

            StringBuilder newLine = new StringBuilder();
            for (int i = 2; i < brokenLine.length; i++) {
                newLine.append(brokenLine[i]);
                newLine.append(" ");
            }
            expression = new Expression();
            String replacedLine = expression.replaceVariables(new String(newLine), variableMap);
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

    public String handlePrint(String[] brokenLine) {
        String builtOutput = "";
        // We have a PRINT keyword, the rest of the line is the expression
        StringBuilder newLine = new StringBuilder();
        for (int i = 1; i < brokenLine.length; i++) {
            newLine.append(brokenLine[i]);
            newLine.append(" ");
        }
        expression = new Expression();
        String replacedLine = expression.replaceVariables(new String(newLine), variableMap);
        expression.evaluate(replacedLine);

        if (expression.error()) {
            setError();
            builtOutput = "Line " + lineNumber + ": " + expression.getErrorMessage();
        } else {
            builtOutput = expression.getResult();
        }

        return builtOutput;
    }

    public String handleNoRecognizedKeyWord(String line) {
        String builtOutput = "";
        // No keyword, everything is part of the expression
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
}
