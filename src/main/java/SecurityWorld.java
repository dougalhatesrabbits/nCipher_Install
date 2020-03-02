/*
 *   Copyright (c) 2020. David Brooke
 *   This file is subject to the terms and conditions defined in
 *   file 'LICENSE.txt', which is part of this source code package.
 */

import com.file.Checksum;
import com.file.CopyDir;
import com.file.ReadIso;
import com.file.UnTarFile;
import com.platform.ConsoleColours;
import com.platform.RunProcBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.tools.ant.DirectoryScanner;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SecurityWorld {
    // Always use the classname, this way you can refactor
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    /***
     * This is a parent
     */
    // Class attributes
    Path path = null;

    String sw_version = "126030";
    Path sw_filename = null;
    final String isoLocation = "/mnt/iso";
    File sw_iso = null;
    // This folder should exist, that's where
    // .tar file will go
    final String tarFolder = "/tmp";

    final static String NFAST_HOME = ("/opt/nfast");
    final String NFAST_KMDATA = "/opt/nfast/kmdata";
    final String TAR_DESTINATION = "/";


    final String CLASSPATH = "/opt/nfast/java:/opt/nfast/java/bin";
    final String JAVA_HOME = "/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.191.b12-1.el7_6.x86_64";
    final String JRE_HOME = "/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.191.b12-1.el7_6.x86_64/jre";
    final String JAVA_PATH = "";
    final String PATH = "/opt/nfast/bin:/opt/nfast/sbin";

    // List of source iso filepaths i.e /home/myiso.iso
    ArrayList<Path> sw_files = new ArrayList<>();
    Path iso_FilePath = null;
    Boolean removeStatus = false;

    // Methods
    void applyDrivers() throws IOException {
        LOGGER.fine("running -applyDrivers- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nAdding PCI drivers" + ConsoleColours.RESET);
        LOGGER.info("Adding PCI drivers");

        System.out.println(ConsoleColours.YELLOW + "Configure" + ConsoleColours.YELLOW);
        String[] cmd = {NFAST_HOME + "/driver/configure"};
        //new RunProcBuilder().run(cmd);

        ProcessBuilder pb = new ProcessBuilder(cmd);
        String line = null;
        int status = -1;

        try {
            pb.redirectErrorStream(true);
            pb.directory(new File(NFAST_HOME + "/driver"));


            Map<String, String> env = pb.environment();
            env.clear();
            env.put("User Name", "root");
            env.put("User Dir", NFAST_HOME + "/driver");
            //env.remove("var3");
            pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);

            Process process = pb.start();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            //OutputStream out = process.getOutputStream();

            while ((line = reader.readLine()) != null) {
                //System.out.println(line +pb.redirectErrorStream());
                System.out.println(line);
            }
            process.waitFor();
            status = process.exitValue();
            if (status == -1) {
                System.out.println("Command runtime error");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.logp(Level.SEVERE,
                    "SecurityWorld",
                    "applyDrivers",
                    "Cannot run command", ex.getCause());
        }

        System.out.println(ConsoleColours.YELLOW + "\nMake Clean" + ConsoleColours.RESET);
        cmd = new String[]{"/bin/bash", "-c", "make", "-C", NFAST_HOME + "/driver", NFAST_HOME + "/driver", "clean"};
        //new RunProcBuilder().run(cmd);
        pb = new ProcessBuilder(cmd);
        status = -1;
        try {
            pb.redirectErrorStream(true);
            pb.directory(new File(NFAST_HOME + "/driver"));

            Map<String, String> env = pb.environment();
            env.clear();
            env.put("User Dir", NFAST_HOME + "/driver");
            env.put("User Name", "root");
            //env.remove("var3");
            pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);

            Process process = pb.start();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            //OutputStream out = process.getOutputStream();

            while ((line = reader.readLine()) != null) {
                //System.out.println(line +pb.redirectErrorStream());
                System.out.println(line);
                //output = true;
            }
            process.waitFor();

            status = process.exitValue();
            if (status == -1) {
                System.out.println("Command runtime error");
            }
        } catch (Exception ex) {
            ex.printStackTrace();

            LOGGER.logp(Level.SEVERE,
                    "SecurityWorld",
                    "applyDrivers",
                    "Cannot run command", ex.getCause());
        }


        System.out.println(ConsoleColours.YELLOW + "Make" + ConsoleColours.RESET);
        cmd = new String[]{"/bin/bash", "-c", NFAST_HOME + "driver/make", "-C", NFAST_HOME + "/driver"};
        //new RunProcBuilder().run(cmd);
        //new RunProcBuilder().run(cmd);
        pb = new ProcessBuilder(cmd);
        status = -1;
        try {
            pb.redirectErrorStream(true);
            pb.directory(new File(NFAST_HOME + "/driver"));

            Map<String, String> env = pb.environment();
            env.clear();
            env.put("User Dir", NFAST_HOME + "/driver");
            env.put("User Name", "root");
            //env.remove("var3");
            pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);

            Process process = pb.start();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            //OutputStream out = process.getOutputStream();

            while ((line = reader.readLine()) != null) {
                //System.out.println(line +pb.redirectErrorStream());
                System.out.println(line);
                //output = true;
            }
            process.waitFor();

            status = process.exitValue();
            if (status == -1) {
                System.out.println("Command runtime error");
            }
        } catch (Exception ex) {
            ex.printStackTrace();

            LOGGER.logp(Level.SEVERE,
                    "SecurityWorld",
                    "applyDrivers",
                    "Cannot run command", ex.getCause());
        }


        System.out.println(ConsoleColours.YELLOW + "Make Install" + ConsoleColours.RESET);
        cmd = new String[]{"/bin/bash", "-c", NFAST_HOME + "/driver/make", "-C", NFAST_HOME + "/driver", "install"};
        //new RunProcBuilder().run(cmd);
        pb = new ProcessBuilder(cmd);
        status = -1;
        try {
            pb.redirectErrorStream(true);
            pb.directory(new File(NFAST_HOME + "/driver"));

            Map<String, String> env = pb.environment();
            env.clear();
            env.put("User Dir", NFAST_HOME + "/driver");
            env.put("User Name", "root");
            //env.remove("var3");
            pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);

            Process process = pb.start();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            //OutputStream out = process.getOutputStream();

            while ((line = reader.readLine()) != null) {
                //System.out.println(line +pb.redirectErrorStream());
                System.out.println(line);
                //output = true;
            }
            process.waitFor();

            status = process.exitValue();
            if (status == -1) {
                System.out.println("Command runtime error");
            }
        } catch (Exception ex) {
            ex.printStackTrace();

            LOGGER.logp(Level.SEVERE,
                    "SecurityWorld",
                    "applyDrivers",
                    "Cannot run command", ex.getCause());
        }
    }

    void restartService() throws IOException {
        LOGGER.fine("running -restartService- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nrestartService" + ConsoleColours.RESET);


        System.out.println("\nRestarting NFAST Service...");
        String[] cmd = {NFAST_HOME + "/sbin/init.d-ncipher", "restart"};
        new RunProcBuilder().run(cmd);

        cmd = new String[]{"/bin/bash", "-c", NFAST_HOME + "/bin/enquiry"};
        new RunProcBuilder().run(cmd);

        cmd = new String[]{NFAST_HOME + "/bin/nfkminfo"};
        new RunProcBuilder().run(cmd);
    }

    void applyFirmware() {
        LOGGER.fine("running -applyFirmware- method");
        System.out.println("Installing firmware");
        // TODO apply fw
    }

    void applyHotFix() {
        LOGGER.fine("running -applyHotFix- method");
        System.out.println("Installing hotfix");
        // TODO apply hotfix
    }

    void applySecWorld() throws IOException {
        LOGGER.fine("running -applySecurityWorld- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nInstalling Security World" + ConsoleColours.RESET);

        String cmd = NFAST_HOME + "/sbin/install";
        //String cmd = "sudo ls -l " + NFAST_HOME;

        System.out.println(cmd);
        try {
            new RunProcBuilder().run(new String[]{cmd});

            LOGGER.info(cmd);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.logp(Level.WARNING,
                    "SecurityWorld",
                    "removeExistingSW",
                    "Cannot uninstall", e.fillInStackTrace());
        }
    }

    void backup(String source, String target) throws IOException {
        LOGGER.fine("running -backup- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nPerforming Backup" + ConsoleColours.RESET);

        Path sourceDir = Paths.get(source);
        Path targetDir = Paths.get(target);

        try {
            Files.walkFileTree(sourceDir, new CopyDir(sourceDir, targetDir));
            System.out.println("Backup complete: " + targetDir);
        } catch (IOException e) {
            System.out.println("Unable to perform backup to " + targetDir);
            e.printStackTrace();
        }
    }

    void checkDrivers() throws IOException {
        LOGGER.fine("running -checkDrivers- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nChecking PCI drivers" + ConsoleColours.RESET);

        System.out.println("\nChecking standard linux gcc compiler is installed/at correct version");
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "gcc --version"});
        System.out.println("\nChecking standard linux make builder is installed/at correct version");
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "make --version"});

        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nChecking existing pci drivers" + ConsoleColours.RESET);
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "lspci -nn | grep -i Freescale"});
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nChecking existing usb drivers" + ConsoleColours.RESET);
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "lsusb | grep -i Future"});
    }

    void checkEnvVariables() {
        LOGGER.fine("running -checkEnvironmentVariables- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nChecking environment variables" + ConsoleColours.RESET);
        LOGGER.info("Checking environment variables");

        System.out.println(ConsoleColours.YELLOW + "NFAST_HOME= " + ConsoleColours.RESET + System.getenv("NFAST_HOME"));
        System.out.println(ConsoleColours.YELLOW + "PATH= " + ConsoleColours.RESET + System.getenv("PATH"));
        System.out.println(ConsoleColours.YELLOW + "NFAST_KMDATA= " + ConsoleColours.RESET + System.getenv("NFAST_KMDATA"));

        System.out.println("User TZ: " +System.getProperty("user.timezone"));
        System.out.println("OS Name: " +System.getProperty("os.name"));
        System.out.println("User Country: " +System.getProperty("user.country"));
        System.out.println("CPU Endian: " +System.getProperty("sun.cpu.endian"));
        System.out.println("User Home: " +System.getProperty("user.home"));
        System.out.println("User Lang: " +System.getProperty("user.language"));
        System.out.println("User Name: " +System.getProperty("user.name"));
        System.out.println("OS Version: " +System.getProperty("os.versiom"));
        System.out.println("User Dir: " +System.getProperty("user.dir"));
        System.out.println("OS Arch: " +System.getProperty("os.arch=x86_64"));

        System.out.println();
        Map<String, String> envVars = System.getenv();
        for (String envName : envVars.keySet()) {
            switch (envName) {
                case "NFAST_HOME":
                    if (envVars.get(envName).contains(NFAST_HOME)) {
                        System.out.println(NFAST_HOME);
                        System.out.format("%s=%s%n",
                                envName,
                                envVars.get(envName));
                        System.out.println(ConsoleColours.GREEN + "[OK]" +
                                ConsoleColours.RESET);
                    } else {
                        System.out.println(NFAST_HOME);
                        System.out.println(ConsoleColours.RED + "[NOK]" +
                                ConsoleColours.RESET);
                    }
                    //todo validate NFAST_HOME
                    break;
                case "NFAST_KMDATA":
                    if (envVars.get(envName).contains(NFAST_KMDATA)) {
                        System.out.println(NFAST_KMDATA);
                        System.out.format("%s=%s%n",
                                envName,
                                envVars.get(envName));
                        System.out.println(ConsoleColours.GREEN + "[OK]" +
                                ConsoleColours.RESET);
                    } else {
                        System.out.println(NFAST_KMDATA);
                        System.out.println(ConsoleColours.RED + "[NOK]" +
                                ConsoleColours.RESET);
                    }
                    //todo validate NFAST_KMDATA
                    break;
                case "PATH":
                    if (envVars.get(envName).contains(PATH)) {
                        System.out.println(PATH);
                        System.out.format("%s=%s%n",
                                envName,
                                envVars.get(envName));
                        System.out.println(ConsoleColours.GREEN + "[OK]" +
                                ConsoleColours.RESET);
                    } else {
                        System.out.println(PATH);
                        System.out.println(ConsoleColours.RED + "[NOK]" +
                                ConsoleColours.RESET);
                    }
                    //todo validate PATH
                    break;
            }
        }
    }

    public Boolean checkExistingSW() throws IOException {
        LOGGER.fine("running -checkExistingSW- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nChecking Existing SW" + ConsoleColours.RESET);
        LOGGER.info("Checking existing SW");

        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nChecking for existing Security World in: " + NFAST_HOME + ConsoleColours.RESET);

        if (Files.exists(Paths.get(NFAST_HOME))) {
            System.out.println("Found SW, removing previous install: \nConfirm (Y) to proceed, (N) to abort installation >");
            Scanner myObj = new Scanner(System.in);  // Create a Scanner object
            String confirm = myObj.nextLine();  // Read user input

            if (confirm.equalsIgnoreCase("y")) {
                System.out.println("You entered proceed " + confirm);
                removeStatus = true;// Output user input
            } else {
                System.out.println("You entered stop " + confirm);
                System.exit(1);
            }
        } else {
            System.out.println("No existing SW found, proceeding with install");
        }
        return removeStatus;
    }

    void checkJava() throws IOException {
        LOGGER.fine("running -checkJava- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nChecking Java" + ConsoleColours.RESET);
        LOGGER.info("Checking Java environment variables");

        System.out.println(ConsoleColours.YELLOW + "$JAVA_HOME= " + ConsoleColours.RESET + System.getenv("JAVA_HOME"));
        System.out.println(ConsoleColours.YELLOW + "$JRE_HOME= " + ConsoleColours.RESET + System.getenv("JRE_HOME"));
        System.out.println(ConsoleColours.YELLOW + "$CLASSPATH= " + ConsoleColours.RESET + System.getenv("CLASSPATH"));
        System.out.println(ConsoleColours.YELLOW + "$JAVA_PATH= " + ConsoleColours.RESET + System.getenv("JAVA_PATH"));

        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "java --version"}); // this may be a JRE (not reqd)
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "$JAVA_HOME/bin/javac -version"}); // This is what we want a JDK
        System.out.println("JDK Version: " + System.getProperty("java.version"));
        System.out.println("JRE Version: " + System.getProperty("java.runtime.version"));
        System.out.println("System Java Home: " + System.getProperty("java.home"));
        System.out.println(System.getProperty("java.vendor"));
        System.out.println(System.getProperty("java.vendor.url"));
        System.out.println("System Classpath: " + System.getProperty("java.class.path"));
        System.out.println("System Library path: " + System.getProperty("java.library.path"));
        System.out.println("Java Specification version: " + System.getProperty("java.specification.version"));

        Linux linux = new Linux();

        System.out.println();
        Map<String, String> envVars = System.getenv();
        for (String envName : envVars.keySet()) {
            switch (envName) {
                case "JAVA_HOME":
                    assert false;
                    if (envVars.get(envName).contains("java")) {
                        System.out.format("%s=%s%n",
                                envName,
                                envVars.get(envName));
                        System.out.println(ConsoleColours.GREEN + "[OK]" +
                                ConsoleColours.RESET);
                    } else {
                        System.out.println(ConsoleColours.RED + "[NOK]" +
                                ConsoleColours.RESET);
                    }
                    break;
                case "JRE_HOME":
                    assert false;
                    if (envVars.get(envName).toLowerCase().contains("jre")) {
                        System.out.format("%s=%s%n",
                                envName,
                                envVars.get(envName));
                        System.out.println(ConsoleColours.GREEN + "[OK]" +
                                ConsoleColours.RESET);
                    } else {
                        System.out.println(ConsoleColours.RED + "[NOK]" +
                                ConsoleColours.RESET);
                    }
                    break;
                case "JAVA_PATH":
                    assert false;
                    if (envVars.get(envName).toLowerCase().contains("java")) {
                        System.out.format("%s=%s%n",
                                envName,
                                envVars.get(envName));
                        System.out.println(ConsoleColours.GREEN + "[OK]" +
                                ConsoleColours.RESET);
                    } else {
                        System.out.println(ConsoleColours.RED + "[NOK]" +
                                ConsoleColours.RESET);
                    }
                    break;
                case "CLASSPATH":
                    assert false;
                    if (envVars.get(envName).toLowerCase().contains("java")) {
                        System.out.format("%s=%s%n",
                                envName,
                                envVars.get(envName));
                        System.out.println(ConsoleColours.GREEN + "[OK]" +
                                ConsoleColours.RESET);
                    } else {
                        System.out.println(ConsoleColours.RED + "[NOK]" +
                                ConsoleColours.RESET);
                    }
                    break;
            }

        }
    }

    void checkMount(Path sw_filename) {
        LOGGER.fine("running -checkMount- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nChecking if ISO is already mounted" + ConsoleColours.RESET);
        LOGGER.info("Checking if ISO is already mounted");
        File isoFile = null;
        //File isoFile = new File(sw_location + "/" + sw_filename);

        System.out.println("check iso location: " + sw_filename);
        LOGGER.info("check: " + sw_filename);

        if (sw_filename != null) {
            isoFile = new File(sw_filename.toString());
            //isoFile.setExecutable(true);
        } else {
            System.out.println("Cannot find iso");
            //isoFile = new File(sw_location + "/" + sw_filename);
        }

        // Check if source file exists on mnt
        String path = null;
        boolean bool = false;
        File file2 = null;
        File file = null;
        try {
            // 'creating' new file (paths)
            //file  = new File(sw_location,"linux");
            file = new File(isoLocation);

            //file.createNewFile(); //this gets done by ReadISO
            //System.out.println("Relative filepath 'file' " + file);
            LOGGER.fine((file.toString()));

            // creating new canonical from file object
            file2 = file.getCanonicalFile();
            //System.out.println("Canonical filepath 'file2' " + file2);
            LOGGER.fine((file2.toString()));

            // returns true if the file exists
            bool = file2.exists();
            //System.out.println("Does canon path 'file2' exists? " + bool);

            // returns absolute pathname
            path = file2.getAbsolutePath();
            //System.out.println("Absolute path of 'file2' " + path);

            // if file path exists
            if (bool) {
                // prints
                //System.out.print(path + " Exists? " + bool);
                LOGGER.fine(path + " Exists? " + bool);
            }
        } catch (Exception e) {
            // if any error occurs
            e.printStackTrace();
        }
        if(bool){

            try {
                if (file2.isDirectory()){
                    FileUtils.cleanDirectory(file2);
                    FileUtils.deleteDirectory(file2);
                    //FileUtils.forceDelete(file2);
                    /*
                    assert path != null;
                    Files.walk(Paths.get(path))
                            .sorted(Comparator.reverseOrder())
                            .map(Path::toFile)
                            .forEach(File::delete);

                     */


                } else if (file.delete()) {
                    System.out.println(file.getName() + " deleted");//getting and printing the file name
                    LOGGER.fine(file.getName() + " deleted");
                } else {
                    System.out.println("failed");
                    LOGGER.fine("failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        // TODO Do checksum on iso
        new Checksum().main(new String[]{"-t SHA256 ", String.valueOf(sw_filename)});

        //new ReadIso(new File(sw_filename), destFile);
        try {
            //new ReadIso(isoFile, new File(sw_location));

            //new ReadIso(isoFile, new File(String.valueOf(file2)));
            new ReadIso(isoFile, file2);
            sw_iso = file2;

            System.out.println("\nCompleted Reading ISO, Mounted... " + sw_filename + " at " + sw_iso);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    void checkMount2(Path sw_filename) {
        LOGGER.fine("running -checkMount- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nChecking if ISO is already mounted" + ConsoleColours.RESET);
        LOGGER.info("Checking if ISO is already mounted");
        File isoFile = null;
        //File isoFile = new File(sw_location + "/" + sw_filename);

        System.out.println("check iso location: " + isoLocation);
        LOGGER.info("check iso location: " + isoLocation);

        if (sw_filename != null) {
            isoFile = new File(sw_filename.toString());
            //isoFile.setExecutable(true);
        } else {
            System.out.println("Cannot find iso");
            //isoFile = new File(sw_location + "/" + sw_filename);
            System.exit(1);
        }

        // Lets try unmounting first
        String[] cmd = new String[]{"umount", "-d", isoLocation};

        ProcessBuilder pb = new ProcessBuilder(cmd);
        String line = null;
        int status = -1;
        //Boolean output = false;

        try {
            pb.redirectErrorStream(true);
            //pb.directory(new File ("/"));

            Map<String, String> env = pb.environment();
            env.put("User Name", "root");
            //env.remove("var3");

            Process process = pb.start();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            //OutputStream out = process.getOutputStream();

            while ((line = reader.readLine()) != null) {
                //System.out.println(line +pb.redirectErrorStream());
                System.out.println(line);
                //output = true;
            }


            //inputStream.close();
            //reader.close();
            process.waitFor();
            status = process.exitValue();
            //process.wait();
            //process.destroy();
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
        //return status;
        System.out.println("Unmount exit status = " + status);

        //TODO  Disable this for now
        if (status != 0) {
            System.out.println("couldn't unmount iso, checking if fs exists in " + isoLocation);
            // Check if source file already exists on dangling mnt
            String path = null;
            boolean bool = false;
            File file2 = null;
            File file = null;
            try {
                // 'creating' new file (paths)
                //file  = new File(sw_location,"linux");
                file = new File(isoLocation);

                //file.createNewFile(); //this gets done by ReadISO
                //System.out.println("Relative filepath 'file' " + file);
                LOGGER.fine((file.toString()));

                // creating new canonical from file object
                file2 = file.getCanonicalFile();
                //System.out.println("Canonical filepath 'file2' " + file2);
                LOGGER.fine((file2.toString()));

                // returns true if the file exists
                bool = file2.exists();
                //System.out.println("Does canon path 'file2' exists? " + bool);

                // returns absolute pathname
                path = file2.getAbsolutePath();
                //System.out.println("Absolute path of 'file2' " + path);

                // if file path exists
                if (bool) {
                    // prints
                    //System.out.print(path + " Exists? " + bool);
                    LOGGER.fine(path + " Exists? " + bool);
                }
            } catch (Exception e) {
                // if any error occurs
                e.printStackTrace();
            }
            if (bool) {
                try {
                    if (file2.isDirectory()) {
                        FileUtils.cleanDirectory(file2);
                        FileUtils.deleteDirectory(file2);
                        //FileUtils.forceDelete(file2);
                        /*
                        assert path != null;
                        Files.walk(Paths.get(path))
                                .sorted(Comparator.reverseOrder())
                                .map(Path::toFile)
                                .forEach(File::delete);
                         */

                    } else if (file.delete()) {
                        System.out.println(file.getName() + " deleted");//getting and printing the file name
                        LOGGER.fine(file.getName() + " deleted");
                    } else {
                        System.out.println("failed");
                        LOGGER.fine("failed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("ISO unmounted");
        }


        // TODO Do checksum on iso
        System.out.println("Check sum on:" + iso_FilePath);
        new Checksum().main(new String[]{"-t SHA-256", String.valueOf(isoFile)});

        try {
            //new ReadIso(isoFile, new File(sw_location));

            //new ReadIso(isoFile, new File(String.valueOf(file2)));
            //new ReadIso(isoFile, file2);
            //TODO pb mount
            File file = new File(isoLocation);
            file.mkdirs();
            file.canExecute();
            file.canWrite();
            file.canRead();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("\nCould not create mount... " + sw_filename + " at " + isoLocation);
            System.exit(1);
        }

        try {
            cmd = new String[]{"mount", "-o", "loop", String.valueOf(sw_filename), isoLocation};
            pb.command(cmd);
            Process process = pb.start();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            while ((line = reader.readLine()) != null) {
                //System.out.println(line +pb.redirectErrorStream());
                System.out.println(line);
                //status = true;
            }
            //inputStream.close();
            //reader.close();

            int exitValue = process.waitFor();

            System.out.println("Mount exit value = " + exitValue);
            if (exitValue != 0) {
                cmd = new String[]{"mount", "-t", "iso9660", "-o", "loop", String.valueOf(sw_filename), isoLocation};
                pb.command(cmd);
                process = pb.start();
                inputStream = process.getInputStream();
                reader = new BufferedReader(new InputStreamReader(
                        inputStream));
                while ((line = reader.readLine()) != null) {
                    //System.out.println(line +pb.redirectErrorStream());
                    System.out.println(line);
                    //status = true;
                }
                //inputStream.close();
                //reader.close();
                process.waitFor();
            }
            //process.wait();
            //process.destroy();
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.logp(Level.SEVERE,
                    "RunProcBuilder",
                    "run",
                    "Cannot run command", ex.getCause());
        }
        //return status;
        //sw_iso = sw_filename;

        System.out.println("\nCompleted Reading ISO, Mounted... " + sw_filename + " at " + isoLocation);


    }

    void checkUsers() throws IOException {
        LOGGER.fine("running -checkUsers- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nChecking if Users are in correct Groups" + ConsoleColours.RESET);
        LOGGER.info("Checking if Users are in correct Groups");

        System.out.println(ConsoleColours.YELLOW + "\nUsers" + ConsoleColours.RESET);
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "getent passwd | grep david | cut -d: -f1"});
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "getent passwd | grep nfast | cut -d: -f1"});
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "getent passwd | grep raserv | cut -d: -f1"});
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "getent passwd | grep ncsnmpd | cut -d: -f1"});

        System.out.println(ConsoleColours.YELLOW + "\nGroups" + ConsoleColours.RESET);
        //todo logic on filter return
        //new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "getent group"}); // Get all system groups
        //new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "awk -F':' '{ print $1}' /etc/passwd"});
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "getent group | grep david | cut -d: -f1"});
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "getent group | grep nfast | cut -d: -f1"});
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "getent group | grep raserv | cut -d: -f1"});
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "getent group | grep ncsnmpd | cut -d: -f1"});// Get all system groups


        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "source .bash_profile"});
        //new RunProcBuilder().run(new String[]{"/usr/bin/bash", "-c", "source .bash_profile"});

        // TODO add users if not exist
        //new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "sudo groupadd nfast"});
        //new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "sudo usermod -a -G nfast nfast"});

    }

    void configureJava() throws IOException {
        LOGGER.fine("running -configureJava- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nConfiguring Java" + ConsoleColours.RESET);
        LOGGER.info("Configuring Java environment variables");

        // JDK 9
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "export JAVA_HOME=/opt/java/jdk-9.0.4/"});
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "export JRE_HOME=/opt/java/jdk-9.0.4/jre"});
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "export PATH=$PATH:/opt/java/jdk-9.0.4/bin:/opt/java/jdk-9.0.4/jre/bin"});
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "export CLASSPATH=/opt/java/jdk-9.0.4/jre"});
        //TODO validate java config logic
    }

    public Path getIsoChoices() {
        //TODO menu option [Q]uit
        LOGGER.fine("running -getIsoChoices- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nGet ISO user choice" + ConsoleColours.RESET);
        LOGGER.info("Get ISO user choice");

        int confirm = 0;

        if (sw_files.isEmpty()) {
            System.out.println(ConsoleColours.RED + "\nNo ISO found on system, aborting install" + ConsoleColours.RESET);
            System.exit(1);
        }

        int i = 1;
        System.out.println();
        for (Path file : sw_files) {
            System.out.println("[" + i + "] " + file);
            i = i + 1;
        }
        System.out.println("[0] Quit");

        System.out.println(ConsoleColours.YELLOW + "Enter choice: " + ConsoleColours.RESET);
        LOGGER.info("Enter choice: ");
        Scanner sw = new Scanner(System.in);

        confirm = sw.nextInt();

        if (confirm == 0) {
            System.exit(0);
        }


        iso_FilePath = sw_files.get(confirm - 1);
        System.out.println("[" + confirm + "]" + ConsoleColours.YELLOW + iso_FilePath + ConsoleColours.RESET);
        LOGGER.info("You entered " + iso_FilePath);

        return iso_FilePath;
    }

    ArrayList<Path> getSecWorld(String searchpath) throws IOException {
        LOGGER.fine("running -getSecWorld- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "Searching for ISO in directory: " + searchpath + ConsoleColours.RESET);
        LOGGER.info("Searching for ISO in directory: " + searchpath);

        /*
        try {
            Find.Finder iso = new Find.Finder("*linux*.iso");
            //String ext = FilenameUtils.getExtension(tar);
            //Find.main(new String[]{"/Users/david/IdeaProjects/nCipher_Install", "-name", "*test"});
            Find.main(new String[]{searchpath, "-name", "*linux*.iso"});
            //isoList = iso.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
         */

        /*
        File[] dirs = new File(searchpath).listFiles((FileFilter) new WildcardFileFilter("*linux*.iso"));
        for (File dir : dirs) {
            System.out.println(dir);
            LOGGER.info((Supplier<String>) dir);
            if (dir.isDirectory()) {
                File[] files = dir.listFiles((FileFilter) new WildcardFileFilter("*linux*.iso"));
            }
        }
        */

        // https://ant.apache.org/manual/api/org/apache/tools/ant/DirectoryScanner.html
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setIncludes(new String[]{"**/*linux*.iso"});
        scanner.setBasedir(searchpath);
        scanner.setCaseSensitive(false);
        scanner.setFollowSymlinks(false);
        //scanner.setExcludes(new String[]{"**/."});
        try {
            scanner.scan();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] files = scanner.getIncludedFiles();
        System.out.println("Files: " +files.length);
        LOGGER.info("Files: " +files.length);
        //ArrayList<Path> swfiles = new ArrayList<Path>();
        //String found = null;
        for (String file : files) {
            String found = scanner.getBasedir() + "/" + file;
            //String ext = FilenameUtils.getExtension(found);
            //System.out.println(ext);
            Path path = Paths.get(found);
            System.out.println("Found: " + path);
            LOGGER.info("Found: " + path);
            sw_files.add(path);
        }
        return sw_files;
    }

    //Overloaded
    ArrayList<Path> getSecWorld(String searchpath, String sw) {
        LOGGER.fine("running -getSecWorld- overloaded");
        System.out.println("Searching for: " + sw + " in directory: " + searchpath);
        LOGGER.info("Searching for: " + sw + " in directory: " + searchpath);

        Path path = null;

        // https://ant.apache.org/manual/api/org/apache/tools/ant/DirectoryScanner.html
        DirectoryScanner scanner = new DirectoryScanner();

        //scanner.setExcludes(new String[]{".*/*" + sw});
        scanner.setIncludes(new String[]{"**/*" + sw});
        scanner.setBasedir(searchpath);
        scanner.setCaseSensitive(false);
        scanner.setFollowSymlinks(false);

        try {
            scanner.scan();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] files = scanner.getIncludedFiles();
        System.out.println("Files: " + scanner.getIncludedFilesCount());
        LOGGER.info("Files: " + scanner.getIncludedFilesCount());
        for (String file : files) {
            String found = scanner.getBasedir() + "/" + file;
            path = Paths.get(found);
            LOGGER.info("Found: " + path);
            System.out.println("Found: " + found);
            sw_files.add(path);
        }
        return sw_files;
    }

    void getTars() {
        LOGGER.fine("running -getTars- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "getTars..." + ConsoleColours.RESET);
        LOGGER.info("getTars...");

        // https://ant.apache.org/manual/api/org/apache/tools/ant/DirectoryScanner.html
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setIncludes(new String[]{"**/*tar*"});
        scanner.setBasedir(isoLocation);
        //scanner.setBasedir(sw_iso);
        scanner.setCaseSensitive(false);
        scanner.setFollowSymlinks(false);
        try {
            scanner.scan();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] files = scanner.getIncludedFiles();
        //ArrayList<Path> swfiles = new ArrayList<Path>();
        //String found = null;
        for (String file : files) {
            String found = scanner.getBasedir() + "/" + file;
            Path path = Paths.get(found);


            //tar_files.add(path);
            System.out.println(path);
            LOGGER.info(path.toString());
            unpackSecWorld2(found);
        }
    }

    void installJava() {
        LOGGER.fine("running -installJava- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nInstalling Java" + ConsoleColours.RESET);
        LOGGER.info("Installing Java");

        String home = System.getProperty("user.home");
        /*
        try {
            IOUtils.copy(
                    new URL("https://www.oracle.com/java/technologies/javase-jdk13-doc-downloads.html#license-lightbox").openStream(),
                    new FileOutputStream(home + "/Documents/oracle")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

         */
        // JDK 9
        try {
            System.out.println("sudo cd /opt/java");
            new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "sudo cd /opt/java"});

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.println("wget");
            String cmd = "sudo wget --no-cookies --no-check-certificate --header \"Cookie: oraclelicense=accept-securebackup-cookie\" http://download.oracle.com/otn-pub/java/jdk/9.0.4+11/c2514751926b4512b076cc82f959763f/jdk-9.0.4_linux-x64_bin.tar.gz";
            new RunProcBuilder().run(new String[]{"/bin/bash", "-c", cmd});
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "sudo tar -zxvf jdk-9.0.4_linux-x64_bin.tar.gz"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "sudo cd jdk-9.0.4/"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "sudo update-alternatives --install /usr/bin/java java /opt/java/jdk-9.0.4/bin/java 100"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "sudo update-alternatives --config java"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "sudo update-alternatives --install /usr/bin/javac javac /opt/java/jdk-9.0.4/bin/javac 100"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "sudo update-alternatives --config javac"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "sudo update-alternatives --install /usr/bin/jar jar /opt/java/jdk-9.0.4/bin/jar 100"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "sudo update-alternatives --config jar"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        //TODO validate java install logic
    }

    public void removeExistingSW() {
        LOGGER.fine("running removeExistingSW method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nRemoving old Security World" + ConsoleColours.RESET);

        String[] cmd = {NFAST_HOME + "/sbin/install", "-u"};
        //String cmd = "sudo rm -rf " + NFAST_HOME;

        try {
            new RunProcBuilder().run(cmd);
            System.out.println("Using NFAST " + Arrays.toString(cmd));
            LOGGER.info("Using NFAST " + Arrays.toString(cmd));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.logp(Level.WARNING,
                    "SecurityWorld",
                    "removeExistingSW",
                    "Cannot uninstall", e.fillInStackTrace());
        }
    }

    void unpackSecWorld(String tar, String dest) {
        LOGGER.fine("running -UnpackSecurityWorld- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "Unpacking Security World..." + ConsoleColours.RESET);
        LOGGER.info("Unpacking Security World...");

        //String ext = FilenameUtils.getExtension(tar);
        //String ext = FileUtils.getName(
        if (tar.contains("tgz")) {
            System.out.println("We have a Gunzip compressed tarball " + tar);
            LOGGER.info("We have a Gunzip compressed tarball " + tar);
            UnTarFile untar = new UnTarFile();
            try {
                File inputFile = new File(tar);
                String outputFile = UnTarFile.getFileName(inputFile, tarFolder);
                System.out.println(ConsoleColours.GREEN + "outputFile " + outputFile + ConsoleColours.RESET);
                LOGGER.info("outputFile " + outputFile);
                File tarFile = new File(outputFile);
                // Calling method to decompress file
                tarFile = untar.deCompressGZipFile(inputFile, tarFile);
                File destFile = new File(dest);
                if (!destFile.exists()) {
                    destFile.mkdir();
                }
                // Calling method to untar file
                untar.unTarFile(tarFile, destFile);

            } catch (IOException e) {
                LOGGER.logp(Level.WARNING,
                        "Linux",
                        "unpackSecurityWorld",
                        "Cannot unpack", e.getCause());
            }
        } else if (tar.contains("zip")) {
            System.out.println("We have a standard zip file " + tar);
            LOGGER.info("We have a standard zip file " + tar);
            UnTarFile untar = new UnTarFile();
            try {
                //String inputFile = tar;
                File destFile = new File(dest);
                //untar.unzip(inputFile, destFile);
                untar.unzip(tar, destFile);
            } catch (IOException e) {
                LOGGER.logp(Level.WARNING,
                        "Linux",
                        "unpackSecurityWorld",
                        "Cannot unpack", e.fillInStackTrace());
                LOGGER.logp(Level.WARNING,
                        "Linux",
                        "unpackSecurityWorld",
                        "Cannot unpack", e.getCause());
            }
        } else {
            System.out.println("We have a standard compressed tarball " + tar);
            LOGGER.info("We have a standard compressed tarball " + tar);
            UnTarFile untar = new UnTarFile();
            try {
                File inputFile = new File(tar);
                File destFile = new File(dest);
                untar.unTarFile(inputFile, destFile);
            } catch (IOException e) {
                LOGGER.logp(Level.WARNING,
                        "Linux",
                        "unpackSecurityWorld",
                        "Cannot unpack", e.fillInStackTrace());
                LOGGER.logp(Level.WARNING,
                        "Linux",
                        "unpackSecurityWorld",
                        "Cannot unpack", e.getCause());
            }
        }
    }

    void unpackSecWorld2(String tar) {
        LOGGER.fine("running -UnpackSecurityWorld- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "Unpacking Security World..." + ConsoleColours.RESET);
        LOGGER.info("Unpacking Security World...");

        String ext = FilenameUtils.getExtension(tar);
        //String ext = FileUtils.getName(
        String[] cmd = null;

        if (ext.equals("tgz")) {
            System.out.println("We have a Gunzip compressed tarball " + tar);
            LOGGER.info("We have a Gunzip compressed tarball " + tar);
            cmd = new String[]{"tar", "-zxf", tar};
        } else if (ext.equals("zip")) {
            System.out.println("We have a standard zip file " + tar);
            LOGGER.info("We have a standard zip file " + tar);
            cmd = new String[]{"unzip", tar};
        } else if (ext.equals("tar") || ext.equals("gz")) {
            System.out.println("We have a standard compressed tarball " + tar);
            LOGGER.info("We have a standard compressed tarball " + tar);
            cmd = new String[]{"tar", "-xf", tar};
        } else {
            System.out.println("Unknown archive bundle... exiting " + tar);
            LOGGER.info("Unknown archive bundle... exiting " + tar);
            System.exit(1);
        }


        ProcessBuilder pb = new ProcessBuilder(cmd);
        String line = null;
        Integer status = -1;

        try {
            pb.redirectErrorStream(true);
            pb.directory(new File("/"));

            Map<String, String> env = pb.environment();
            env.clear();
            env.put("User Name", "root");
            //env.put("User Dir", NFAST_HOME + "/driver");
            //env.put("TERM", "xterm-256color");
            //env.remove("var3");

            Process process = pb.start();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            //OutputStream out = process.getOutputStream();

            while ((line = reader.readLine()) != null) {
                //System.out.println(line +pb.redirectErrorStream());
                System.out.println(line);
                //status = true;
            }
            //inputStream.close();
            //reader.close();
            process.waitFor();
            //process.wait();
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
        //return status;


}
