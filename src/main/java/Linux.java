/*
 *   Copyright (c) 2020. David Brooke
 *   This file is subject to the terms and conditions defined in
 *   file 'LICENSE.txt', which is part of this source code package.
 */

import com.file.Find;
import com.file.ReadIso;
import com.file.UnTarFile;
import com.platform.ConsoleColours;
import com.platform.RunProcBuilder;
import org.apache.tools.ant.DirectoryScanner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
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

    Path sw_filename = null;
    String sw_version = "126030";
    final String NFAST_HOME = "/opt/nfast";
    final String NFAST_KMDATA = "opt/nfast/kmdata";

    File sw_iso = null;
    Path iso_File = null;
    final String CLASSPATH = "opt/nfast/java";
    final String JAVA_HOME = "/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.191.b12-1.el7_6.x86_64";
    final String JRE_HOME = "/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.191.b12-1.el7_6.x86_64/jre";
    final String PATH = "$NFAST_HOME/bin:$NFAST_HOME/sbin";
    String sw_location = "/mnt/iso";
    // List of source iso filepaths i.e /home/myiso.iso
    ArrayList<Path> sw_files = new ArrayList<>();

    // This folder should exist, that's where
    // .tar file will go
    String TAR_FOLDER = "/tmp";

    public Path getIsoChoices() {
        LOGGER.fine("running -getIsoChoices- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nGet ISO user choice" + ConsoleColours.RESET);
        LOGGER.info("Get ISO user choice");

        int i = 1;
        System.out.println();
        for (Path file : sw_files) {
            System.out.println("[" + i + "] " + file);
            i = i + 1;
        }

        System.out.println(ConsoleColours.YELLOW + "Enter choice: " + ConsoleColours.RESET);
        LOGGER.info("Enter choice: ");
        Scanner sw = new Scanner(System.in);  // Create a Scanner object
        int confirm = sw.nextInt();
        iso_File =  sw_files.get(confirm-1);
        System.out.println("You entered " + "[" + confirm + "]" + ConsoleColours.YELLOW +iso_File+ConsoleColours.RESET);
        LOGGER.info("You entered " + iso_File);

        return iso_File;
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
            System.out.println("Relative filepath 'file' " + file);
            LOGGER.info((file.toString()));

            // creating new canonical from file object
            file2 = file.getCanonicalFile();
            System.out.println("Canonical filepath 'file2' " + file2);
            LOGGER.info((file2.toString()));

            // returns true if the file exists
            bool = file2.exists();
            System.out.println("Does canon path 'file2' exist? " + bool);

            // returns absolute pathname
            path = file2.getAbsolutePath();
            System.out.println("Absolute path of 'file2' " + path);

            // if file path exists
            if (bool) {
                // prints
                System.out.print(path + " Exists? ");
                LOGGER.info(path + " Exists? ");
            }
        } catch (Exception e) {
            // if any error occurs
            e.printStackTrace();
        }
        if(bool){

            try {
                if (file2.isDirectory()){
                    //org.apache.commons.io.FileUtils.deleteDirectory(file2);
                    //FileUtils.cleanDirectory(file2);
                    //FileUtils.deleteDirectory(file2);
                    //FileUtils.forceDelete(file2);
                    assert path != null;
                    Files.walk(Paths.get(path))
                            .sorted(Comparator.reverseOrder())
                            .map(Path::toFile)
                            .forEach(File::delete);


                } else if (file.delete()) {
                    System.out.println(file.getName() + " deleted");//getting and printing the file name
                    LOGGER.info(file.getName() + " deleted");
                } else {
                    System.out.println("failed");
                    LOGGER.info("failed");
                }
            } catch(Exception e) {
                e.printStackTrace();
            }

        }
        // TODO Do checksum on iso
        //new ReadIso(new File(sw_filename), destFile);
        try {
            //new ReadIso(isoFile, new File(sw_location));
            sw_iso = file2;
            new ReadIso(isoFile, new File(String.valueOf(file2)));
            new ReadIso(isoFile, file2);

            System.out.println("\nCompleted ReadingISO, Mounted... " + sw_filename + " at " + sw_iso);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    void checkUsers() throws IOException {
        LOGGER.fine("running -checkUsers- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED+"\nChecking if Users are in correct Groups"+ConsoleColours.RESET);
        LOGGER.info("Checking if Users are in correct Groups");
        //Process p = Runtime.getRuntime().exec("////command////");
        //Process p = Runtime.getRuntime().exec("pwd");
        System.out.println(ConsoleColours.YELLOW+"\nUsers"+ConsoleColours.RESET);
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "sudo cat /etc/passwd"});
        System.out.println(ConsoleColours.YELLOW+"\nGroups"+ConsoleColours.RESET);
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "sudo cat /etc/group"});
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "source .bash_profile"});

        // TODO add users if not exist

    }

    void getTars(){
        LOGGER.fine("running -getTars- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED+"getTars..." +ConsoleColours.RESET);
        LOGGER.info("getTars...");
        // https://ant.apache.org/manual/api/org/apache/tools/ant/DirectoryScanner.html
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setIncludes(new String[]{"**/*tar*"});
        //scanner.setBasedir(sw_location);
        scanner.setBasedir(sw_iso);
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
            unpackSecWorld(found, NFAST_HOME);
        }
    }

    void unpackSecWorld(String tar, String dest) {
        LOGGER.fine("running -UnpackSecurityWorld- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED+"Unpacking Security World..." +ConsoleColours.RESET);
        LOGGER.info("Unpacking Security World...");

        //String ext = FilenameUtils.getExtension(tar);
        //String ext = FileUtils.getName(
        if (tar.contains("tgz")) {
            System.out.println("We have a Gunzip compressed tarball " + tar);
            LOGGER.info("We have a Gunzip compressed tarball " + tar);
            UnTarFile untar = new UnTarFile();
            try {
                File inputFile = new File(tar);
                String outputFile = UnTarFile.getFileName(inputFile, TAR_FOLDER);
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
                String inputFile = tar;
                File destFile = new File(dest);
                untar.unzip(inputFile, destFile);
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

    ArrayList<Path> getSecWorld(String searchpath) throws IOException {
        LOGGER.fine("running -getSecWorld- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "Getting Security World" +ConsoleColours.RESET);
        LOGGER.info("Getting Security World");

        try {

            Find.Finder iso = new Find.Finder("*linux*.iso");
            //String ext = FilenameUtils.getExtension(tar);
            //Find.main(new String[]{"/Users/david/IdeaProjects/nCipher_Install", "-name", "*test"});
            Find.main(new String[]{searchpath, "-name", "*linux*.iso"});
            //isoList = iso.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
            //String ext = FilenameUtils.getExtension(found);
            //System.out.println(ext);
            Path path = Paths.get(found);
            sw_files.add(path);
            System.out.println(path);
            LOGGER.info(path.toString());
        }


        return sw_files;
    }

    public Path getSecWorld(String searchpath, String sw) throws IOException {
        LOGGER.fine("running -getSecWorld- overloaded");
        System.out.println("Getting Security world overloaded: " +sw);
        LOGGER.info("Getting Security world overloaded: " +sw);

        try {
            //Find.Finder iso = new Find.Finder(sw);
            //String ext = FilenameUtils.getExtension(tar);
            //Find.main(new String[]{"/Users/david/IdeaProjects/nCipher_Install", "-name", "*test"});

            Find.main(new String[]{searchpath, "-name", sw});
            //isoList = iso.getResult();
        } catch (Exception e){
            e.printStackTrace();
        }

        /*
        File[] dirs = new File(searchpath).listFiles((FileFilter) new WildcardFileFilter(sw));
        for (File dir : dirs) {
            System.out.println(dir);
            LOGGER.info((Supplier<String>) dir);
            if (dir.isDirectory()) {
                File[] files = dir.listFiles((FileFilter) new WildcardFileFilter(sw));
            }
        }
        */

        // https://ant.apache.org/manual/api/org/apache/tools/ant/DirectoryScanner.html
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setIncludes(new String[]{"**/" + sw});
        scanner.setBasedir(searchpath);
        scanner.setCaseSensitive(false);
        scanner.setFollowSymlinks(false);
        scanner.setExcludes(new String[]{"**/."});

        try {
            scanner.scan();
        } catch (Exception e){
            e.printStackTrace();
        }
        String[] files = scanner.getIncludedFiles();
        System.out.println("Files: " +files.length);
        LOGGER.info("Files: " +files.length);
        //ArrayList<Path> swfiles = new ArrayList<Path>();
        //String found = null;
        for (String file : files) {
            String found = scanner.getBasedir() + "/" + file;
            Path path = Paths.get(found);


            //sw_filename = path;
            System.out.println("Path: " +path);
            LOGGER.info("Path: " +path);
            System.out.println("Found: " +found);
            return path;
        }
        return path;
    }

    void applyDrivers() throws IOException {
        LOGGER.fine("running -applyDrivers- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED+"\nAdding PCI drivers"+ConsoleColours.RESET);
        LOGGER.info("Adding PCI drivers");
        //Process p = Runtime.getRuntime().exec("////command////");
        //Process p = Runtime.getRuntime().exec("pwd");
        System.out.println("\nChecking standard linux gcc compiler is installed/at correct version");
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "gcc --version"});
        System.out.println("\nChecking existing pci drivers");
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "lspci -nn"});
        System.out.println("\nChecking existing usb drivers");
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "lsusb -nn"});

        System.out.println("\nClean");
        String cmd = NFAST_HOME + "/sbin/drivers/clean";
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", cmd});

        System.out.println("Configure");
        String cmd1 = NFAST_HOME + "/sbin/drivers/configure";
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", cmd1});

        System.out.println("Compile");
        String cmd2 = NFAST_HOME + "sbin/drivers/compile";
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", cmd2});

        System.out.println("Make");
        String cmd3 = NFAST_HOME + "sbin/drivers/make";
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", cmd3});

        System.out.println("Install");
        String cmd4 = NFAST_HOME + "sbin/drivers/install";
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", cmd4});

        System.out.println("\nRestarting NFAST Service...");
        String cmd5 = NFAST_HOME + "bin/init-d restart";
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", cmd5});

        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "NFAST_HOME/bin/enquiry"});
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "NFAST_HOME/bin/nfkminfo"});

    }
}
