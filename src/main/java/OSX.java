import com.file.Find;
import com.file.ReadIso;
import com.file.UnTarFile;
import com.platform.ConsoleColours;
import com.platform.RunProcBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.tools.ant.DirectoryScanner;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OSX extends SecurityWorld {
    // Always use the classname, this way you can refactor
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    /***
     * Child class
     *
     * @param name
     * @param version
     */

    //String sw_filename = "SecWorld-linux64-user-12.60.3.iso";
    Path sw_filename = null;
    String sw_version = "126030";
    String sw_location = "mnt/iso/";
    // List of source iso filepaths i.e /home/myiso.iso
    ArrayList<Path> sw_files = new ArrayList<Path>();
    ArrayList<Path> tar_files = new ArrayList<Path>();
    File sw_iso = null;
    Path iso_File = null;

    final String NFAST_HOME = "opt/nfast";
    final String NFAST_KMDATA = "$NFAST_HOME/kmdata";
    final String CLASSPATH = null;
    final String PATH = "$NFAST_HOME/bin:$NFAST_HOME/sbin";
    final String JAVA_PATH = null;

    // Path to input file, which is a
    // tar file compressed to create gzip file
    // String INPUT_FILE = "linux.tar.gz";
    // This folder should exist, that's where
    // .tar file will go
    String TAR_FOLDER = "tmp";
    // After untar files will go to this folder
    // String DESTINATION_FOLDER = "mnt";

    public Path getIsoChoices(){
        int i = 1;
        System.out.println();
        for (Path file :  sw_files) {
            System.out.println("[" + i + "] " + file);
            i=i+1;
        }

        System.out.println(ConsoleColours.YELLOW+"Enter choice: "+ConsoleColours.RESET);
        LOGGER.info("Enter choice: ");
        Scanner sw = new Scanner(System.in);  // Create a Scanner object
        int confirm = sw.nextInt();
        iso_File =  sw_files.get(confirm-1);
        System.out.println("You entered " + "[" + confirm + "]" + ConsoleColours.YELLOW +iso_File+ConsoleColours.RESET);
        LOGGER.info("You entered " + iso_File);

        return iso_File;
    }

    void checkMount(Path sw_filename) throws IOException {
        LOGGER.fine("running -checkMount- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED+"\nChecking if ISO is already mounted"+ConsoleColours.RESET);
        LOGGER.info("Checking if ISO is already mounted");
        File isoFile = null;
        //File isoFile = new File(sw_location + "/" + sw_filename);

        System.out.println("check: " + sw_filename);
        LOGGER.info("check: " + sw_filename);

        if ( sw_filename !=null) {
             isoFile = new File( sw_filename.toString());
        } else {
            System.out.println("Cannot find iso");
            //isoFile = new File(sw_location + "/" + sw_filename);
        }


        // Check if source file exists on mnt
        String path = "";
        boolean bool = false;
        File file2 = null;
        File file = null;
        try {
            // creating new files
            file  = new File(sw_location,"linux");
            //file.createNewFile();
            System.out.println(file);
            LOGGER.info((file.toString()));
            // creating new canonical from file object
            file2 = file.getCanonicalFile();
            // returns true if the file exists
            System.out.println(file2);
            LOGGER.info((file2.toString()));
            bool = file2.exists();
            // returns absolute pathname
            path = file2.getAbsolutePath();
            System.out.println(bool);
            // if file exists
            if (bool) {
                // prints
                System.out.print(path + " Exists? " + bool);
                LOGGER.info(path + " Exists? " + bool);
            }
        } catch (Exception e) {
            // if any error occurs
            e.printStackTrace();
        }
        if(bool){

            try {
            if (file2.isDirectory()){
                FileUtils.deleteDirectory(file2);
            } else if (file.delete()){

                    System.out.println(file.getName() + " deleted");//getting and printing the file name
                    LOGGER.info(file.getName() + " deleted");
            } else {
                    System.out.println("failed");
                    LOGGER.info("failed");
                }
            } catch(Exception e)
            {
                e.printStackTrace();
            }



        }
        // TODO Do checksum on iso
        //new ReadIso(new File(sw_filename), destFile);
        new ReadIso(isoFile, new File(sw_location));

        System.out.println("\nMounted " + sw_filename + " at /" + sw_location);


    }

    void checkUsers() throws IOException {
        LOGGER.fine("running -checkUsers- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED+"\nChecking if Users are in correct Groups"+ConsoleColours.RESET);
        LOGGER.info("Checking if Users are in correct Groups");
        //Process p = Runtime.getRuntime().exec("////command////");
        //Process p = Runtime.getRuntime().exec("pwd");
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "pwd"});
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "ls -l"});
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "source .bash_profile"});

        // TODO add users if not exist

    }

    void getTars(){
        // https://ant.apache.org/manual/api/org/apache/tools/ant/DirectoryScanner.html
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setIncludes(new String[]{"**/*tar*"});
        scanner.setBasedir(sw_location + "/linux");
        scanner.setCaseSensitive(false);
        scanner.scan();
        String[] files = scanner.getIncludedFiles();
        //ArrayList<Path> swfiles = new ArrayList<Path>();
        //String found = null;
        for (String file : files) {
            String found = scanner.getBasedir() + "/" + file;
            Path path = Paths.get(found);


            tar_files.add(path);
            System.out.println(path);
            LOGGER.info(path.toString());
            unpackSecWorld(found, NFAST_HOME);
        }
    }

    void unpackSecWorld(String tar, String dest) {
        LOGGER.fine("running -UnpackSecurityWorld- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED+"Unpacking Security World..." +ConsoleColours.RESET);
        LOGGER.info("Unpacking Security World...");

        String ext = FilenameUtils.getExtension(tar);
        if (ext.equals("tgz")) {
            System.out.println("We have a Gunzip compressed tarball " + ext);
            LOGGER.info("We have a Gunzip compressed tarball " + ext);
            UnTarFile untar = new UnTarFile();
            try {
                File inputFile = new File(tar);
                String outputFile = untar.getFileName(inputFile, TAR_FOLDER);
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
                        "OSX",
                        "unpackSecurityWorld",
                        "Cannot unpack", e.getCause());
            }
        } else if (ext.equals("zip")) {
            System.out.println("We have a standard zip file " + ext);
            LOGGER.info("We have a standard zip file " + ext);
            UnTarFile untar = new UnTarFile();
            try {
                String inputFile = new String(tar);
                File destFile = new File(dest);
                untar.unzip(inputFile, destFile);
            } catch (IOException e) {
                LOGGER.logp(Level.WARNING,
                        "OSX",
                        "unpackSecurityWorld",
                        "Cannot unpack", e.fillInStackTrace());
                LOGGER.logp(Level.WARNING,
                        "OSX",
                        "unpackSecurityWorld",
                        "Cannot unpack", e.getCause());
            }
        } else {
            System.out.println("We have a standard compressed tarball " + ext);
            LOGGER.info("We have a standard compressed tarball " + ext);
            UnTarFile untar = new UnTarFile();
            try {
                File inputFile = new File(tar);
                File destFile = new File(dest);
                untar.unTarFile(inputFile, destFile);
            } catch (IOException e) {
                LOGGER.logp(Level.WARNING,
                        "OSX",
                        "unpackSecurityWorld",
                        "Cannot unpack", e.fillInStackTrace());
                LOGGER.logp(Level.WARNING,
                        "OSX",
                        "unpackSecurityWorld",
                        "Cannot unpack", e.getCause());
            }
        }
    }

    void getSecWorld() throws IOException {
        LOGGER.fine("running -getSecWorld- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nGetting Security World" +ConsoleColours.RESET);
        LOGGER.info("Getting Security World");

        Find.Finder iso =  new Find.Finder("*linux*.iso");
        //String ext = FilenameUtils.getExtension(tar);
        //Find.main(new String[]{"/Users/david/IdeaProjects/nCipher_Install", "-name", "*test"});
        Find.main(new String[]{"/Users/david/IdeaProjects/nCipher_Install", "-name", "*linux*.iso"});
        //isoList = iso.getResult();

        File[] dirs = new File("/Users/david/IdeaProjects/nCipher_Install").listFiles((FileFilter) new WildcardFileFilter("*linux*.iso"));
        for (File dir : dirs) {
            System.out.println(dir);
            LOGGER.info((Supplier<String>) dir);
            if (dir.isDirectory()) {
                File[] files = dir.listFiles((FileFilter) new WildcardFileFilter("*linux*.iso"));
            }
        }
        // https://ant.apache.org/manual/api/org/apache/tools/ant/DirectoryScanner.html
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setIncludes(new String[]{"**/*linux*.iso"});
        scanner.setBasedir("/Users/david/IdeaProjects/nCipher_Install");
        scanner.setCaseSensitive(false);
        scanner.scan();
        String[] files = scanner.getIncludedFiles();
        //ArrayList<Path> swfiles = new ArrayList<Path>();
        //String found = null;
        for (String file : files) {
            String found = scanner.getBasedir() + "/" + file;
            Path path = Paths.get(found);
            sw_files.add(path);
            System.out.println(path);
            LOGGER.info(path.toString());
        }
    }

    public Path getSecWorld(String sw) throws IOException {
        LOGGER.fine("running -getSecWorld- overloaded");
        System.out.println("Getting Security world overloaded: " +sw);
        LOGGER.info("Getting Security world overloaded: " +sw);

        Find.Finder iso =  new Find.Finder(sw);
        //String ext = FilenameUtils.getExtension(tar);
        //Find.main(new String[]{"/Users/david/IdeaProjects/nCipher_Install", "-name", "*test"});
        Find.main(new String[]{"/Users/david/IdeaProjects/nCipher_Install", "-name", sw});
        //isoList = iso.getResult();

        File[] dirs = new File("/Users/david/IdeaProjects/nCipher_Install").listFiles((FileFilter) new WildcardFileFilter(sw));
        for (File dir : dirs) {
            System.out.println(dir);
            LOGGER.info((Supplier<String>) dir);
            if (dir.isDirectory()) {
                File[] files = dir.listFiles((FileFilter) new WildcardFileFilter(sw));
            }
        }
        // https://ant.apache.org/manual/api/org/apache/tools/ant/DirectoryScanner.html
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setIncludes(new String[]{"**/" + sw});
        scanner.setBasedir("/Users/david/IdeaProjects/nCipher_Install");
        scanner.setCaseSensitive(false);
        scanner.scan();
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

    void applyDrivers(){

    }
}
