package knaveengine.token;

import java.util.ArrayList;

public class Tokens {
    private final ArrayList<Token> tokens;

    public Tokens(String input) {
        this.tokens = Tokens.tokenize(input);
    }

    public ArrayList<Token> getTokens() {
        return new ArrayList<>(this.tokens);
    }

    private static ArrayList<Token> tokenize(String input) {
        ArrayList<Token> tokens = new ArrayList<>();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (Character.isWhitespace(c)) {
                continue;
            }

            if (c == '!' || c == '~') {
                tokens.add(new Token(TokenType.NOT, "!"));
            } else if (c == '&' || c == '^' || c == '∧') {
                tokens.add(new Token(TokenType.AND, "&"));
            } else if (c == '|' || c == '∨' || c == 'v' || c == 'V') {
                tokens.add(new Token(TokenType.OR, "|"));
            } else if (c == '>' || c == '→') {
                tokens.add(new Token(TokenType.IMPLIES, ">"));
            } else if (c == '(') {
                tokens.add(new Token(TokenType.OPEN_PAREN, "("));
            } else if (c == '=') {
                tokens.add(new Token(TokenType.EQUALS, "="));
            } else if (c == ')') {
                tokens.add(new Token(TokenType.CLOSE_PAREN, ")"));
            } else {
                tokens.add(new Token(TokenType.VAR, String.valueOf(c)));
            }
        }

        return tokens;
    }
}
