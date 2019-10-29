package de.rwth.swc.quasoq2019.runner;

import java.io.IOException;
import java.net.URISyntaxException;

public class Runner {

    public static void main(String[] args) throws IOException, URISyntaxException {
        final String command = getValue(args, "do");

        if(command.equals("log")) {
            ExecutionLogger.main(args);
        } else if(command.equals("trace")) {
            ExecutionTracer.main(args);
        } else if(command.equals("evaluate")) {
            ExecutionEvaluator.main(args);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static String getValue(String[] args, String name) {
        for(int i = 0; i < args.length; i++) {
            if(args[i].equals("--" + name)) {
                return args[i + 1];
            }
        }

        throw new IllegalArgumentException();
    }
}
