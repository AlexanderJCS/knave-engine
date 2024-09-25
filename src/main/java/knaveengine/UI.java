package knaveengine;

import knaveengine.ast.ReversePolishNotation;
import knaveengine.token.Tokens;
import knaveengine.truthtable.TruthTable;

import java.util.*;

public class UI {

    /**
     * Runs the console UI
     */
    public static void run() {
        List<String> claims = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        String input = "";
        while (!input.equalsIgnoreCase("end")) {
            char thisChar = (char) ('a' + claims.size());

            System.out.print("Enter a claim for character " + thisChar + " (or 'end' to finish): ");
            input = scanner.nextLine();

            if (!input.equalsIgnoreCase("end")) {
                claims.add(input);
            }
        }

        List<String> truthTableStatements = new ArrayList<>(claims.size());
        for (int i = 0; i < claims.size(); i++) {
            char thisChar = (char) ('a' + i);
            String claim = claims.get(i);

            truthTableStatements.add("(" + thisChar + " > (" + claim + "))" + " & (!(" + thisChar + ") > !(" + claim + "))");
        }

        List<ReversePolishNotation> rpnList = new ArrayList<>();
        for (String statement : truthTableStatements) {
            rpnList.add(new ReversePolishNotation(new Tokens(statement)));
        }

        TruthTable tt = new TruthTable(rpnList);

        List<Map<String, Boolean>> possibleStates = tt.getTruthfulStates();

        if (possibleStates.isEmpty()) {
            System.out.println("There is a contradiction: no value of the truth tables is true.");
        } else {
            for (java.util.Map<String, Boolean> state : possibleStates) {
                for (String variable : state.keySet()) {
                    System.out.print(variable + " is a " + (state.get(variable) ? "Knight" : "Knave") + ", ");
                }

                System.out.println();
            }
        }
    }
}
