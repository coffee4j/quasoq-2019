package de.rwth.swc.quasoq2019.runner;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static de.rwth.swc.quasoq2019.runner.Config.TRACES_FOLDER;

public class ExecutionEvaluator {

    public static void main(String[] args) throws IOException {
        final List<Path> traces = Files
                .walk(Paths.get(TRACES_FOLDER))
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());

        for(Path path : traces) {
            runEvaluation(path);
        }
    }

    private static void runEvaluation(Path path) throws IOException {
        final String str = path.toString().replace("traces", "evaluations");
        final Path target = Paths.get(str);

        if(Files.exists(target)) {
            System.out.println("INFO: skip " + str);
        } else {
            final StringBuilder builder = evaluate(path);

            final FileWriter fileWriter = new FileWriter(str);
            fileWriter.write(builder.toString());
            fileWriter.close();
        }
    }

    private static StringBuilder evaluate(Path path) throws IOException {
        final String content = Files.readString(path);
        final String[] rows = content
                .substring(content.indexOf('\n')+1) /* skip first line */
                .split("\\r?\\n");

        final StringBuilder builder = new StringBuilder();
        builder.append("NumberOfParameters;ValidValues;InvalidValues;ErrorHandlers;Strength;Size;NoOfVariants;IndexOfIncorrectEH;Absolute;"); // Min;Max;Avg;");
        builder.append("\n");

        for(String row : rows) {
            final String[] values = row.split(";");

            final String[] rawTraces = values[7]
                    .replace("[", "")
                    .replace("]", "")
                    .split(",");

            final int[] traces = Arrays.stream(rawTraces)
                    .mapToInt(trigger -> Integer.parseInt(trigger.trim()))
                    .toArray();

            final int number_of_test_suite_variants = traces.length;

            final long count_failing_test_suite_variants = Arrays.stream(traces)
                    .filter(trace -> trace > 0)
                    .count();

//            final double min_failing_test_inputs = Arrays.stream(traces)
//                    .filter(trace -> trace > 0)
//                    .mapToDouble(count -> count).min().orElse(0);
//            final double max_failing_test_inputs = Arrays.stream(traces)
//                    .filter(trace -> trace > 0)
//                    .mapToDouble(count -> count).max().orElse(0);
//            final double avg_failing_test_inputs = Arrays.stream(traces)
//                    .filter(trace -> trace > 0)
//                    .mapToDouble(count -> count).average().orElse(0);

            builder.append(values[0]).append(";"); /* no of parameters */
            builder.append(values[1]).append(";"); /* no of valid inputs */
            builder.append(values[2]).append(";"); /* no of invalid inputs */
            builder.append(values[3]).append(";"); /* no of error handlers */
            builder.append(values[4]).append(";"); /* strength */
            builder.append(values[5]).append(";"); /* size */
            builder.append(number_of_test_suite_variants).append(";");
            builder.append(values[6]).append(";"); /* index*/
            builder.append(count_failing_test_suite_variants).append(";");
            //builder.append(min_failing_test_inputs).append(";");
            //builder.append(max_failing_test_inputs).append(";");
            //builder.append(avg_failing_test_inputs).append(";");
            builder.append("\n");
        }

        return builder;
    }
}
