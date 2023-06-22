package com.example.pm2e10002_0039_1.Models;

public class SpinnerItem {
    private int value;
    private String text;
    private int codigo;

    public SpinnerItem(int value, String text, int codigo) {
        this.value = value;
        this.text = text;
        this.codigo=codigo;
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    public int getCodigo(){
        return codigo;
    }

    @Override
    public String toString() {
        return text + " ("+codigo+")";
    }
}
