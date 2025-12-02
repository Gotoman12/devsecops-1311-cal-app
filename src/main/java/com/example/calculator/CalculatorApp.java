package com.example.calculator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Simple JavaFX calculator with a clean UI.
 */
public class CalculatorApp extends Application {

    private final Calculator calculator = new Calculator();
    private Label display;

    @Override
    public void start(Stage primaryStage) {
        display = new Label("0");
        display.setFont(Font.font("Menlo", 32));
        display.setAlignment(Pos.CENTER_RIGHT);
        display.setTextFill(Color.WHITE);
        display.setStyle("-fx-background-color: #1e1e1e; -fx-padding: 20px; -fx-min-width: 260px;");

        GridPane buttons = createButtonGrid();

        BorderPane root = new BorderPane();
        root.setTop(displayContainer());
        root.setCenter(buttons);
        root.setStyle("-fx-background-color: #252526;");

        Scene scene = new Scene(root, 300, 420);
        primaryStage.setTitle("Calculator");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private HBox displayContainer() {
        HBox box = new HBox(display);
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.CENTER_RIGHT);
        return box;
    }

    private GridPane createButtonGrid() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(8);
        grid.setVgap(8);

        String[][] layout = new String[][]{
                {"C", "", "", "/"},
                {"7", "8", "9", "*"},
                {"4", "5", "6", "-"},
                {"1", "2", "3", "+"},
                {"0", "0", ".", "="}
        };

        for (int row = 0; row < layout.length; row++) {
            for (int col = 0; col < layout[row].length; col++) {
                String text = layout[row][col];
                if (text.isEmpty()) {
                    continue;
                }
                Button button = createButton(text);
                if (row == 4 && col == 0) {
                    button.setMaxWidth(Double.MAX_VALUE);
                    GridPane.setColumnSpan(button, 2);
                }
                grid.add(button, col, row);
            }
        }

        return grid;
    }

    private Button createButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("System", 18));
        button.setPrefSize(60, 60);
        button.setStyle(styleForButton(text));
        button.setTextFill(Color.WHITE);

        button.setOnAction(e -> handleInput(text));
        return button;
    }

    private String styleForButton(String text) {
        if ("C".equals(text)) {
            return "-fx-background-color: #d9534f; -fx-background-radius: 8;";
        }
        if ("+".equals(text) || "-".equals(text) || "*".equals(text) || "/".equals(text)) {
            return "-fx-background-color: #0275d8; -fx-background-radius: 8;";
        }
        if ("=".equals(text)) {
            return "-fx-background-color: #5cb85c; -fx-background-radius: 8;";
        }
        return "-fx-background-color: #3c3c3c; -fx-background-radius: 8;";
    }

    private void handleInput(String text) {
        String current = display.getText();
        try {
            switch (text) {
                case "C" -> display.setText(calculator.clear());
                case "+", "-", "*", "/" -> display.setText(calculator.setOperator(current, text));
                case "=" -> display.setText(calculator.calculate(current));
                case "." -> display.setText(calculator.inputDecimal(current));
                default -> { // digits
                    if (text.matches("\\d")) {
                        display.setText(calculator.inputDigit(current, text.charAt(0)));
                    }
                }
            }
        } catch (ArithmeticException ex) {
            display.setText("Error");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
