package com.imc.model;

public enum DataType {
    BOOL("BOOL", "bool", "EC_READ_BIT", "EC_WRITE_BIT"),
    BIT1("BIT1", "bool", "EC_READ_BIT", "EC_READ_BIT"),
    BIT2("BIT2", "bool", "EC_READ_BIT", "EC_READ_BIT"),
    BIT3("BIT3", "bool", "EC_READ_BIT", "EC_READ_BIT"),
    BIT4("BIT4", "bool", "EC_READ_BIT", "EC_READ_BIT"),
    BIT5("BIT5", "bool", "EC_READ_BIT", "EC_READ_BIT"),
    BIT6("BIT6", "bool", "EC_READ_BIT", "EC_READ_BIT"),
    BIT7("BIT7", "bool", "EC_READ_BIT", "EC_READ_BIT"),
    BIT8("BIT8", "bool", "EC_READ_BIT", "EC_READ_BIT"),


    SINT("SINT", "int8_t", "EC_READ_S8", "EC_WRITE_S8"),
    INT("INT", "int16_t", "EC_READ_S16", "EC_WRITE_S16"),
    DINT("DINT", "int32_t", "EC_READ_S32", "EC_WRITE_S32"),
    LINT("LINT", "int64_t", "EC_READ_S64", "EC_WRITE_S64"),

    USINT("USINT", "uint8_t", "EC_READ_U8", "EC_WRITE_U8"),

    UINT("UINT", "uint16_t", "EC_READ_U16", "EC_WRITE_U16"),
    UDINT("UDINT", "uint32_t", "EC_READ_U32", "EC_WRITE_U32"),
    ULINT("ULINT", "uint64_t", "EC_READ_U64", "EC_WRITE_U64"),

    REAL("REAL", "float", "EC_READ_REAL", "EC_WRITE_REAL"),
    LREAL("LREAL", "double", "EC_READ_LREAL", "EC_WRITE_LREAL"),
    UNkonw("unkonw", "", "", "");

    private final String xmlString;
    private final String typeString;
    private final String readString;

    private final String writeString;

    DataType(String xmlString, String typeString, String readString, String writeString) {
        this.xmlString = xmlString;
        this.typeString = typeString;
        this.readString = readString;
        this.writeString = writeString;
    }

    public static DataType getDataTypeBytypeString(String xmlString) {
        return switch (xmlString) {
            case "BOOL", "BIT" -> DataType.BOOL;
            case "BIT1" -> DataType.BIT1;
            case "BIT2" -> DataType.BIT2;
            case "BIT3" -> DataType.BIT3;
            case "BIT4" -> DataType.BIT4;
            case "BIT5" -> DataType.BIT5;
            case "BIT6" -> DataType.BIT6;
            case "BIT7" -> DataType.BIT7;
            case "BIT8" -> DataType.BIT8;

            case "SINT" -> DataType.SINT;
            case "INT" -> DataType.INT;
            case "DINT" -> DataType.DINT;
            case "LINT" -> DataType.LINT;
            case "USINT" -> DataType.USINT;
            case "UINT" -> DataType.UINT;
            case "UDINT" -> DataType.UDINT;
            case "ULINT" -> DataType.ULINT;
            case "REAL" -> DataType.REAL;
            case "LREAL" -> DataType.LREAL;
            default -> DataType.UNkonw;
        };

    }

    public String getXmlString() {
        return xmlString;
    }

    public String getReadString() {
        return readString;
    }

    public String getTypeString() {
        return typeString;
    }

    public String getWriteString() {
        return writeString;
    }

    public boolean isBits( ) {
        return this.equals(BIT2) || this.equals(BIT3) || this.equals(BIT4) || this.equals(BIT5)
                || this.equals(BIT6) || this.equals(BIT7) || this.equals(BIT8) ;
    }
}
