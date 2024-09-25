package knaveengine.ast;

import knaveengine.token.Token;
import knaveengine.token.Tokens;
import knaveengine.token.TokenType;

import java.util.*;


/**
 * Parses and evaluates a Tokens object as Reverse Polish Notation.
 */
public class ReversePolishNotation {
    /**
     * Operator precedence. Key: the operator. Value: the precedence. Higher precedence value means that the operator is
     * evaluated first.
     */
    private static final HashMap<TokenType, Integer> PRECEDENCE = new HashMap<>(){{
        put(TokenType.NOT, 4);
        put(TokenType.AND, 3);
        put(TokenType.OR, 2);
        put(TokenType.EQUALS, 1);
        put(TokenType.IMPLIES, 0);
    }};

    private final Queue<Token> rpn;

    public ReversePolishNotation(Tokens tokens) {
        if (!tokens.validateParenthesis()) {
            throw new RuntimeException("Invalid infix expression: Mismatched parentheses");
        }

        this.rpn = genRPN(tokens);
    }

    /**
     * Implements the Shunting Yard Algorithm to convert tokenized infix notation to Reverse Polish Notation.
     * @param tokens The tokenized infix notation.
     * @return The tokenized Reverse Polish Notation.
     */
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

            // This should not happen because the infix expression is already validated to have matching parentheses.
            // But just in case it does happen, we throw an exception.
            if (token.type() == TokenType.OPEN_PAREN || token.type() == TokenType.CLOSE_PAREN) {
                throw new RuntimeException("Invalid RPN expression: Mismatched parentheses");
            }

            output.add(token);
        }

        return output;
    }

    /**
     * Get a copy of the RPN expression.
     * @return A copy of the RPN expression.
     */
    public Queue<Token> getRPN() {
        return new LinkedList<>(this.rpn);
    }

    /**
     * Get all the variables in the RPN expression.
     * @return A set of all the variables in the RPN expression.
     */
    public Set<String> getAllVariables() {
        Set<String> variables = new HashSet<>();

        for (Token token : this.rpn) {
            if (token.type() == TokenType.VAR && !token.value().equals("F") && !token.value().equals("T")) {
                variables.add(token.value());
            }
        }

        return variables;
    }

    /**
     * Evaluate the RPN expression with the given values for the variables. Variables "T" and "F" will be evaluated as
     * true and false respectively.
     *
     * @param values The values for the variables. Key: the variable name. Value: the value of the variable.
     * @return The result of the RPN expression.
     */
    public boolean evaluate(HashMap<String, Boolean> values) {
        Stack<Boolean> stack = new Stack<>();

        /*
         * Evaluating the RPN can be done using a simple algorithm utilizing a single stack. It iterates through the RPN and does the following:
         * - If the token is a variable, push the value of the variable onto the stack.
         *    If the variable's value is 'T' or 'F', evaluate it as 'true' or 'false' respectively.
         * - If the token is the not operator, pop the last value of the stack, invert it,
         *    and push it back onto the stack.
         * - If the token is any other operator, pop the last two values of the stack, perform the operation,
         *    and push the output back onto the stack. This should reduce the stack size by 1, since the operator
         *    requires 2 inputs and only provides 1 output.
         */

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
