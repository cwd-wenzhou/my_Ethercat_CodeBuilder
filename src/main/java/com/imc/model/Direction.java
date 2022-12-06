package com.imc.model;

public enum Direction {

    MBoxOut("MBoxOut", "EC_DIR_OUTPUT"),
    MBoxIN("MBoxIn", "EC_DIR_INPUT"),
    Outputs("Outputs", "EC_DIR_OUTPUT"),
    Inputs("Inputs", "EC_DIR_INPUT"),
    Invalid("", "EC_DIR_INVALID");

    private final String xmlString;
    private final String sourceFileString;

    Direction(String xmlString, String sourceFileString) {
        this.xmlString = xmlString;
        this.sourceFileString = sourceFileString;
    }

    public String getSourceFileString() {
        return sourceFileString;
    }

    public String getXmlString() {
        return xmlString;
    }

    public static Direction getInstance(String xmlString) {
        return switch (xmlString) {
            case "MBoxOut" -> Direction.MBoxOut;
            case "MBoxIn" -> Direction.MBoxIN;
            case "Inputs" -> Direction.Inputs;
            case "Outputs" -> Direction.Outputs;
            default -> Direction.Invalid;
        };
    }
}
