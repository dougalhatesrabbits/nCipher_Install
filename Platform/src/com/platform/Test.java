package com.platform;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws IOException {

        ProcessBuilder pb = new ProcessBuilder("/usr/bin/export");
        Map<String, String> env = pb.environment();
        env.put("SOME_VARIABLE_NAME", "VARIABLE_VALUE");
        Process p = pb.start();
        InputStreamReader isr = new InputStreamReader(p.getInputStream());
        char[] buf = new char[1024];
        while (!isr.ready()) {
            ;
        }
        while (isr.read(buf) != -1) {
            System.out.println(buf);
        }
    }
}
