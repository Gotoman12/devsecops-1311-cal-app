package com.example.calculator.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.example.calculator.model.CalculationRequest;
import com.example.calculator.service.CalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class CalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CalculatorService calculatorService;

    @InjectMocks
    private CalculatorController calculatorController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(calculatorController).build();
    }

    @Test
    public void testAdd() throws Exception {
        CalculationRequest request = new CalculationRequest();
        request.setA(5);
        request.setB(3);

        when(calculatorService.add(5, 3)).thenReturn(8.0);

        mockMvc.perform(post("/api/calculator/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"a\":5,\"b\":3}"))
                .andExpect(status().isOk())
                .andExpect(content().string("8.0"));
    }

    @Test
    public void testSubtract() throws Exception {
        CalculationRequest request = new CalculationRequest();
        request.setA(5);
        request.setB(3);

        when(calculatorService.subtract(5, 3)).thenReturn(2.0);

        mockMvc.perform(post("/api/calculator/subtract")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"a\":5,\"b\":3}"))
                .andExpect(status().isOk())
                .andExpect(content().string("2.0"));
    }

    @Test
    public void testMultiply() throws Exception {
        CalculationRequest request = new CalculationRequest();
        request.setA(5);
        request.setB(3);

        when(calculatorService.multiply(5, 3)).thenReturn(15.0);

        mockMvc.perform(post("/api/calculator/multiply")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"a\":5,\"b\":3}"))
                .andExpect(status().isOk())
                .andExpect(content().string("15.0"));
    }

    @Test
    public void testDivide() throws Exception {
        CalculationRequest request = new CalculationRequest();
        request.setA(6);
        request.setB(3);

        when(calculatorService.divide(6, 3)).thenReturn(2.0);

        mockMvc.perform(post("/api/calculator/divide")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"a\":6,\"b\":3}"))
                .andExpect(status().isOk())
                .andExpect(content().string("2.0"));
    }

    @Test
    public void testDivideByZero() throws Exception {
        CalculationRequest request = new CalculationRequest();
        request.setA(6);
        request.setB(0);

        when(calculatorService.divide(6, 0)).thenThrow(new IllegalArgumentException("Division by zero is not allowed."));

        mockMvc.perform(post("/api/calculator/divide")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"a\":6,\"b\":0}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Division by zero is not allowed."));
    }
}