package com.jolyvert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class Item {

    private int value;
    private int weight;

    public double getRatio() {
        return (double) value / weight;
    }
}
