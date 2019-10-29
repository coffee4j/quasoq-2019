package de.rwth.swc.quasoq2019;

import de.rwth.swc.coffee4j.engine.util.Preconditions;

import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class SystemModel {

    private final int[] configurations;

    public SystemModel(int ... configurations) {
        Preconditions.notNull(configurations);
        Preconditions.check(configurations.length > 0);
        Preconditions.check(Arrays.stream(configurations).filter(config -> config == 1).count() == 1, "should contain exactly 1 fault");

        this.configurations = configurations;
    }

    public boolean test(Object ... values) {
        Preconditions.notNull(values);
        Preconditions.check(values.length == configurations.length);
        Preconditions.check(Arrays.stream(values)
                .allMatch(value -> value instanceof Integer || value instanceof String));

        for(int i = 0; i < values.length; i++) {
            final int configuration = configurations[i];
            final Object value = values[i];

            if(value instanceof String) {
                if(configuration > 0) {
                    return false; // fault triggered
                } else if(configuration == 0){
                    return true;  // error-propagation
                }
                // else {
                //      disabled error-handler
                // }
            }
        }

        return true;
    }

    public String toString() {
        return Arrays.toString(configurations);
    }
}
