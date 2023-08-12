package com.expression.solver;

import java.util.HashSet;
import java.util.regex.Pattern;

public class Validator {
    static final char[] operators = {'/', '*', '+', '-'};
    static final String regex = "\\d.[0-9*/+\\-].*\\d";
    static final String regexForSmallEquation = "\\d";

    public static boolean isExpressionValid(String expression){
        if(isNumberAtEndAndBegin(expression)){
            if(isOperatorsPresent(expression)){
                return isValidArithmetic(expression);
            }
        }
        return false;
    }

    private static boolean isNumberAtEndAndBegin(String expression){
        if(expression.length() == 3){
            if(Pattern.matches(regexForSmallEquation, String.valueOf(expression.charAt(0)))){
                return Pattern.matches(regexForSmallEquation, String.valueOf(expression.charAt(expression.length()-1)));
            }
            return false;
        }
        return Pattern.matches(regex,expression);
    }

    private static boolean isOperatorsPresent(String expression){
        for (char opr : operators){
            if (expression.contains(String.valueOf(opr))){
                return true;
            }
        }
        return false;
    }

    private static boolean isValidArithmetic(String expression){
        char[] equationArray = expression.toCharArray();
        HashSet<Integer> operatorIndex = getOperatorsIndex(equationArray);
        if(isOperatorIndexValid(operatorIndex)){
            return true;
        }
        return false;
    }
    private static HashSet<Integer> getOperatorsIndex(char[] expressionArray){
        HashSet<Integer> operatorIndex = new HashSet<Integer>();
        for (int i = 0 ; i < expressionArray.length ; i++){
            if(new String(operators).contains(String.valueOf(expressionArray[i]))){
                operatorIndex.add(i);
            }
        }
        return operatorIndex;
    }
    private static boolean isOperatorIndexValid(HashSet<Integer> operatorIndex){
        if(operatorIndex.size() > 1){
            Object[] operatorsIndexArray = operatorIndex.toArray();
            int arrayLength = operatorsIndexArray.length;
            for (int i = 0 ; i < arrayLength; i++){
                int incrementedValue = i+1;
                if ((incrementedValue) < arrayLength){
                    int diff = Integer.parseInt(operatorsIndexArray[incrementedValue].toString()) - Integer.parseInt(operatorsIndexArray[i].toString());
                    if(diff == 1){
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
