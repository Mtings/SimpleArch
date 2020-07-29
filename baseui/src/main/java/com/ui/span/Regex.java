package com.ui.span;

public class Regex {
    // region Constants
    public static final int CASE_SENSITIVE = 0;
    public static final int CASE_INSENSITIVE = 1;
    // endregion

    // region Fields
    private String text;
    private int caseSensitivity;
    // endregion

    // region Constructors
    public Regex(String text, int caseSensitivity) {
        this.text = text;
        this.caseSensitivity = caseSensitivity;
    }
    // endregion

    // region Getters
    public String getText() {
        return text;
    }

    public int getCaseSensitivity() {
        return caseSensitivity;
    }
    // endregion

    // region Setters
    public void setText(String text) {
        this.text = text;
    }

    public void setCaseSensitivity(int caseSensitivity) {
        this.caseSensitivity = caseSensitivity;
    }
    // endregion
}
