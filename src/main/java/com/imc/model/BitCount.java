package com.imc.model;

import lombok.Data;

@Data
public class BitCount {
    int bit;
    int offset;

    public void addBit(int addOffset) {
        offset += addOffset;
        bit += offset / 8;
        offset %= 8;
    }

    public BitCount() {
        this.bit = 0;
        this.offset = 0;
    }
}
