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
    public void complexTests() {
        Tokens tokens = new Tokens("a > a = b");
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

        tokens = new Tokens("a > (a = b)");
        rpn = new ReversePolishNotation(tokens);

        Assert.assertTrue(rpn.evaluate(new java.util.HashMap<>() {{
            put("a", false);
            put("b", false);
        }}));

        Assert.assertFalse(rpn.evaluate(new java.util.HashMap<>() {{
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

        tokens = new Tokens("(a > a = b) & (!a > !(a = b)");
        rpn = new ReversePolishNotation(tokens);

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
