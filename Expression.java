import java.util.Stack;
import java.math.BigInteger;
import java.util.Scanner;
import java.util.HashMap;

public class Expression {
    private Stack<String> expressionStack;
    private String result;
    private String errorMessage;
    private boolean error;
    private HashMap<String, BigInteger> variableMap;

    public Expression(String statement, HashMap<String, BigInteger> variables) {
        expressionStack = new Stack<String>();
        variableMap = variables;
        error = false;
        errorMessage = "";

        evaluate(replaceVariables(statement, variableMap));
    }

    // This method takes a String and a HashMap of variables and replaces each instance of
    // a variable with its stored BigInteger value
    public String replaceVariables(String line, HashMap<String, BigInteger> variables) {
        if (line.matches(".*[a-z].*")){
            // the expression contains some variables, so we need to replace them
            char[] lineArray = line.toCharArray();
            for(int i = 0; i < lineArray.length; i++) {
                char operand = lineArray[i];
                if (operand == ' ') {
                    continue;
                }
                if (Character.isLetter(operand)) {
                    String strValue = Character.toString(operand);
                    if (variables.containsKey(strValue)) {
                        BigInteger value = variables.get(strValue);
                        lineArray[i] = value.toString().charAt(0);
                    } else {
                        System.out.println("SHOULD BE A VARIABLE, BUT IT'S NOT. IT IS NOT. OH HAI MARK");
                    }
                } else {
                    System.out.println("NOT A VARIABL");
                }
            }
            return new String(lineArray);
        } else {
            return line;
        }
    }

    // This method evaluates mathematical expressions assuming Reverse Polish Notation as
    // specified by the project
    // Return: The string form of the result of the expressions (or the proper error
    //         statement with error flags set)
    public void evaluate(String statement) {
        Scanner tokenReader = new Scanner(statement);
        boolean noOperand = true;
        while(tokenReader.hasNext()) {
            if (tokenReader.hasNextBigInteger()) {
                expressionStack.push(tokenReader.nextBigInteger().toString());
            } else {
                try {
                    noOperand = false;
                    BigInteger operand2 = new BigInteger(expressionStack.pop());
                    BigInteger operand1 = new BigInteger(expressionStack.pop());

                    String operator = tokenReader.next();

                    if (operator.equals("+")) {
                        expressionStack.push(operand1.add(operand2).toString());
                    } else if (operator.equals("-")) {
                        expressionStack.push(operand1.subtract(operand2).toString());
                    } else if (operator.equals("*")) {
                        expressionStack.push(operand1.multiply(operand2).toString());
                    } else if (operator.equals("/")) {
                        expressionStack.push(operand1.divide(operand2).toString());
                    } else {
                        throw new ArithmeticException();
                    }
                } catch (RuntimeException e){
                    error = true;
                    errorMessage = "Could not evaluate expression";
                    return;
                }
            }
        }

        result = expressionStack.pop();
        int count = 0;
        if (noOperand && !expressionStack.empty()){
            count = 1;
        }

        // handle possible error case
        while (!expressionStack.empty()) {
            count++;
            expressionStack.pop();
        }
        if (count != 0) {
            error = true;
            errorMessage = count + " elements in stack after evaluation";
        }
        return;
    }

    public String getResult() {
        return result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean error() {
        return error;
    }
}
