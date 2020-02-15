package com.platform;

import java.io.IOException;

public class MyInvokeOtherApps {
    /***
     * By using ProcessBuilder class, you can invoke any application in java. Below example shows how to run a java class
     * using ProcessBuilder class. Here first argument takes the command, and command arguments follows after this.
     * Here first argument invokes java.exe file, and second argument passes MyTest.classfile to java application.
     * You can pass any number of arguments to an application.
     * @param a
     * @throws IOException
     */
    public static void main(String a[]) throws IOException {

        //ProcessBuilder pb = new ProcessBuilder("java", "MyTest");
        ProcessBuilder pb = new ProcessBuilder("/usr/bin/java", "/Users/david/IdeaProjects/nCipher_Install/Envmap/Platform/src/com/platform");
        try {
            pb.start();
            System.out.println("Process has been started.");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
