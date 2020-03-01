/*
 *   Copyright (c) 2020. David Brooke
 *   This file is subject to the terms and conditions defined in
 *   file 'LICENSE.txt', which is part of this source code package.
 */

package com.platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RunProcBuilder {
    // Always use the classname, this way you can refactor
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public void run(String[] args) throws IOException {
        LOGGER.fine("running -run- method");
        //Process p = Runtime.getRuntime().exec("pwd");
        //List<String> args = new ArrayList<String>();
        ProcessBuilder pb = new ProcessBuilder(args);
        String line = null;
        Integer status = -1;

        try {
            pb.redirectErrorStream();
            //try this https://stackoverflow.com/questions/3936023/printing-runtime-exec-outputstream-to-console
            //pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            Process process = pb.start();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            process.waitFor();
            //inputStream.close();
            //reader.close();
            //process.destroy();
            status = process.exitValue();
            if (status == -1) {
                System.out.println("Command runtime error");
            }
        } catch (Exception ex) {
            ex.printStackTrace();

            LOGGER.logp(Level.SEVERE,
                    "RunProcBuilder",
                    "run",
                    "Cannot run command", ex.getCause());
        }
    }
}
