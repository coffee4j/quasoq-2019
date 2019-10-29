package de.rwth.swc.quasoq2019;

import java.util.Arrays;

public class CoveringArraySwapper {

    public static int[][] swap(int[][] rows, int[] columnOrder, int[][] valueOrders) {
        final int[][] swapped = swapColumns(rows, columnOrder);

        for(int i = 0; i < valueOrders.length; i++) {
            swapValues(swapped, i, valueOrders[i]);
        }

        return swapped;
    }

    private static int[][] swapColumns(int[][] rows, int[] order) {
        return Arrays.stream(rows)
                .map(row -> cloneAndApplyOrder(row, order))
                .toArray(int[][]::new);
    }

    private static int[] cloneAndApplyOrder(int[] elements, int[] order) {
        final int[] orderedElements = new int[elements.length];

        for(int j = 0; j < order.length; j++) {
            orderedElements[j] = elements[order[j]];
        }

        return orderedElements;
    }

    private static void swapValues(int[][] rows, int column, int[] order) {
        Arrays.stream(rows)
                .forEach(row -> row[column] = order[row[column]]);
    }
}
