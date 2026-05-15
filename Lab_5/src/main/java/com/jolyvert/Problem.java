package com.jolyvert;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Getter
public class Problem {

    private int n;
    private int seed;
    private int lowerBound;
    private int upperBound;

    private List<Item> items = new ArrayList<>();

    public Problem(int n, int seed, int lowerBound, int upperBound) {

        this.n = n;
        this.seed = seed;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;

        Random random = new Random(seed);

        for (int i = 0; i < n; i++) {

            int value = random.nextInt(upperBound - lowerBound + 1) + lowerBound;
            int weight = random.nextInt(upperBound - lowerBound + 1) + lowerBound;

            items.add(new Item(value, weight));
        }
    }

    public Result solve(int capacity) {

        Result result = new Result();

        List<Integer> indexes = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            indexes.add(i);
        }

        indexes.sort((a, b) ->
                Double.compare(
                        items.get(b).getRatio(),
                        items.get(a).getRatio()
                )
        );

        int currentWeight = 0;

        for (Integer index : indexes) {

            Item item = items.get(index);

            int count = 0;

            while (currentWeight + item.getWeight() <= capacity) {

                currentWeight += item.getWeight();
                count++;
            }

            if (count > 0) {

                result.addItem(index, count, count * item.getValue(), count * item.getWeight()
                );
            }

            if (currentWeight == capacity) {
                break;
            }
        }

        return result;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("Problem items:\n");

        for (int i = 0; i < items.size(); i++) {

            sb.append(i)
                    .append(": ")
                    .append(items.get(i))
                    .append("\n");
        }

        return sb.toString();
    }
}
