package com.expression.solver;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Objects;

public class Solver extends Validator{
    private static final DecimalFormat dfZero = new DecimalFormat("0.00");
    public static String getSolution(String expression){
        if(isExpressionValid(expression)){
            return String.valueOf(solveExpression(expression));
        }
        return "Invalid Expression";
    }

    private static String solveExpression(String expression){
        char[] expressionArray = expression.toCharArray();
        HashMap<String, HashMap<Integer, String>> response = getExpressionArray(expressionArray);
        HashMap<Integer, String> numberArray = response.get("numbers");
        HashMap<Integer, String> operatorsArray = response.get("operators");
        for (char opr : operators){
            operatorsArray.forEach((key, value) ->{
                if(Objects.equals(value, String.valueOf(opr))){
                    if (key > 0){
                        if(numberArray.size() > 1){
                            double numOne = 0, numTwo = 0;
                            int keyOne = 0 , keyTwo = 0;
                            if(numberArray.containsKey(key-1) && numberArray.containsKey(key+1)){
                                numOne = Double.parseDouble(numberArray.get(key-1));
                                keyOne = key - 1;
                                numTwo = Double.parseDouble(numberArray.get(key+1));
                                keyTwo = key + 1;
                            }else if(numberArray.containsKey(key -1) || numberArray.containsKey(key+1)){
                                if(numberArray.containsKey(key-1)){
                                    numOne = Double.parseDouble(numberArray.get(key-1));
                                    keyOne = key - 1;
                                    int lastEntry = numberArray.keySet().stream().reduce((one , two) -> two).get();
                                    for (int i = key; i <= lastEntry ; i++){
                                        if(numberArray.containsKey(i)){
                                            numTwo = Double.parseDouble(numberArray.get(i));
                                            keyTwo = i;
                                            break;
                                        }
                                    }
                                }else if(numberArray.containsKey(key+1)){
                                    numTwo = Double.parseDouble(numberArray.get(key+1));
                                    keyTwo = key + 1;
                                    for (int i = key ; i > 0 ; i--){
                                        if (numberArray.containsKey(i)){
                                            numOne =Double.parseDouble(numberArray.get(i));
                                            keyOne = i;
                                            break;
                                        }
                                    }
                                }
                            }

                            double updatedNumber = 0;
                            switch (opr) {
                                case '/' -> {
                                    updatedNumber = numOne / numTwo;
                                    numberArray.remove(keyOne);
                                    numberArray.remove(keyTwo);
                                    numberArray.put(key + 1, String.valueOf(dfZero.format(updatedNumber)));
                                }
                                case '*' -> {
                                    updatedNumber = numOne * numTwo;
                                    numberArray.remove(keyOne);
                                    numberArray.remove(keyTwo);
                                    numberArray.put(key + 1, String.valueOf(dfZero.format(updatedNumber)));
                                }
                                case '+' -> {
                                    updatedNumber = numOne + numTwo;
                                    numberArray.remove(keyOne);
                                    numberArray.remove(keyTwo);
                                    numberArray.put(key + 1, String.valueOf(dfZero.format(updatedNumber)));
                                }
                                case '-' -> {
                                    updatedNumber = numOne - numTwo;
                                    numberArray.remove(keyOne);
                                    numberArray.remove(keyTwo);
                                    numberArray.put(key + 1, String.valueOf(dfZero.format(updatedNumber)));
                                }
                            }
                        }
                    }
                }
            });
        }
        return String.valueOf(numberArray.values());
    }
    private static HashMap<String, HashMap<Integer, String>> getExpressionArray(char[] expressionArray){
        HashMap<String, HashMap<Integer, String>> responseArray = new HashMap<String, HashMap<Integer, String>>();
        HashMap<Integer, String> numberArray = new HashMap<Integer, String>();
        HashMap<Integer, String> operatorsArray = new HashMap<Integer, String>();
        int lastPosition = 0;
        int numberPosition = 0;
        StringBuilder tempValue = new StringBuilder();
        for (int i = 0; i < expressionArray.length ; i++){
            String currentValue = String.valueOf(expressionArray[i]);
            if(String.valueOf(operators).contains(currentValue)){
                if(i > 0){
                    if(lastPosition > 0){
                        lastPosition+=1;
                    }
                    for (int j = lastPosition ; j < i ; j ++){
                        tempValue.append(expressionArray[j]);
                    }
                    lastPosition = i;

                    numberArray.put(numberPosition ,String.valueOf(tempValue));
                    operatorsArray.put(numberPosition+1, String.valueOf(expressionArray[lastPosition]));
                    tempValue.delete(0 , tempValue.length());
                    numberPosition+=2;
                }
            }else if (i == (expressionArray.length-1)){
                if(lastPosition > 0){
                    lastPosition+=1;
                }
                for (int j = lastPosition ; j < expressionArray.length; j++){
                    tempValue.append(expressionArray[j]);
                }
                numberArray.put(numberPosition,String.valueOf(tempValue));
                tempValue.delete(0 , tempValue.length());
            }
        }
        responseArray.put("numbers", numberArray);
        responseArray.put("operators", operatorsArray);
        return responseArray;
    }
}
