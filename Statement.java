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
            // We have a LET keyword
            if (brokenLine.length <= 2) {
                setError();
                statementOutput = "Line " + lineNumber + ": Operator LET applied to empty stack";

            }
            // We need to set a variable to some value here
            else if (!brokenLine[1].matches("^[A-z]$")) {
                // the next input is not a valid variable declaration
                setError();
                statementOutput = "Line " + lineNumber + ": Invalid variable name '" + brokenLine[1] + "'"  ;
            } else {
                // set variable has a value
                variable = brokenLine[1];

                StringBuilder newLine = new StringBuilder();
                for (int i = 2; i < brokenLine.length; i++) {
                    newLine.append(brokenLine[i]);
                    newLine.append(" ");
                }
                expression = new Expression(new String(newLine), variableMap);
                if (expression.error()) {
                    setError();
                    statementOutput = "Line " + lineNumber + ": " + expression.getErrorMessage();
                } else {
                    statementOutput = expression.getResult();
                }
            }

        } else if (brokenLine[0].equals("quit")){
            // We have a QUIT keyword
            setQuit();
        } else if (brokenLine[0].equals("print")) {
            // We have a PRINT keyword, the rest of the line is the expression
            StringBuilder newLine = new StringBuilder();
            for (int i = 1; i < brokenLine.length; i++) {
                newLine.append(brokenLine[i]);
                newLine.append(" ");
            }
            expression = new Expression(new String(newLine), variableMap);
            if (expression.error()) {
                setError();
                statementOutput = "Line " + lineNumber + ": " + expression.getErrorMessage();
            } else {
                statementOutput = expression.getResult();
            }
        } else {
            // No keyword, everything is part of the expression
            expression = new Expression(line, variableMap);
            if (expression.error()) {
                setError();
                statementOutput = "Line " + lineNumber + ": " + expression.getErrorMessage();
            } else {
                statementOutput = expression.getResult();
            }
        }
        return statementOutput;
    }
}
