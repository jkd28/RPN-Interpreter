import java.util.ArrayList;

public class Statement {
    private String keyword;
    private Expression expression;
    private boolean isQuitStatement;

    public Statement(String line){
        parseString(line);
        setQuit();
    }

    public String getKeyword() {
        return this.keyword;
    }

    public boolean isQuit() {
        return this.isQuitStatement;
    }

    private void parseString(String line){
        String[] brokenLine = line.split(" ");

        // check for a keyword
        if (isKeyword(brokenLine[0])) {
            this.keyword = brokenLine[0];
        } else {
            // No keyword, everything is part of the expression
            this.keyword = "";

            expression = new Expression(line);
            System.out.println(expression.getResult());
        }
    }

    private boolean isKeyword(String potential) {
        return ((potential.equals("quit")) || (potential.equals("let")) || (potential.equals("print")));
    }

    private void setQuit() {
        if (this.keyword.equals("quit")) {
            this.isQuitStatement = true;
        } else {
            this.isQuitStatement = false;
        }
    }
}
