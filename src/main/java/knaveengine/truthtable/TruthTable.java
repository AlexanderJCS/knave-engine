package knaveengine.truthtable;

import knaveengine.ast.ReversePolishNotation;

import java.util.*;

public class TruthTable {
    private final List<ReversePolishNotation> expressions;
    private final Set<String> variables;

    public TruthTable(List<ReversePolishNotation> expressions) {
        this.expressions = new ArrayList<>(expressions);

        this.variables = new HashSet<>();
        for (ReversePolishNotation rpn : this.expressions) {
            this.variables.addAll(rpn.getAllVariables());
        }
    }

    public List<HashMap<String, Boolean>> getAllStateCombinations() {
        List<HashMap<String, Boolean>> states = new ArrayList<>();

        for (int i = 0; i < Math.pow(2, this.variables.size()); i++) {
            HashMap<String, Boolean> state = new HashMap<>();

            int j = 0;
            for (String variable : this.variables) {
                state.put(variable, (i & (1 << j)) != 0);
                j++;
            }

            states.add(state);
        }

        return states;
    }

    /**
     * Evaluates the truth table for true expressions.
     *
     * @return a list of all possible states such that each truth table expression is true
     */
    public List<Map<String, Boolean>> getTruthfulStates() {
        List<Map<String, Boolean>> states = new ArrayList<>();

        for (HashMap<String, Boolean> state : this.getAllStateCombinations()) {
            boolean isTrue = true;
            for (ReversePolishNotation rpn : this.expressions) {
                if (!rpn.evaluate(state)) {
                    isTrue = false;
                    break;
                }
            }

            if (isTrue) {
                states.add(state);
            }
        }

        return states;
    }
}
