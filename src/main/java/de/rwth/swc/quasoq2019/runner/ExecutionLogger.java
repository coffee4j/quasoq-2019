package de.rwth.swc.quasoq2019.runner;

import de.rwth.swc.coffee4j.engine.util.Preconditions;
import de.rwth.swc.quasoq2019.*;
import org.apache.commons.lang3.tuple.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static de.rwth.swc.quasoq2019.runner.Config.LOGS_FOLDER;

public class ExecutionLogger {

    public static void main(String[] args) throws IOException, URISyntaxException {
        final int repetitions         = 100;
        final int maxStrength         = 5;

        final int noOfValidValues     = intValue(args, "valid-values");
        final int noOfInvalidValues   = intValue(args, "invalid-values");
        final int noOfParameters      = intValue(args, "parameters");
        final int noOfErrorHandlers   = intValue(args, "error-handlers");
        final int beginWith           = intValue(args, "begin-with");
        final int endBefore           = intValue(args, "end-before");

        System.out.println("INFO: execute logger with ");
        System.out.println("INFO: parameters: " + noOfParameters);
        System.out.println("INFO: valid-values: " + noOfValidValues);
        System.out.println("INFO: invalid-values: " + noOfInvalidValues);
        System.out.println("INFO: error-handlers: " + noOfErrorHandlers
                + " from: " + beginWith + " to: " + endBefore);

        runExecutionLogger(noOfParameters, noOfValidValues, noOfInvalidValues,
                noOfErrorHandlers, beginWith, endBefore, repetitions, maxStrength);
    }

    private static String getValue(String[] args, String name) {
        for(int i = 0; i < args.length; i++) {
            if(args[i].equals("--" + name)) {
                return args[i + 1];
            }
        }

        throw new IllegalArgumentException();
    }

    private static int intValue(String[] args, String name) {
        final String value = getValue(args, name);

        return Integer.parseInt(value);
    }

    private static void runExecutionLogger(int numberOfParameters,
                                           int numberOfValidValues,
                                           int numberOfInvalidValues,
                                           int numberOfErrorHandlers,
                                           int beginWithErrorHandlerIndex,
                                           int endBeforeErrorHandlerIndex,
                                           int repetitions,
                                           int maxStrength) throws IOException, URISyntaxException {
        Preconditions.check(numberOfErrorHandlers <= numberOfParameters);

        final TestModel testModel = buildTestModel(numberOfParameters, numberOfValidValues, numberOfInvalidValues);

        for(int indexOfIncorrectErrorHandler = beginWithErrorHandlerIndex;
            indexOfIncorrectErrorHandler < endBeforeErrorHandlerIndex;
            indexOfIncorrectErrorHandler++) {
            final SystemModel systemModel = buildSystemModel(numberOfParameters, numberOfErrorHandlers, indexOfIncorrectErrorHandler);

            for(int testingStrength = 1; testingStrength <= maxStrength; testingStrength++) {

                logExecution(numberOfParameters, numberOfValidValues, numberOfInvalidValues, numberOfErrorHandlers,
                        repetitions, testModel, indexOfIncorrectErrorHandler, systemModel, testingStrength);
            }
        }
    }

    private static SystemModel buildSystemModel(int numberOfParameters, int numberOfErrorHandlers, int indexOfIncorrectErrorHandler) {
        Preconditions.check(indexOfIncorrectErrorHandler >= 0);
        Preconditions.check(indexOfIncorrectErrorHandler < numberOfErrorHandlers);

        final int[] configurations = new int[numberOfParameters];
        Arrays.fill(configurations, -1);
        Arrays.fill(configurations, 0, numberOfErrorHandlers, 0);
        configurations[indexOfIncorrectErrorHandler] = 1;

        return new SystemModel(configurations);
    }

    private static TestModel buildTestModel(int numberOfParameters, int numberOfValidValues, int numberOfInvalidValues) {
        final int[] validValues = new int[numberOfParameters];
        Arrays.fill(validValues, numberOfValidValues);

        final int[] invalidValues = new int[numberOfParameters];
        Arrays.fill(invalidValues, numberOfInvalidValues);

        return new TestModel(validValues, invalidValues);
    }

    private static void logExecution(int numberOfParameters,
                                     int numberOfValidValues,
                                     int numberOfInvalidValues,
                                     int numberOfErrorHandlers,
                                     int repetitions,
                                     TestModel testModel,
                                     int indexOfIncorrectErrorHandler,
                                     SystemModel systemModel,
                                     int testingStrength) throws IOException, URISyntaxException {
        final String filename = LOGS_FOLDER
                + numberOfParameters + "_"
                + numberOfValidValues + "_"
                + numberOfInvalidValues + "_"
                + numberOfErrorHandlers + "_"
                + indexOfIncorrectErrorHandler + "_"
                + testingStrength
                + ".txt";

        final Path target = Paths.get(filename);

        if(Files.exists(target)) {
            System.out.println("INFO: skip " + filename);
        } else {
            System.out.println("INFO: log " + filename);

            final List<int[][]> arrays = createRandomizedCoveringArrays(testingStrength, repetitions, testModel);

            try(final FileWriter fileWriter = new FileWriter(filename)) {

                for (int[][] array : arrays) {
                    runTest(fileWriter, systemModel, testModel, array);
                }
            }
        }
    }

    private static List<int[][]> createRandomizedCoveringArrays(int testingStrength,
                                                                int repetitions,
                                                                TestModel testModel) throws IOException, URISyntaxException {
        final int[][] original = CoveringArrayLoader.load(
                testingStrength,
                testModel.getNumberOfParameters(),
                testModel.getNumberOfValues());

        return IntStream.range(0, repetitions)
                .mapToObj(i -> CoveringArrayRandomizer.randomize(testModel, original))
                .collect(Collectors.toList());
    }

    private static void runTest(FileWriter fileWriter,
                                SystemModel system,
                                TestModel testModel,
                                int[][] array) throws IOException {
        final Object[][] testInputs = CoveringArrayConverter.convert(testModel, array);

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("BEGIN\n");

        for (final Object[] testInput : testInputs) {
            final boolean result = system.test(testInput);

            stringBuilder.append(Arrays.toString(testInput));
            stringBuilder.append(" -> ");
            stringBuilder.append(result);
            stringBuilder.append("\n");
        }
        stringBuilder.append("END\n");
        stringBuilder.append("\n");

        fileWriter.write(stringBuilder.toString());
        fileWriter.flush();
    }
}
