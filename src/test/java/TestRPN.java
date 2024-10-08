import knaveengine.ast.ReversePolishNotation;
import knaveengine.token.Token;
import knaveengine.token.Tokens;
import knaveengine.token.TokenType;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class TestRPN {
    @Test
    public void trueFalseTest() {
        Tokens tokens = new Tokens("a = T");
        ReversePolishNotation rpn = new ReversePolishNotation(tokens);

        Assert.assertTrue(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", true);
        }}));

        Assert.assertFalse(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", false);
        }}));

        tokens = new Tokens("a = F");
        rpn = new ReversePolishNotation(tokens);

        Assert.assertFalse(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", true);
        }}));

        Assert.assertTrue(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", false);
        }}));
    }

    @Test
    public void notTest() {
        Tokens tokens = new Tokens("!(a)");
        ReversePolishNotation rpn = new ReversePolishNotation(tokens);

        Assert.assertTrue(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", false);
        }}));

        Assert.assertFalse(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", true);
        }}));
    }

    @Test
    public void andTest() {
        Tokens tokens = new Tokens("a & b");
        ReversePolishNotation rpn = new ReversePolishNotation(tokens);

        Assert.assertTrue(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", true);
            put("b", true);
        }}));

        Assert.assertFalse(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", true);
            put("b", false);
        }}));

        Assert.assertFalse(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", false);
            put("b", true);
        }}));

        Assert.assertFalse(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", false);
            put("b", false);
        }}));
    }

    @Test
    public void orTest() {
        Tokens tokens = new Tokens("a | b");
        ReversePolishNotation rpn = new ReversePolishNotation(tokens);

        Assert.assertTrue(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", true);
            put("b", true);
        }}));

        Assert.assertTrue(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", true);
            put("b", false);
        }}));

        Assert.assertTrue(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", false);
            put("b", true);
        }}));

        Assert.assertFalse(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", false);
            put("b", false);
        }}));
    }

    @Test
    public void equalsTest() {
        Tokens tokens = new Tokens("a = b");
        ReversePolishNotation rpn = new ReversePolishNotation(tokens);

        Assert.assertTrue(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", true);
            put("b", true);
        }}));

        Assert.assertFalse(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", true);
            put("b", false);
        }}));

        Assert.assertFalse(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", false);
            put("b", true);
        }}));

        Assert.assertTrue(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", false);
            put("b", false);
        }}));
    }

    @Test
    public void notAndOrEqualsTest() {
        Tokens tokens = new Tokens("!(a & b) | c = d");
        ReversePolishNotation rpn = new ReversePolishNotation(tokens);

        System.out.println(rpn.getRPN());

        Assert.assertTrue(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", true);
            put("b", true);
            put("c", false);
            put("d", false);
        }}));
    }

    @Test
    public void impliesTest1() {
        Tokens tokens = new Tokens("a > b");
        ReversePolishNotation rpn = new ReversePolishNotation(tokens);

        Assert.assertTrue(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", false);
            put("b", false);
        }}));

        Assert.assertTrue(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", true);
            put("b", true);
        }}));

        Assert.assertFalse(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", true);
            put("b", false);
        }}));

        Assert.assertTrue(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", false);
            put("b", true);
        }}));
    }

    @Test
    public void impliesTest2() {
        Tokens tokens = new Tokens("a > (a = b)");
        ReversePolishNotation rpn = new ReversePolishNotation(tokens);

        Assert.assertTrue(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", false);
            put("b", false);
        }}));

        Assert.assertTrue(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", true);
            put("b", true);
        }}));

        Assert.assertFalse(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", true);
            put("b", false);
        }}));

        Assert.assertTrue(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", false);
            put("b", true);
        }}));
    }

    @Test
    public void impliesTest3() {
        Tokens tokens = new Tokens("a > !(a = b)");
        ReversePolishNotation rpn = new ReversePolishNotation(tokens);

        Assert.assertTrue(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", false);
            put("b", false);
        }}));

        Assert.assertFalse(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", true);
            put("b", true);
        }}));

        Assert.assertTrue(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", true);
            put("b", false);
        }}));

        Assert.assertTrue(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", false);
            put("b", true);
        }}));
    }

    @Test
    public void impliesTest4() {
        Tokens tokens = new Tokens("(a > (a = b)) & (!a > !(a = b))");
        ReversePolishNotation rpn = new ReversePolishNotation(tokens);

        System.out.println(rpn.getRPN());

        Assert.assertFalse(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", false);
            put("b", false);
        }}));

        Assert.assertTrue(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", true);
            put("b", true);
        }}));

        Assert.assertFalse(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", true);
            put("b", false);
        }}));

        Assert.assertTrue(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", false);
            put("b", true);
        }}));
    }

    @Test
    public void testImplication() {
        Tokens tokens = new Tokens("p > q");
        ReversePolishNotation rpn = new ReversePolishNotation(tokens);

        Assert.assertTrue(rpn.evaluate(new java.util.HashMap<>() {{
            put("p", true);
            put("q", true);
        }}));

        Assert.assertFalse(rpn.evaluate(new java.util.HashMap<>() {{
            put("p", true);
            put("q", false);
        }}));

        Assert.assertTrue(rpn.evaluate(new java.util.HashMap<>() {{
            put("p", false);
            put("q", true);
        }}));

        Assert.assertTrue(rpn.evaluate(new java.util.HashMap<>() {{
            put("p", false);
            put("q", false);
        }}));
    }

    @Test
    public void testRPN() {
        Tokens tokens = new Tokens("!(p & q) | r = c");
        ReversePolishNotation rpn = new ReversePolishNotation(tokens);

        List<Token> expectedTokensRPN = Arrays.asList(
                new Token(TokenType.VAR, "p"),
                new Token(TokenType.VAR, "q"),
                new Token(TokenType.AND, "&"),
                new Token(TokenType.NOT, "!"),
                new Token(TokenType.VAR, "r"),
                new Token(TokenType.OR, "|"),
                new Token(TokenType.VAR, "c"),
                new Token(TokenType.EQUALS, "=")
        );

        Queue<Token> rpnQueue = rpn.getRPN();
        Assert.assertEquals(expectedTokensRPN.size(), rpnQueue.size());

        for (Token token : expectedTokensRPN) {
            Assert.assertEquals(token, rpnQueue.poll());
        }

        Assert.assertTrue(rpn.evaluate(new java.util.HashMap<>() {{
            put("p", true);
            put("q", false);
            put("r", false);
            put("c", true);
        }}));

        Assert.assertTrue(rpn.evaluate(new java.util.HashMap<>() {{
            put("p", true);
            put("q", true);
            put("r", false);
            put("c", false);
        }}));

        Assert.assertTrue(rpn.evaluate(new java.util.HashMap<>() {{
            put("p", false);
            put("q", false);
            put("r", true);
            put("c", true);
        }}));
    }
}
