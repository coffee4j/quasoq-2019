package de.rwth.swc.quasoq2019;

import de.rwth.swc.coffee4j.engine.util.Preconditions;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

import java.util.stream.IntStream;

public class CoveringArrayRandomizer {

    private static final RandomGenerator rng = new Well19937c();

    public static int[][] randomize(TestModel testModel, int[][] array) {
        Preconditions.notNull(testModel);
        Preconditions.notNull(array);

        final int[] parameterOrder = RandomUtil.buildRandomOrder(testModel.getNumberOfParameters(), rng);
        final int[][] valueOrders = IntStream.range(0, testModel.getNumberOfParameters())
                .mapToObj(i -> RandomUtil.buildRandomOrder(testModel.getNumberOfValues(), rng))
                .toArray(int[][]::new);

        return CoveringArraySwapper.swap(array, parameterOrder, valueOrders);
    }
}
