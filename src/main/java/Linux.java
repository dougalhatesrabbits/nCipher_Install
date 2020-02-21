import com.file.ReadIso;
import com.file.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.platform.RunProcBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.tools.ant.DirectoryScanner;

import javax.tools.FileObject;

public class Linux extends SecurityWorld {
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
    ArrayList<Path>  sw_files = new ArrayList<Path>();
    File sw_iso = null;
    Path iso_File = null;

    final String NFAST_HOME = "/opt/nfast/";
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

    // Default constructor
    public Linux(String name, String version, String location) {
        super(name, version, location);
    }

    public Path getIsoChoices(){


        int i = 1;

        for (Path file :  sw_files) {
            System.out.println("[" + i + "] " + file);
            i=i+1;
        }

        System.out.println("Enter choice: ");
        Scanner sw = new Scanner(System.in);  // Create a Scanner object
        int confirm = sw.nextInt();
        System.out.println("You entered " + confirm);
        iso_File =  sw_files.get(confirm-1);
        System.out.println("You entered " + iso_File);

        return iso_File;
    }

    void checkMount(Path sw_filename) throws IOException {
        LOGGER.fine("running -checkMount- method");
        System.out.println("Checking if ISO is mounted");
        File isoFile = null;
        //File isoFile = new File(sw_location + "/" + sw_filename);

        System.out.println(sw_filename);

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
            // creating new canonical from file object
            file2 = file.getCanonicalFile();
            // returns true if the file exists
            System.out.println(file2);
            bool = file2.exists();
            // returns absolute pathname
            path = file2.getAbsolutePath();
            System.out.println(bool);
            // if file exists
            if (bool) {
                // prints
                System.out.print(path + " Exists? " + bool);
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

                    System.out.println(file.getName() + " deleted");   //getting and printing the file name
            } else {
                    System.out.println("failed");
                }
            } catch(Exception e)
            {
                e.printStackTrace();
            }



        }
        // TODO Do checksum on iso
        //new ReadIso(new File(sw_filename), destFile);
        new ReadIso(isoFile, new File(sw_location));

        System.out.println("Mounted " + sw_filename + " at /" + sw_location);


    }

    void checkUsers() throws IOException {
        LOGGER.fine("running -checkUsers- method");
        System.out.println("Checking if Users are in correct Groups");
        //Process p = Runtime.getRuntime().exec("////command////");
        //Process p = Runtime.getRuntime().exec("pwd");
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "pwd"});
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "ls -l"});
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "source .bash_profile"});

        // TODO

    }

    void unpackSecWorld(String tar, String dest) {
        LOGGER.fine("running -UnpackSecurityWorld- method");
        System.out.println("Unpacking Security World...");

        String ext = FilenameUtils.getExtension(tar);
        if (ext.equals("tgz")) {
            System.out.println("We have a Gunzip compressed tarball " + ext);
            UnTarFile untar = new UnTarFile();
            try {
                File inputFile = new File(tar);
                String outputFile = untar.getFileName(inputFile, TAR_FOLDER);
                System.out.println("outputFile " + outputFile);
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
        } else if (ext.equals("zip")) {
            System.out.println("We have a standard zip file " + ext);
            UnTarFile untar = new UnTarFile();
            try {
                String inputFile = new String(tar);
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
            System.out.println("We have a standard compressed tarball " + ext);
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

    void getSecWorld() throws IOException {

        Find.Finder iso =  new Find.Finder("*linux*.iso");
        //String ext = FilenameUtils.getExtension(tar);
        //Find.main(new String[]{"/Users/david/IdeaProjects/nCipher_Install", "-name", "*test"});
        Find.main(new String[]{"/Users/david/IdeaProjects/nCipher_Install", "-name", "*linux*.iso"});
        //isoList = iso.getResult();

        File[] dirs = new File("/Users/david/IdeaProjects/nCipher_Install").listFiles((FileFilter) new WildcardFileFilter("*linux*.iso"));
        for (File dir : dirs) {
            System.out.println(dir);
            if (dir.isDirectory()) {
                File[] files = dir.listFiles((FileFilter) new WildcardFileFilter("sample*.java"));
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
        }
    }
}
