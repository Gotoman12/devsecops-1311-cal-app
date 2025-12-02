package com.example.calculator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void testAddition() {
        Calculator calc = new Calculator();
        String display = "0";

        display = calc.inputDigit(display, '1'); // "1"
        display = calc.setOperator(display, "+");
        display = calc.inputDigit(display, '2'); // "2"
        display = calc.calculate(display);

        assertEquals("3", display);
    }

    @Test
    void testSubtractionWithMultipleDigits() {
        Calculator calc = new Calculator();
        String display = "0";

        display = calc.inputDigit(display, '1');
        display = calc.inputDigit(display, '0'); // "10"
        display = calc.setOperator(display, "-");
        display = calc.inputDigit("0", '4'); // simulate next number "4"
        display = calc.calculate(display);

        assertEquals("6", display);
    }

    @Test
    void testMultiplication() {
        Calculator calc = new Calculator();
        String display = "0";

        display = calc.inputDigit(display, '3');
        display = calc.setOperator(display, "*");
        display = calc.inputDigit("0", '4'); // "4"
        display = calc.calculate(display);

        assertEquals("12", display);
    }

    @Test
    void testDivision() {
        Calculator calc = new Calculator();
        String display = "0";

        display = calc.inputDigit(display, '8');
        display = calc.setOperator(display, "/");
        display = calc.inputDigit("0", '2'); // "2"
        display = calc.calculate(display);

        assertEquals("4", display);
    }

    @Test
    void testDivisionByZeroThrows() {
        Calculator calc = new Calculator();
        String display = "0";

        display = calc.inputDigit(display, '8');
        display = calc.setOperator(display, "/");
        String finalDisplay = calc.inputDigit("0", '0'); // "0"

        assertThrows(ArithmeticException.class, () -> calc.calculate(finalDisplay));
    }

    @Test
    void testDecimalInputAndTrimming() {
        Calculator calc = new Calculator();
        String display = "0";

        display = calc.inputDigit(display, '1');
        display = calc.inputDecimal(display); // "1."
        display = calc.inputDigit(display, '5'); // "1.5"

        display = calc.setOperator(display, "+");
        display = calc.inputDigit("0", '2');
        display = calc.calculate(display);

        assertEquals("3.5", display);
    }

    @Test
    void testClear() {
        Calculator calc = new Calculator();
        String display = "123";
        display = calc.clear();
        assertEquals("0", display);
    }
}
