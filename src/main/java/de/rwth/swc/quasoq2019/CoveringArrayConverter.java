package de.rwth.swc.quasoq2019;

import de.rwth.swc.coffee4j.engine.util.Preconditions;

import java.util.Arrays;

public class CoveringArrayConverter {

    public static Object[][] convert(TestModel testModel, int[][] rows) {
        Preconditions.notNull(testModel);
        Preconditions.notNull(rows);
        Preconditions.check(Arrays.stream(rows).allMatch(row-> row.length == testModel.getNumberOfParameters()));

        return Arrays.stream(rows)
                .map(row -> convertRow(testModel, row))
                .toArray(Object[][]::new);

    }

    private static Object[] convertRow(TestModel testModel, int[] row) {
        Preconditions.notNull(row);
        Preconditions.check(row.length == testModel.getNumberOfParameters());

        final Object[] testInput = new Object[row.length];

        for(int i = 0; i < row.length; i++) {
            final Object[] values = testModel.getValuesOfParameter(i);

            testInput[i] = values[row[i]];
        }

        return testInput;
    }
}
