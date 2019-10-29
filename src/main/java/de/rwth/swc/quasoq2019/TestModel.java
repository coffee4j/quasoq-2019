package de.rwth.swc.quasoq2019;

import de.rwth.swc.coffee4j.engine.util.Preconditions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestModel {

    private final int[] validValues;
    private final int[] invalidValues;
    private final String[] alphabet = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};

    public TestModel(int[] validValues, int[] invalidValues) {
        Preconditions.notNull(validValues);
        Preconditions.check(validValues.length > 0);
        Preconditions.check(Arrays.stream(validValues).allMatch(value -> value > 0));
        Preconditions.notNull(invalidValues);
        Preconditions.check(validValues.length == invalidValues.length);
        Preconditions.check(Arrays.stream(invalidValues).allMatch(value -> value >= 0));
        checkAllParametersOfSameSize(validValues, invalidValues);

        this.validValues = validValues;
        this.invalidValues = invalidValues;
    }

    private void checkAllParametersOfSameSize(int[] validValues, int[] invalidValues) {
        final int validSize = validValues[0];
        final int invalidSize = invalidValues[0];

        for(int i = 1; i < validValues.length; i++) {
            Preconditions.check(validValues[i] == validSize, "valid value " + i + " has wrong size");
            Preconditions.check(invalidValues[i] == invalidSize, "invalid value " + i + " has wrong size");
        }
    }

    public int getNumberOfParameters() {
        return validValues.length;
    }

    public int getNumberOfValues() {
        return validValues[0] + invalidValues[0];
    }

    public int[] getValidValuesOfParameter(int parameter) {
        Preconditions.check(parameter >= 0);
        Preconditions.check(parameter < validValues.length);

        return IntStream
                .range(0, validValues[parameter])
                .toArray();
    }

    public String[] getInvalidValuesOfParameter(int parameter) {
        Preconditions.check(parameter >= 0);
        Preconditions.check(parameter < invalidValues.length);

        final int size = invalidValues[parameter];

        return Arrays.copyOfRange(alphabet, 0, size);
    }

    public Object[] getValuesOfParameter(int parameter) {
        final int[] validValues = getValidValuesOfParameter(parameter);
        final Object[] invalidValues = getInvalidValuesOfParameter(parameter);

        final List<Object> list = new ArrayList<>();
        list.addAll(Arrays.stream(validValues).boxed().collect(Collectors.toList()));
        list.addAll(Arrays.asList(invalidValues));

        return list.toArray();
    }

    public String toString() {
        return Arrays.toString(validValues) + Arrays.toString(invalidValues);
    }
}
