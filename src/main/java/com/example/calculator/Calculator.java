package com.example.calculator;

/**
 * Core calculator logic independent of UI, easy to unit test.
 */
public class Calculator {

    private double currentValue = 0.0;
    private double storedValue = 0.0;
    private String pendingOperator = null;
    private boolean resetOnNextDigit = false;

    public String inputDigit(String currentDisplay, char digit) {
        if (resetOnNextDigit || "0".equals(currentDisplay)) {
            resetOnNextDigit = false;
            return String.valueOf(digit);
        }
        return currentDisplay + digit;
    }

    public String inputDecimal(String currentDisplay) {
        if (resetOnNextDigit) {
            resetOnNextDigit = false;
            return "0.";
        }
        if (currentDisplay.contains(".")) {
            return currentDisplay; // ignore extra decimals
        }
        return currentDisplay + ".";
    }

    public String clear() {
        currentValue = 0.0;
        storedValue = 0.0;
        pendingOperator = null;
        resetOnNextDigit = false;
        return "0";
    }

    public String setOperator(String currentDisplay, String operator) {
        currentValue = Double.parseDouble(currentDisplay);
        if (pendingOperator != null) {
            storedValue = applyOperator(storedValue, currentValue, pendingOperator);
        } else {
            storedValue = currentValue;
        }
        pendingOperator = operator;
        resetOnNextDigit = true;
        return trimTrailingZeros(storedValue);
    }

    public String calculate(String currentDisplay) {
        currentValue = Double.parseDouble(currentDisplay);
        if (pendingOperator == null) {
            return trimTrailingZeros(currentValue);
        }
        double result = applyOperator(storedValue, currentValue, pendingOperator);
        storedValue = result;
        pendingOperator = null;
        resetOnNextDigit = true;
        return trimTrailingZeros(result);
    }

    private double applyOperator(double a, double b, String op) {
        return switch (op) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> {
                if (b == 0.0) {
                    throw new ArithmeticException("Division by zero");
                }
                yield a / b;
            }
            default -> throw new IllegalArgumentException("Unknown operator: " + op);
        };
    }

    private String trimTrailingZeros(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return String.valueOf(value);
        }
        String s = Double.toString(value);
        if (s.contains(".")) {
            // remove trailing zeros
            s = s.replaceAll("0+$", "").replaceAll("$", "");
        }
        return s;
    }
}
