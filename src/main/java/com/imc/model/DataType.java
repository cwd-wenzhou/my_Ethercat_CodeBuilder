package com.imc.model;

public enum DataType {
    BOOL("bool", "EC_READ_BIT", "EC_WRITE_BIT"),
    BITs("bool", "EC_READ_BIT", "EC_READ_BIT"),
    SINT("int8_t", "EC_READ_S8", "EC_WRITE_S8"),
    INT("int16_t", "EC_READ_S16", "EC_WRITE_S16"),
    DINT("int32_t", "EC_READ_S32", "EC_WRITE_S32"),
    LINT("int64_t", "EC_READ_S64", "EC_WRITE_S64"),
    USINT("uint8_t", "EC_READ_U8", "EC_WRITE_U8"),
    UINT("uint16_t", "EC_READ_U8", "EC_WRITE_U16"),
    UDINT("uint32_t", "EC_READ_U32", "EC_WRITE_U32"),
    ULINT("uint64_t", "EC_READ_U64", "EC_WRITE_U64"),
    REAL("float", "EC_READ_REAL", "EC_WRITE_REAL"),
    LREAL("double", "EC_READ_LREAL", "EC_WRITE_LREAL"),
    UNkonw("", "", "");


    private final String typeString;
    private final String readString;

    private final String writeString;

    DataType(String typeString, String readString, String writeString) {
        this.typeString = typeString;
        this.readString = readString;
        this.writeString = writeString;
    }

    public static DataType getDataTypeBytypeString(String xmlString) {
        return switch (xmlString) {
            case "BOOL", "BIT" -> DataType.BOOL;
            case "BIT1", "BIT2", "BIT3", "BIT4", "BIT5", "BIT6", "BIT7", "BIT8" -> DataType.BITs;
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

    public String getReadString() {
        return readString;
    }

    public String getTypeString() {
        return typeString;
    }

    public String getWriteString() {
        return writeString;
    }
}
