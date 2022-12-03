package com.imc.model;

import lombok.Data;

@Data
public class Entry {
    String index;
    String subindex;
    String bitLen;
    String name;
    DataType dataType;
}
