package com.platform;

import java.io.*;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

class RunLinuxShellDirect implements Runnable {
    private InputStream inputStream;
    private Consumer<String> consumer;

    public RunLinuxShellDirect(InputStream inputStream, Consumer<String> consumer) {
        this.inputStream = inputStream;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        new BufferedReader(new InputStreamReader(inputStream)).lines()
                .forEach(consumer);
    }




    public static void main(String[] args) throws InterruptedException, IOException {
        boolean isWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");
        ProcessBuilder builder = new ProcessBuilder();
        if (isWindows) {
            builder.command("cmd.exe", "/c", "dir");
        } else {
            builder.command("sh", "-c", "ls");
        }
        builder.directory(new File(System.getProperty("user.home")));
        Process process = builder.start();
        RunLinuxShellDirect streamGobbler =
                new RunLinuxShellDirect(process.getInputStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);
        int exitCode = process.waitFor();
        assert exitCode == 0;
    }
}