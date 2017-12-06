import org.junit.*;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.math.BigInteger;

public class StatementTest {
    Statement testStatement;
    HashMap<String,BigInteger> testMap;

    @Before
    public void setup() {
        testMap = new HashMap<String, BigInteger>();
        testStatement = new Statement(2, testMap);
    }

    @Test
    public void testStatementNoKeywordSimpleExpression() {
        String expected = "4";
        String observed = testStatement.parseString("2 2 +");
        assertEquals(expected, observed);
        assertFalse(testStatement.isError());
    }

    @Test
    public void testStatementInvalidKeyword() {
        String expected = "Line 2: Unknown keyword loop";
        String observed = testStatement.parseString("loop");
        assertEquals(expected, observed);
        assertTrue(testStatement.isError());
    }

    @Test
    public void testStatementGarbage() {
        String expected = "Line 2: Unknown keyword garbasdklj";
        String observed = testStatement.parseString("garbasdklj sdasdl ajer pjeskfljasff");
        assertEquals(expected, observed);
        assertTrue(testStatement.isError());
    }

    @Test
    public void testStatementValidLet() {
        String expected = "12";
        String observed = testStatement.parseString("let a 6 6 +");
        assertEquals(expected, observed);
        assertTrue(testStatement.isLet());
        assertFalse(testStatement.isError());
    }

    @Test
    public void testStatementValidLetLargeNumbers(){
        String expected = "9999999999999999999999999999999999999";
        String observed = testStatement.parseString("let x 9999999999999999999999999999999999999");
        assertEquals(expected, observed);
        assertTrue(testStatement.isLet());
        assertFalse(testStatement.isError());
    }

    @Test
    public void testStatementEmptyStackLet() {
        String expected = "Line 2: Operator LET applied to empty stack";
        String observed = testStatement.parseString("let a");
        assertEquals(expected, observed);
        assertTrue(testStatement.isLet());
        assertTrue(testStatement.isError());
    }

    @Test
    public void testStatementInvalidExpressionLet() {
        String expected = "Line 2: Could not evaluate expression";
        String observed = testStatement.parseString("let a 3 2 garbage");
        assertEquals(expected, observed);
        assertTrue(testStatement.isLet());
        assertTrue(testStatement.isError());
    }

    @Test
    public void testStatementUninitializedVariableLet() {
        String expected = "Line 2: Variable b is not initialized";
        String observed = testStatement.parseString("let a 2 2 + b +");
        assertEquals(expected, observed);
        assertTrue(testStatement.isLet());
        assertTrue(testStatement.isError());
    }

    @Test
    public void testStatementQuit() {
        String expected = "";
        String observed = testStatement.parseString("quit");
        assertEquals(expected, observed);
        assertTrue(testStatement.isQuit());
        assertFalse(testStatement.isLet());
        assertFalse(testStatement.isError());
    }

    @Test
    public void testStatementQuitIgnoreFollowing() {
        String expected = "";
        String observed = testStatement.parseString("quit ignore this");
        assertEquals(expected, observed);
        assertTrue(testStatement.isQuit());
        assertFalse(testStatement.isLet());
        assertFalse(testStatement.isError());
    }
}
