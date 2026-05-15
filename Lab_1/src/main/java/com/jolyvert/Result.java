package com.jolyvert;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Result {

    private List<Integer> itemIndexes = new ArrayList<>();
    private List<Integer> itemCounts = new ArrayList<>();

    private int totalValue;
    private int totalWeight;

    public void addItem(int index, int count, int value, int weight) {

        itemIndexes.add(index);
        itemCounts.add(count);

        totalValue += value;
        totalWeight += weight;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("Items in backpack:\n");

        for (int i = 0; i < itemIndexes.size(); i++) {

            sb.append("Item ")
                    .append(itemIndexes.get(i))
                    .append(" x ")
                    .append(itemCounts.get(i))
                    .append("\n");
        }

        sb.append("Total value: ")
                .append(totalValue)
                .append("\n");

        sb.append("Total weight: ")
                .append(totalWeight);

        return sb.toString();
    }
}