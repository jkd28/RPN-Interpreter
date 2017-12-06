import org.junit.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Locale;

public class FileConcatenatorTest {
    FileConcatenator testFileCat;

    @Before
    public void setup() {
        testFileCat = new FileConcatenator();
    }

    // Test that the correct error values are set if the file reader cannot
    // find/encounters an error in the provided file
    @Test
    public void testFileReaderStringReturn() {
        testFileCat.readLinesFromFile("this file doesn't exist");
        assertEquals("Could not find file 'this file doesn't exist'", testFileCat.getErrorMessage());
        assertEquals(5, testFileCat.getErrorCode());
    }

    /****
        NOTE: The following tests require that the 'testfiles' folder be populated with 'test1.rpn'
        If this is not the case, comment out these tests, else they will not properly compile
    ****/

    // Tests that the file reader properly reads a file into a single
    // String.  This test is more of a System Test than a unit test,
    // and so it is rather brittle and can easily break if the contents of test1.rpn
    // are ever changed
    @Test
    public void testFileRead() {
        String testReadFile = "testfiles/test1.rpn";
        String expected = "LET a 2 2 +\nLET c a 4 +\n1 2 3 4\nPRINT c\n";
        String observed = testFileCat.readLinesFromFile(testReadFile);
        assertEquals(0, testFileCat.getErrorCode());
        assertEquals(expected, observed);
    }

    // Tests that two basic files will be concatenated into a single string as expected.
    // Like the above test, this functions more as a System test than a unit test,
    // but should be automated anyway.  I make use of JUnit here, but this is a very brittle
    // test that could break if the contents of either file are changed even slightly
    @Test
    public void testFileConcatenation() {
        String testFile1 = "testfiles/test1.rpn";
        String testFile2 = "testfiles/test2.rpn";

        ArrayList<String> testArray = new ArrayList<String>();
        testArray.add("let a 2 2 +");
        testArray.add("let c a 4 +");
        testArray.add("1 2 3 4");
        testArray.add("print c");
        testArray.add("print a");

        testFileCat.addFile(testFile1);
        testFileCat.addFile(testFile2);

        ArrayList<String> observed = testFileCat.getCurrentFileString();
        assertEquals(0, testFileCat.getErrorCode());
        for (int i = 0; i < testArray.size(); i++) {
            assertEquals(testArray.get(i), observed.get(i));
        }
    }
}
