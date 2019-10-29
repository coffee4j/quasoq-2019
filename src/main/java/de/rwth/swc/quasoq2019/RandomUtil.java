package de.rwth.swc.quasoq2019;

import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.MathArrays;

import java.util.stream.IntStream;

public class RandomUtil {

     public static int[] buildRandomOrder(int numberOfElements, RandomGenerator rng) {
        int[] indices  = IntStream.range(0, numberOfElements).toArray();

        MathArrays.shuffle(indices, rng);

        return indices;
    }
}
