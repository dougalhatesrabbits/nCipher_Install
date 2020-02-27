/*
 *   Copyright (c) 2020. David Brooke
 *   This file is subject to the terms and conditions defined in
 *   file 'LICENSE.txt', which is part of this source code package.
 */

import com.file.Find;
import com.file.ReadIso;
import com.file.UnTarFile;
import com.platform.ConsoleColours;
import com.platform.Platform;
import com.platform.RunProcBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.DirectoryScanner;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OSX extends SecurityWorld {
    // Always use the classname, this way you can refactor
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    /***
     * Child class
     *
     *
     */

    String sw_location = "Volumes/CDROM";
    public String  NFAST_HOME = "opt/nfast";
    Boolean removeStatus = false;
    Path sw_filename = null;

    void applySecWorld() throws IOException {
        LOGGER.fine("running -applySecurityWorld- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nInstalling Security World" + ConsoleColours.RESET);

        //String cmd = NFAST_HOME + "/sbin/install";
        String[] cmd = {"/bin/bash", "-c", "ls -l" +  NFAST_HOME};

        System.out.println(cmd);
        try {
            new RunProcBuilder().run(cmd);

            LOGGER.info(String.valueOf(cmd));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.logp(Level.WARNING,
                    "SecurityWorld",
                    "removeExistingSW",
                    "Cannot uninstall", e.fillInStackTrace());
        }
    }

    public Boolean checkExistingSW() throws IOException {
        LOGGER.fine("running -checkExistingSW- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nChecking Existing SW" + ConsoleColours.RESET);
        LOGGER.info("Checking existing SW");

        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nChecking for existing Security World in $NFAST_HOME: " + path + ConsoleColours.RESET);

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

    public void removeExistingSW() {
        LOGGER.fine("running removeExistingSW method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nRemoving old Security World" + ConsoleColours.RESET);

        //String cmd = "sudo " + NFAST_HOME + "/sbin/install -d";
        String cmd = "rm -rf " + NFAST_HOME;
        //TODO switch for production
        try {
            new RunProcBuilder().run(new String[]{"/bin/bash", "-c", cmd});
            //TODO
            //new RunProcBuilder().run(new String[]{cmd});
            System.out.println("Using NFAST " + cmd);
            LOGGER.info("Using NFAST " + cmd);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.logp(Level.WARNING,
                    "SecurityWorld",
                    "removeExistingSW",
                    "Cannot uninstall", e.fillInStackTrace());
        }
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
            file  = new File(sw_location);

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
            } catch(Exception e) {
                e.printStackTrace();
            }

        }
        // TODO Do checksum on iso
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

        System.out.println("check iso location: " + sw_location);
        LOGGER.info("check iso location: " + sw_location);

        if (sw_filename != null) {
            isoFile = new File(sw_filename.toString());
            //isoFile.setExecutable(true);
        } else {
            System.out.println("Cannot find iso");
            //isoFile = new File(sw_location + "/" + sw_filename);
        }

        // Lets try unmounting first
        String[] cmd = new String[]{"/bin/bash", "-c", "hdiutil unmount " +sw_location};
        //String[] cmd = new String[]{"/bin/bash", "-c", "ls -l", sw_location};

        ProcessBuilder pb = new ProcessBuilder(cmd);
        String line = null;
        Boolean status = false;
        Boolean output = false;

        try {
            pb.redirectErrorStream(true);
            //pb.directory(new File ("/"));

            Map<String, String> env = pb.environment();
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
                output = true;
            }
            if (process.exitValue() == 1) {
                status = false;
            }
            inputStream.close();
            reader.close();
            process.waitFor();
            //process.wait();
            process.destroy();
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.logp(Level.SEVERE,
                    "RunProcBuilder",
                    "run",
                    "Cannot run command", ex.getCause());
        }
        //return status;

        //TODO  Disable this for now

        if (!status) {
            System.out.println("couldn't unmount iso, checking if fs exists in" + sw_location);
            // Check if source file already exists on mnt
            String path = null;
            boolean bool = false;
            File file2 = null;
            File file = null;
            try {
                // 'creating' new file (paths)
                //file  = new File(sw_location,"linux");
                file = new File(sw_location);

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
        //new ReadIso(new File(sw_filename), destFile);
        try {
            //new ReadIso(isoFile, new File(sw_location));

            //new ReadIso(isoFile, new File(String.valueOf(file2)));
            //new ReadIso(isoFile, file2);
            //TODO pb mount
            File file = new File(sw_location);
            file.mkdir();
            file.canExecute();
            file.canWrite();
            file.canRead();

            cmd = new String[]{"/bin/bash", "-c", "hdiutil mount", String.valueOf(sw_filename)};

            pb = new ProcessBuilder(cmd);
            String line1 = null;
            status = false;

            try {
                pb.redirectErrorStream(true);
                //pb.directory(new File ("/"));

                Map<String, String> env = pb.environment();
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
                    status = true;
                }
                inputStream.close();
                reader.close();
                process.waitFor();
                //process.wait();
                process.destroy();
            } catch (Exception ex) {
                ex.printStackTrace();
                LOGGER.logp(Level.SEVERE,
                        "RunProcBuilder",
                        "run",
                        "Cannot run command", ex.getCause());
            }
            //return status;
            //sw_iso = sw_filename;

            System.out.println("\nCompleted Reading ISO, Mounted... " + sw_filename + " at " + sw_location);

        } catch (Exception e){
            e.printStackTrace();
            System.out.println("\nCould not mount... " + sw_filename + " at " + sw_location);
            System.exit(1);
        }
    }

    void getTars(){
        LOGGER.fine("running -getTars- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED+"getTars..." +ConsoleColours.RESET);
        LOGGER.info("getTars...");

        // https://ant.apache.org/manual/api/org/apache/tools/ant/DirectoryScanner.html
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setIncludes(new String[]{"**/*tar*"});
        //scanner.setBasedir(sw_location);
        scanner.setBasedir(sw_location);
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
            unpackSecWorld2(found, TAR_DESTINATION);
        }
    }
}
