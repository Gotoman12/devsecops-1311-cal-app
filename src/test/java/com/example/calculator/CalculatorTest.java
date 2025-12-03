package com.example.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Calculator Test Suite")
class CalculatorTest {

    private Calculator calc;

    @BeforeEach
    void setUp() {
        calc = new Calculator();
    }

    // ========== Basic Operations Tests ==========

    @Test
    @DisplayName("Should add two positive integers")
    void testAddition() {
        String display = "0";
        display = calc.inputDigit(display, '1'); // "1"
        display = calc.setOperator(display, "+");
        display = calc.inputDigit(display, '2'); // "2"
        display = calc.calculate(display);

        assertEquals("3", display);
    }

    @Test
    @DisplayName("Should subtract with multiple digits")
    void testSubtractionWithMultipleDigits() {
        String display = "0";
        display = calc.inputDigit(display, '1');
        display = calc.inputDigit(display, '0'); // "10"
        display = calc.setOperator(display, "-");
        display = calc.inputDigit("0", '4'); // simulate next number "4"
        display = calc.calculate(display);

        assertEquals("6", display);
    }

    @Test
    @DisplayName("Should multiply two numbers")
    void testMultiplication() {
        String display = "0";
        display = calc.inputDigit(display, '3');
        display = calc.setOperator(display, "*");
        display = calc.inputDigit("0", '4'); // "4"
        display = calc.calculate(display);

        assertEquals("12", display);
    }

    @Test
    @DisplayName("Should divide two numbers")
    void testDivision() {
        String display = "0";
        display = calc.inputDigit(display, '8');
        display = calc.setOperator(display, "/");
        display = calc.inputDigit("0", '2'); // "2"
        display = calc.calculate(display);

        assertEquals("4", display);
    }

    @Test
    @DisplayName("Should throw exception on division by zero")
    void testDivisionByZeroThrows() {
        String display = "0";
        display = calc.inputDigit(display, '8');
        display = calc.setOperator(display, "/");
        String finalDisplay = calc.inputDigit("0", '0'); // "0"

        assertThrows(ArithmeticException.class, () -> calc.calculate(finalDisplay));
    }

    // ========== Decimal Operations Tests ==========

