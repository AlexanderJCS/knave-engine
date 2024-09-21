import knaveengine.token.Token;
import knaveengine.token.Tokens;
import knaveengine.token.TokenType;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TestTokenizer {
    @Test

    public void testTokenize() {
        Tokens tokens = new Tokens("!(p & q) | r = c");

        List<Token> expectedTokens = Arrays.asList(
                new Token(TokenType.NOT, "!"),
                new Token(TokenType.OPEN_PAREN, "("),
                new Token(TokenType.VAR, "p"),
                new Token(TokenType.AND, "&"),
                new Token(TokenType.VAR, "q"),
                new Token(TokenType.CLOSE_PAREN, ")"),
                new Token(TokenType.OR, "|"),
                new Token(TokenType.VAR, "r"),
                new Token(TokenType.EQUALS, "="),
                new Token(TokenType.VAR, "c")
        );

        Assert.assertEquals(expectedTokens, tokens.getTokens());

        for (int i = 0; i < expectedTokens.size(); i++) {
            Assert.assertEquals(expectedTokens.get(i), tokens.getTokens().get(i));
        }
    }
}