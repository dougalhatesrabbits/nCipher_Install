/*
 *   Copyright (c) 2020. David Brooke
 *   This file is subject to the terms and conditions defined in
 *   file 'LICENSE.txt', which is part of this source code package.
 */

import com.file.Checksum;
import com.file.ReadIso;
import com.platform.ConsoleColours;
import com.platform.RunProcBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.DirectoryScanner;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Linux extends SecurityWorld {
    // Always use the classname, this way you can refactor
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    /***
     * Child class
     *
     * @param name
     * @param version
     */


    final String CLASSPATH = "/opt/nfast/java/classes/nfjava.jar:/opt/nfast/java/classes/kmjava.jar:/opt/nfast/java/classes/keysafe.jar";
    final String JAVA_HOME = "/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.191.b12-1.el7_6.x86_64";
    final String JRE_HOME = "/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.191.b12-1.el7_6.x86_64/jre";
    final String JAVA_PATH = "";


    final String PATH = "/opt/nfast/bin:/opt/nfast/sbin";

    Path sw_filename = null;

    @Override
    void checkEnvVariables() {
        LOGGER.fine("running -checkEnvironmentVariables- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nChecking environment variables" + ConsoleColours.RESET);
        LOGGER.info("Checking environment variables");

        System.out.println(ConsoleColours.YELLOW + "NFAST_HOME= " + ConsoleColours.RESET + System.getenv("NFAST_HOME"));
        System.out.println(ConsoleColours.YELLOW + "PATH= " + ConsoleColours.RESET + System.getenv("PATH"));
        System.out.println(ConsoleColours.YELLOW + "NFAST_KMDATA= " + ConsoleColours.RESET + System.getenv("NFAST_KMDATA"));

        System.out.println("User TZ: " + System.getProperty("user.timezone"));
        System.out.println("OS Name: " + System.getProperty("os.name"));
        System.out.println("User Country: " + System.getProperty("user.country"));
        System.out.println("CPU Endian: " + System.getProperty("sun.cpu.endian"));
        System.out.println("User Home: " + System.getProperty("user.home"));
        System.out.println("User Lang: " + System.getProperty("user.language"));
        System.out.println("User Name: " + System.getProperty("user.name"));
        System.out.println("OS Version: " + System.getProperty("os.versiom"));
        System.out.println("User Dir: " + System.getProperty("user.dir"));
        System.out.println("OS Arch: " + System.getProperty("os.arch=x86_64"));

        System.out.println();
        Map<String, String> envVars = System.getenv();
        for (String envName : envVars.keySet()) {
            switch (envName) {
                case "NFAST_HOME":
                    if (envVars.get(envName).contains(NFAST_HOME)) {
                        System.out.println("$NFAST_HOME=" + NFAST_HOME);
                        System.out.format("%s=%s%n",
                                envName,
                                envVars.get(envName));
                        System.out.println(ConsoleColours.GREEN + "[OK]" +
                                ConsoleColours.RESET);
                    } else {
                        System.out.println("$NFAST_HOME=" + NFAST_HOME);
                        System.out.println(ConsoleColours.RED + "[NOK]" +
                                ConsoleColours.RESET);
                    }
                    //todo validate NFAST_HOME
                    break;
                case "NFAST_KMDATA":
                    if (envVars.get(envName).contains(NFAST_KMDATA)) {
                        System.out.println("$NFAST_KMDATA=" + NFAST_KMDATA);
                        System.out.format("%s=%s%n",
                                envName,
                                envVars.get(envName));
                        System.out.println(ConsoleColours.GREEN + "[OK]" +
                                ConsoleColours.RESET);
                    } else {
                        System.out.println("$NFAST_KMDATA=" + NFAST_KMDATA);
                        System.out.println(ConsoleColours.RED + "[NOK]" +
                                ConsoleColours.RESET);
                    }
                    //todo validate NFAST_KMDATA
                    break;
                case "PATH":
                    if (envVars.get(envName).contains(PATH)) {
                        System.out.println("$PATH=" + PATH);
                        System.out.format("%s=%s%n",
                                envName,
                                envVars.get(envName));
                        System.out.println(ConsoleColours.GREEN + "[OK]" +
                                ConsoleColours.RESET);
                    } else {
                        System.out.println("$PATH=" + PATH);
                        System.out.println(ConsoleColours.RED + "[NOK]" +
                                ConsoleColours.RESET);
                    }
                    //todo validate PATH
                    break;
            }
        }
    }

    @Override
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
        // TODO Do checksum on iso
        //new ReadIso(new File(sw_filename), destFile);
        try {
            //new ReadIso(isoFile, new File(sw_location));

            //new ReadIso(isoFile, new File(String.valueOf(file2)));
            new ReadIso(isoFile, file2);
            sw_iso = file2;

            System.out.println("\nCompleted Reading ISO, Mounted... " + sw_filename + " at " + sw_iso);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO checkmount2
    @Override
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

            }


            //inputStream.close();
            //reader.close();
            process.waitFor();
            status = process.exitValue();
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
        new Checksum().main(new String[]{"-t", "SHA-256", String.valueOf(isoFile)});

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

    @Override
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

    @Override
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
}
