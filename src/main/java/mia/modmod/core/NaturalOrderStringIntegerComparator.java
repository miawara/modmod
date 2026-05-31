package mia.modmod.core;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/*
    idea from
     *  Copyright 1997-2007 BBNT Solutions, LLC
     *  under sponsorship of the Defense Advanced Research Projects
     *  Agency (DARPA).
     this code is original cus it looked fun to make

     Sort number groups in strings from left to right, then lexigraphically if equal
 */

public class NaturalOrderStringIntegerComparator<T> implements Comparator<T> {
    @Override
    public int compare(T o1, T o2) {
        String a = o1.toString();
        String b = o2.toString();

        StringNumberTokens tokensA = tokenizeString(a);
        StringNumberTokens tokensB = tokenizeString(b);

        List<Integer> numbersA = tokensA.numberTokens;
        List<Integer> numbersB = tokensB.numberTokens;

        for (int i = 0; i < Math.min(numbersA.size(),numbersB.size()); i++) {
            int numA = numbersA.get(i);
            int numB = numbersB.get(i);

            int comp = Integer.compare(numA, numB);
            if (comp != 0) return comp;
        }
        int compSize = Integer.compare(numbersA.size(), numbersB.size());
        if (compSize != 0) return compSize;

        // if numbers equal

        String stringsA = tokensA.stringTokens;
        String stringsB = tokensB.stringTokens;

        return stringsA.compareTo(stringsB);
    }

    private record StringNumberTokens(String stringTokens, List<Integer> numberTokens) { }

    enum TokenType {
        STRING,
        NUMBER;
    }

    private StringNumberTokens tokenizeString(String string) {
        String stringTokens = "";
        List<Integer> numberTokens = new ArrayList<>();


        String builder = "";
        TokenType lastType = null;
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);

            TokenType type;
            if (Character.isDigit(c)) type = TokenType.NUMBER;
            else type = TokenType.STRING;



            if (i == 0) {
                lastType = type;
                builder += c;
                continue;
            }

            if (!type.equals(lastType)) {
                if (lastType.equals(TokenType.NUMBER)) numberTokens.add(Integer.parseInt(builder));
                else stringTokens += (builder);
                builder = "";
            }

            lastType = type;
            builder += c;
        }
        if (!builder.isEmpty()) {
            if (lastType.equals(TokenType.NUMBER)) numberTokens.add(Integer.parseInt(builder));
            else stringTokens += (builder);
        }
        return new StringNumberTokens(stringTokens, numberTokens);
    }
}
