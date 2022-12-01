package com.imc.model;

import lombok.Data;

import java.util.List;
@Data
public class Pdo {
    String index;
    String name;
    List<String> exclude;
    List<Entry> entries;
}
