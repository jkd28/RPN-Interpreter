import java.util.Stack;
import java.math.BigInteger;
import java.util.Scanner;
public class Expression {
    private Stack<String> expressionStack;
    private String result;
    private String errorMessage;
    private boolean error;

    public Expression(String statement) {
        expressionStack = new Stack<String>();
        evaluate(statement);
    }

    // This method evaluates mathematical expressions
    // assuming Reverse Polish Notation as specified by the project
    // Return: The string form of the result of the expressions.
    //         NULL if error
    public void evaluate(String statement) {
        Scanner tokenReader = new Scanner(statement);
        System.out.println(statement);
        while(tokenReader.hasNext()) {
            if (tokenReader.hasNextInt()) {
                expressionStack.push(Integer.toString(tokenReader.nextInt()));
            } else {
                BigInteger operand2 = new BigInteger(expressionStack.pop());
                System.out.println("Operand 2: " + operand2.toString());

                BigInteger operand1 = new BigInteger(expressionStack.pop());
                System.out.println("Operand 1: " + operand1.toString());

                String operator = tokenReader.next();
                System.out.println("Operator: " + operator);


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
            }
        }
        result = expressionStack.pop();
        int count = 0;
        while (!expressionStack.empty()) {
            count++;
            expressionStack.pop();
        }
        if (count != 0) {
            error = true;
            errorMessage = count + " elements in stack after evaluation";
        }
    }

    public String getResult() {
        return result;
    }
}
