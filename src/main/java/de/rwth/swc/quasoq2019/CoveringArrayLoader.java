package de.rwth.swc.quasoq2019;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static de.rwth.swc.quasoq2019.runner.Config.ARRAYS_FOLDER;

public class CoveringArrayLoader {

    public static int size(int t, int k, int v) throws IOException, URISyntaxException {
        final String str = loadCompleteFile(t, k, v);
        final String firstLine = str.substring(0, str.indexOf('\n'));

        return Integer.parseInt(firstLine);
    }

    public static int[][] load(int t, int k , int v) throws IOException, URISyntaxException {
        final String str = loadCAFromFile(t, k, v);
        final String ca = replaceDoNotCareValues(str, v);
        final String[] rows = splitIntoRows(ca);

        return Arrays.stream(rows)
                .map(CoveringArrayLoader::splitIntoValues)
                .map(CoveringArrayLoader::asIntArray)
                .toArray(int[][]::new);
    }

    private static String loadCompleteFile(int t, int k, int v) throws URISyntaxException, IOException {
        final String filename = "ca." + t + "." + v + "^" + k + ".txt";

        final Path path = Paths.get(ARRAYS_FOLDER + filename);

        if(!Files.exists(path)) {
            throw new IllegalStateException("ERROR: no file found for " + filename);
        }

        return Files.readString(path);
    }

    private static String loadCAFromFile(int t, int k, int v) throws URISyntaxException, IOException {
        final String str = loadCompleteFile(t, k, v);

        return str.substring(str.indexOf('\n')+1); /* skip first line */
    }

    private static String replaceDoNotCareValues(String str, int v) throws IOException {
        final UniformIntegerDistribution distribution = new UniformIntegerDistribution(0, v - 1);
        final int occurrences = StringUtils.countMatches(str, "-");

        if(occurrences > 0) {
            final int[] sample = distribution.sample(occurrences);

            for(int i = 0; i < occurrences; i++) {
                str = str.replaceFirst("-", Integer.toString(sample[i]));
            }
        }

        return str;
    }

    private static String[] splitIntoRows(String str) {
        return str.split("\\r?\\n");
    }

    private static String[] splitIntoValues(String str) {
        return str.split(" ");
    }

    private static int[] asIntArray(String[] array) {
        return Arrays.stream(array)
                .mapToInt(Integer::valueOf)
                .toArray();
    }
}
