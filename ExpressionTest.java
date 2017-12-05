import org.junit.*;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.math.BigInteger;

public class ExpressionTest {
    HashMap<String, BigInteger> testMap;
    Expression testExpression;

    @Before
    public void setup() {
        testMap = new HashMap<String, BigInteger>();
        testExpression = new Expression();
    }

    /******************************** replaceVariables() TESTS **************************************/
    // Test that the replaceVariables method does in fact replace
    // variables when they all exist in the HashMap
    @Test
    public void testReplaceWithExistingVariables() {
        testMap.put("a", new BigInteger("1"));
        testMap.put("b", new BigInteger("3"));
        testMap.put("c", new BigInteger("5"));
        testMap.put("z", new BigInteger("4"));

        String testString = "a b + c - z *";
        String expected = "1 3 + 5 - 4 *";
        String observed = testExpression.replaceVariables(testString, testMap);

        assertEquals(expected, observed);
    }

    // Test that the replaceVariables method returns a string saying
    // that the given value is not an intitialized variable when
    // the variable is not present in the HashMap
    @Test
    public void testReplaceWithNonexistantVariable() {
        testMap.put("a", new BigInteger("1"));
        testMap.put("b", new BigInteger("3"));

        String testString = "a b + c - z *";
        String expected = "Variable c is not initialized";
        String observed = testExpression.replaceVariables(testString, testMap);

        assertEquals(expected, observed);
    }

    // Test that the replaceVariables method returns an empty string if the provided
    // HashMap is null
    @Test
    public void testReplaceNullMap() {
        assertEquals("", testExpression.replaceVariables("a b +", null));
    }

    // Test that the replaceVariables does nothing to a string when variables are
    // not included in the input
    @Test
    public void testReplaceNoReplacements() {
        testMap.put("a", new BigInteger("1"));
        testMap.put("b", new BigInteger("3"));
        testMap.put("c", new BigInteger("5"));
        testMap.put("z", new BigInteger("4"));

        String testString = "1 12 + 3 - 4 *";
        String expected = "1 12 + 3 - 4 *";
        String observed = testExpression.replaceVariables(testString, testMap);
        assertEquals(expected, observed);
    }

    // Test that the replacement works when there is a mix of numbers and variables
    @Test
    public void testReplaceMixture() {
        testMap.put("a", new BigInteger("1"));
        testMap.put("b", new BigInteger("3"));
        testMap.put("c", new BigInteger("5"));
        testMap.put("z", new BigInteger("4"));

        String testString = "1 c + b - 4 *";
        String expected = "1 5 + 3 - 4 *";
        String observed = testExpression.replaceVariables(testString, testMap);
        assertEquals(expected, observed);
    }

    // Test that replace works for very large numbers
    @Test
    public void testReplaceLargeNumbers() {
        testMap.put("a", new BigInteger("99999999999999999999999"));
        testMap.put("b", new BigInteger("11111111111111111111111"));

        String testString = "a b +";
        String expected = "99999999999999999999999 11111111111111111111111 +";
        String observed = testExpression.replaceVariables(testString, testMap);
        assertEquals(expected, observed);
    }

    // Test that replace works for a combination of large and small numbers
    @Test
    public void testReplaceLargeAndSmallNumbers() {
        testMap.put("a", new BigInteger("99999999999999999999999"));
        testMap.put("b", new BigInteger("3"));

        String testString = "a b +";
        String expected = "99999999999999999999999 3 +";
        String observed = testExpression.replaceVariables(testString, testMap);
        assertEquals(expected, observed);
    }

    // Test that replace returns an empty string when sent an empty string
    @Test
    public void testReplaceEmptyString() {
        testMap.put("a", new BigInteger("4"));
        testMap.put("b", new BigInteger("3"));

        String testString = "";
        String expected = "";
        String observed = testExpression.replaceVariables("", testMap);
    }

    /******************************** replaceVariables TESTS **************************************/
    // Test that the evaluate method works for very simple expressions
    @Test
    public void testEvaluateBasicExpression(){
        String testString = "2 2 +";
        String expected = "4";
        String observed = testExpression.evaluate(testString);
        assertEquals(expected, observed);
    }

    // Test that the evaluate method works for more complex expressions
    @Test
    public void testEvaluateComplexExpression() {
        String testString = "2 3 * 5 14 * / 10 + 30 - 15 + 4 - 25 4 * +";
        String expected = "91";
        String observed = testExpression.evaluate(testString);
        assertEquals(expected, observed);
    }

    // Test that the evaluate method works for exceptionally large numbers
    // in basic expressions
    @Test
    public void testEvaluateLargeNumbersBasic() {
        String testString = "99999999999999999 99999999999999999 *";
        String expected = "9999999999999999800000000000000001";
        String observed = testExpression.evaluate(testString);
        assertEquals(expected, observed);
    }

    // Test that the evaluate method works for exceptionally large numbers
    // in rather comlpex expressions
    @Test
    public void testEvaluateLargeNumbersComplex() {
        String testString = "99999999999999999999 999999999999999999 * 1000000000000000 - 45000000000000 2000000000000 / + 3000000000000 + 2 *";
        String expected = "199999999999999999797998006000000000046";
        String observed = testExpression.evaluate(testString);
        assertEquals(expected, observed);
    }

    // Test that the method properly returns the number of remaining items in the stack
    // for an invalid expression
    @Test
    public void testEvaluateRemainingOnStack() {
        String testString = "2 3 4";
        String expected = "3 elements in stack after evaluation";
        String observed = testExpression.evaluate(testString);
        assertEquals(expected, observed);
    }

    // Test that evaluate properly returns the amount of items left on the stack
    // after a seemingly valid (mistakenly invalid) expression
    @Test
    public void testEvaluateRemainingFromSeeminglyValidExpression() {
        String testString = "2 2 + 3";
        String expected = "1 elements in stack after evaluation";
        String observed = testExpression.evaluate(testString);
        assertEquals(expected, observed);
    }

    // Test that evaluate properly returns an error message on invalid input
    @Test
    public void testEvaluateInvalidInput() {
        String testString = "4 3 LET + a";
        String expected = "Could not evaluate expression";
        String observed = testExpression.evaluate(testString);
        assertEquals(expected, observed);
    }

    // Test that division by zero is handled
    @Test
    public void testEvaluateDivisionByZero() {
        String testString = "1 0 /";
        String expected = "Could not evaluate expression";
        String observed = testExpression.evaluate(testString);
        assertEquals(expected, observed);
    }

    // Test that proper message is output for the incorrect inut
    @Test
    public void testEvaluateInvalidOperator() {
        String testString = " 1 2 + +";
        String expected = "Operator + applied to empty stack";
        String observed = testExpression.evaluate(testString);
        assertEquals(expected, observed);
    }

}