    @Test
    @DisplayName("Should handle decimal input and trim trailing zeros")
    void testDecimalInputAndTrimming() {
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
    @DisplayName("Should prevent multiple decimal points")
    void testMultipleDecimalPoints() {
        String display = "0";
        display = calc.inputDigit(display, '1');
        display = calc.inputDecimal(display); // "1."
        display = calc.inputDigit(display, '5'); // "1.5"
        String beforeSecondDecimal = display;
        display = calc.inputDecimal(display); // should be ignored

        assertEquals(beforeSecondDecimal, display);
    }

    @Test
    @DisplayName("Should handle decimal point at start")
    void testDecimalAtStart() {
        String display = "0";
        display = calc.inputDecimal(display); // Should become "0."

        assertTrue(display.startsWith("0."));
    }

    @Test
    @DisplayName("Should handle operations with decimals")
    void testDecimalOperations() {
        String display = "0";
        display = calc.inputDigit(display, '2');
        display = calc.inputDecimal(display);
        display = calc.inputDigit(display, '5'); // "2.5"
        display = calc.setOperator(display, "*");
        display = calc.inputDigit("0", '4'); // "4"
        display = calc.calculate(display);

        assertEquals("10", display);
    }

    // ========== Chained Operations Tests ==========

    @Test
    @DisplayName("Should handle chained additions")
    void testChainedAdditions() {
        String display = "0";
        display = calc.inputDigit(display, '5');
        display = calc.setOperator(display, "+"); // 5 +
        display = calc.inputDigit(display, '3');
        display = calc.setOperator(display, "+"); // should show 8 (5+3)
        assertEquals("8", display);
        display = calc.inputDigit(display, '2');
        display = calc.calculate(display); // 8 + 2 = 10

        assertEquals("10", display);
    }

    @Test
    @DisplayName("Should handle mixed operations")
    void testMixedOperations() {
        String display = "0";
        display = calc.inputDigit(display, '1');
        display = calc.inputDigit(display, '0'); // "10"
        display = calc.setOperator(display, "+");
        display = calc.inputDigit(display, '5'); // "5"
        display = calc.setOperator(display, "*"); // should calculate 10+5=15 first
        display = calc.inputDigit(display, '2'); // "2"
        display = calc.calculate(display); // 15 * 2 = 30

        assertEquals("30", display);
    }

    @Test
    @DisplayName("Should handle complex calculation sequence")
    void testComplexCalculationSequence() {
        String display = "0";
        // Calculate: 100 / 4 - 5 * 2
        display = calc.inputDigit(display, '1');
        display = calc.inputDigit(display, '0');
        display = calc.inputDigit(display, '0'); // "100"
        display = calc.setOperator(display, "/");
        display = calc.inputDigit(display, '4'); // "4"
        display = calc.setOperator(display, "-"); // 100/4 = 25
        assertEquals("25", display);
        display = calc.inputDigit(display, '5'); // "5"
        display = calc.setOperator(display, "*"); // 25-5 = 20
        display = calc.inputDigit(display, '2'); // "2"
        display = calc.calculate(display); // 20*2 = 40

        assertEquals("40", display);
    }

    // ========== Clear Function Tests ==========

    @Test
    @DisplayName("Should clear to zero")
    void testClear() {
        String display = "123";
        display = calc.clear();
        assertEquals("0", display);
    }

    @Test
    @DisplayName("Should clear and allow new calculation")
    void testClearAndContinue() {
        String display = "0";
        display = calc.inputDigit(display, '5');
        display = calc.setOperator(display, "+");
        display = calc.clear();
        assertEquals("0", display);

        // Start new calculation
        display = calc.inputDigit(display, '3');
        display = calc.setOperator(display, "*");
        display = calc.inputDigit(display, '2');
        display = calc.calculate(display);

        assertEquals("6", display);
    }

    // ========== Edge Cases Tests ==========

    @Test
    @DisplayName("Should handle large numbers")
    void testLargeNumbers() {
        String display = "0";
        display = calc.inputDigit(display, '9');
        display = calc.inputDigit(display, '9');
        display = calc.inputDigit(display, '9');
        display = calc.inputDigit(display, '9');
        display = calc.inputDigit(display, '9');
        display = calc.inputDigit(display, '9'); // "999999"
        display = calc.setOperator(display, "+");
        display = calc.inputDigit(display, '1');
        display = calc.calculate(display);

        assertEquals("1000000", display);
    }

    @Test
    @DisplayName("Should handle result that equals zero")
    void testResultEqualsZero() {
        String display = "0";
        display = calc.inputDigit(display, '5');
        display = calc.setOperator(display, "-");
        display = calc.inputDigit(display, '5');
        display = calc.calculate(display);

        assertEquals("0", display);
    }

    @Test
    @DisplayName("Should handle negative results")
    void testNegativeResults() {
        String display = "0";
        display = calc.inputDigit(display, '3');
        display = calc.setOperator(display, "-");
        display = calc.inputDigit(display, '7');
        display = calc.calculate(display);

        assertEquals("-4", display);
    }

    @Test
    @DisplayName("Should handle division resulting in decimal")
    void testDivisionWithDecimalResult() {
        String display = "0";
        display = calc.inputDigit(display, '1');
        display = calc.setOperator(display, "/");
        display = calc.inputDigit(display, '2');
        display = calc.calculate(display);

        assertEquals("0.5", display);
    }

    @Test
    @DisplayName("Should handle very small decimal results")
    void testSmallDecimalResults() {
        String display = "0";
        display = calc.inputDigit(display, '1');
        display = calc.setOperator(display, "/");
        display = calc.inputDigit(display, '3');
        display = calc.calculate(display);

        // Result should contain decimal representation of 1/3
        assertTrue(display.startsWith("0.33"));
    }

    @Test
    @DisplayName("Should handle equals without pending operation")
    void testEqualsWithoutOperation() {
        String display = "0";
        display = calc.inputDigit(display, '4');
        display = calc.inputDigit(display, '2'); // "42"
        display = calc.calculate(display);

        assertEquals("42", display);
    }

    @Test
    @DisplayName("Should reset on next digit after equals")
    void testResetAfterEquals() {
        String display = "0";
        display = calc.inputDigit(display, '5');
        display = calc.setOperator(display, "+");
        display = calc.inputDigit(display, '3');
        display = calc.calculate(display); // "8"

        // Next digit should start fresh
        display = calc.inputDigit(display, '2');

        assertEquals("2", display);
    }

    @Test
    @DisplayName("Should handle operator change before second number")
    void testOperatorChange() {
        String display = "0";
        display = calc.inputDigit(display, '5');
        display = calc.setOperator(display, "+");
        display = calc.setOperator(display, "*"); // Change operator - completes 5+5=10 first
        display = calc.inputDigit(display, '3');
        display = calc.calculate(display); // Should be 10 * 3 = 30

        assertEquals("30", display);
    }

    // ========== Invalid Operator Tests ==========

    @Test
    @DisplayName("Should throw exception for invalid operator during calculation")
    void testInvalidOperator() {
        // Build an invalid expression 5 % 3 and verify calculation throws
        assertThrows(IllegalArgumentException.class, () ->
                calc.calculate(
                        calc.inputDigit(
                                calc.setOperator("5", "%"),
                                '3'
                        )
                )
        );
    }

    @Test
    @DisplayName("Should handle zero multiplication")
    void testZeroMultiplication() {
        String display = "0";
        display = calc.inputDigit(display, '5');
        display = calc.setOperator(display, "*");
        display = calc.inputDigit(display, '0');
        display = calc.calculate(display);

        assertEquals("0", display);
    }

    @Test
    @DisplayName("Should handle zero divided by number")
    void testZeroDivision() {
        String display = "0";
        display = calc.setOperator(display, "/");
        display = calc.inputDigit(display, '5');
        display = calc.calculate(display);

        assertEquals("0", display);
    }
}
