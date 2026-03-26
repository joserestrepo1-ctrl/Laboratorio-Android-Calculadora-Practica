package com.utp.ioscalculator.history;

public class HistoryModel {
    private String expression;
    private String result;

    // Constructor
    public HistoryModel(String expression, String result) {
        this.expression = expression;
        this.result = result;
    }

    // Getters y Setters
    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}