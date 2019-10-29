package de.rwth.swc.quasoq2019.runner;

import de.rwth.swc.quasoq2019.CoveringArrayLoader;
import org.apache.commons.lang3.StringUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static de.rwth.swc.quasoq2019.runner.Config.LOGS_FOLDER;
import static de.rwth.swc.quasoq2019.runner.Config.TRACES_FOLDER;

public class ExecutionTracer {

    public static void main(String[] args) throws IOException, URISyntaxException {
        final List<Path> logs = Files
                .walk(Paths.get(LOGS_FOLDER))
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());

        final Map<String, List<Path>> groups = logs.stream()
                .collect(Collectors.groupingBy(ExecutionTracer::extractGroup));

        for(String group : groups.keySet()) {
            if(!traceExists(group)) {
                final List<Path> paths = groups.get(group);

                evaluateExecution(group, paths);
            }
        }
    }

    private static String extractGroup(Path path) {
        final String[] splits = path.getFileName().toString()
                .replace(".txt", "")
                .split("_");

        final int numberOfParameters = Integer.parseInt(splits[0]);
        final int numberOfValidValues = Integer.parseInt(splits[1]);
        final int numberOfInvalidValues = Integer.parseInt(splits[2]);
        final int numberOfErrorHandlers = Integer.parseInt(splits[3]);

        return numberOfParameters + "_"
                + numberOfValidValues + "_"
                + numberOfInvalidValues + "_"
                + numberOfErrorHandlers;
    }

    private static boolean traceExists(String group) {
        final String targetFilename = TRACES_FOLDER + group + ".txt";

        if(Files.exists(Paths.get(targetFilename))) {
            System.out.println("INFO: skip " + targetFilename);

            return true;
        }

        return false;
    }

    private static void evaluateExecution(String group, List<Path> executionLogPaths) throws IOException, URISyntaxException {
        final String targetFilename = TRACES_FOLDER + group + ".txt";

        final FileWriter fileWriter = new FileWriter(targetFilename);
        fileWriter.write("NumberOfParameters;ValidValues;InvalidValues;ErrorHandlers;Strength;Size;IndexOfIncorrectEH;Traces;\n");
        fileWriter.flush();

        for(Path path : executionLogPaths) {
            final String[] splits = path.getFileName().toString()
                    .replace(".txt", "")
                    .split("_");

            final int numberOfParameters = Integer.parseInt(splits[0]);
            final int numberOfValidValues = Integer.parseInt(splits[1]);
            final int numberOfInvalidValues = Integer.parseInt(splits[2]);
            final int numberOfErrorHandlers = Integer.parseInt(splits[3]);
            final int indexOfIncorrectErrorHandler = Integer.parseInt(splits[4]);
            final int testingStrength = Integer.parseInt(splits[5]);

            final int size = CoveringArrayLoader.size(testingStrength, numberOfParameters, (numberOfValidValues + numberOfInvalidValues));
            final int[] traces = runTracer(path);

            writeTrace(fileWriter,
                    numberOfParameters, numberOfValidValues, numberOfInvalidValues, numberOfErrorHandlers,
                    testingStrength, size, indexOfIncorrectErrorHandler, traces);
        }

        fileWriter.close();
    }

    private static void writeTrace(FileWriter fileWriter,
                                   int numberOfParameters,
                                   int numberOfValidValues,
                                   int numberOfInvalidValues,
                                   int numberOfErrorHandlers,
                                   int strength,
                                   int size,
                                   int index,
                                   int[] traces) throws IOException {
        fileWriter.write(numberOfParameters + ";");
        fileWriter.write(numberOfValidValues + ";");
        fileWriter.write(numberOfInvalidValues + ";");
        fileWriter.write(numberOfErrorHandlers + ";");
        fileWriter.write(strength + ";");
        fileWriter.write(size + ";");
        fileWriter.write(index + ";");
        fileWriter.write(Arrays.toString(traces) + ";");
        fileWriter.write("\n");
        fileWriter.flush();
    }

    private static int[] runTracer(Path path) throws IOException {
        final String content = Files.readString(path);
        final String[] blocks = content.split("END\n");

        return Arrays.stream(blocks)
                .filter(block -> block.contains("BEGIN"))
                .mapToInt(block -> StringUtils.countMatches(block, "false"))
                .toArray();
    }
}
