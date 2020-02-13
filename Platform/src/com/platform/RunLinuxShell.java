package com.platform;

import java.io.IOException;

class RunLinuxShell {
    public static void main(String[] args) throws InterruptedException, IOException {
        {
            Process proc = Runtime.getRuntime().exec("./your_script.sh");
            Process p = Runtime.getRuntime().exec("////command////");
            proc.waitFor();
        }
    }
}