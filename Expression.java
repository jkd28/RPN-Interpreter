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

    public Expression() {
        expressionStack = new Stack<String>();
        variableMap = new HashMap<String,BigInteger>();
        error = false;
        errorMessage = "";
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
    // This method takes a String and a HashMap of variables and replaces each instance of
    // a variable with its stored BigInteger value
    public String replaceVariables(String line, HashMap<String, BigInteger> variables) {
        if (variables == null) {
            return "";
        }
        if (line.matches(".*[a-z].*")){
            // the expression contains some variables, so we need to replace them
            String[] lineArray = line.split(" ");
            for(int i = 0; i < lineArray.length; i++) {
                //ensure we only deal with individual characters
                if (lineArray[i].length() != 1){
                    continue;
                }

                char operand = lineArray[i].charAt(0);

                if (Character.isLetter(operand)) {
                    String strValue = Character.toString(operand);
                    if (variables.containsKey(strValue)) {
                        BigInteger value = variables.get(strValue);
                        line = line.replace(strValue, value.toString());
                    } else {
                        error = true;
                        errorMessage = "Variable " + operand + " is not initialized";
                        return errorMessage;
                    }
                }
            }
            return line;
        } else {
            return line;
        }
    }

    // This method evaluates mathematical expressions assuming Reverse Polish Notation as
    // specified by the project
    // Return: The string form of the result of the expressions (or the proper error
    //         statement with error flags set)
    public String evaluate(String statement) {
        if (error) {
            return this.errorMessage;
        }
        Scanner tokenReader = new Scanner(statement);
        boolean noOperand = true;
        BigInteger operand2 = new BigInteger("0");
        BigInteger operand1 = new BigInteger("0");
        String operator = "";

        while(tokenReader.hasNext()) {
            if (tokenReader.hasNextBigInteger()) {
                expressionStack.push(tokenReader.nextBigInteger().toString());
            } else {
                try {
                    noOperand = false;

                    operand2 = new BigInteger(expressionStack.pop());
                    operand1 = new BigInteger(expressionStack.pop());
                    operator = tokenReader.next();

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
                } catch(ArithmeticException e ) {
                    this.error = true;
                    this.errorMessage = "Could not evaluate expression";
                    return this.errorMessage;
                } catch (RuntimeException e){
                    this.error = true;
                    this.errorMessage = "Operator " +  operator + " applied to empty stack";
                    return this.errorMessage;
                }
            }
        }

        this.result = expressionStack.pop();
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
            this.error = true;
            this.errorMessage = count + " elements in stack after evaluation";
            return this.errorMessage;
        }
        return this.result;
    }
}
