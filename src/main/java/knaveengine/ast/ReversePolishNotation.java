package knaveengine.ast;

import knaveengine.token.Token;
import knaveengine.token.Tokens;
import knaveengine.token.TokenType;

import java.util.*;

public class ReversePolishNotation {
    private static final HashMap<TokenType, Integer> PRECEDENCE = new HashMap<>(){{
        put(TokenType.NOT, 4);
        put(TokenType.AND, 3);
        put(TokenType.OR, 2);
        put(TokenType.EQUALS, 1);
        put(TokenType.IMPLIES, 0);
    }};

    private final Queue<Token> rpn;

    public ReversePolishNotation(Tokens tokens) {
        this.rpn = genRPN(tokens);
    }

    private static Queue<Token> genRPN(Tokens tokens) {
        Stack<Token> stack = new Stack<>();
        Queue<Token> output = new LinkedList<>();

        for (Token token : tokens.getTokens()) {
            if (token.type() == TokenType.OPEN_PAREN) {
                stack.push(token);
            } else if (token.type() == TokenType.VAR) {
                output.add(token);
            } else if (token.type() == TokenType.CLOSE_PAREN) {
                while (stack.peek().type() != TokenType.OPEN_PAREN) {
                    output.add(stack.pop());
                }
                stack.pop();
            } else {
                while (!stack.isEmpty() && stack.peek().type() != TokenType.OPEN_PAREN && PRECEDENCE.get(stack.peek().type()) >= PRECEDENCE.get(token.type())) {
                    output.add(stack.pop());
                }
                stack.push(token);
            }
        }

        while (!stack.isEmpty()) {
            Token token = stack.pop();

            if (token.type() == TokenType.OPEN_PAREN || token.type() == TokenType.CLOSE_PAREN) {
                throw new RuntimeException("Invalid RPN expression: Mismatched parentheses");
            }

            output.add(token);
        }

        return output;
    }

    public Queue<Token> getRPN() {
        return new LinkedList<>(this.rpn);
    }

    public Set<String> getAllVariables() {
        Set<String> variables = new HashSet<>();

        for (Token token : this.rpn) {
            if (token.type() == TokenType.VAR && !token.value().equals("F") && !token.value().equals("T")) {
                variables.add(token.value());
            }
        }

        return variables;
    }

    public boolean evaluate(HashMap<String, Boolean> values) {
        Stack<Boolean> stack = new Stack<>();

        for (Token token : this.rpn) {
            if (token.type() == TokenType.VAR) {
                if (token.value().equals("F")) {
                    stack.push(false);
                } else if (token.value().equals("T")) {
                    stack.push(true);
                } else {
                    stack.push(values.get(token.value()));
                }


            } else if (token.type() == TokenType.NOT) {
                stack.push(!stack.pop());
            } else {
                boolean p;
                boolean q;

                try {
                    q = stack.pop();
                    p = stack.pop();
                } catch (EmptyStackException | NullPointerException e) {
                    throw new RuntimeException("Invalid RPN expression: a variable is likely missing");
                }

                if (token.type() == TokenType.AND) {
                    stack.push(p && q);
                } else if (token.type() == TokenType.OR) {
                    stack.push(p || q);
                } else if (token.type() == TokenType.IMPLIES) {
                    stack.push(!p || q);
                } else if (token.type() == TokenType.EQUALS) {
                    stack.push(p == q);
                }
            }
        }

        if (stack.size() != 1) {
            throw new RuntimeException("Invalid RPN expression: not every variable has an operator");
        }

        return stack.pop();
    }
}
