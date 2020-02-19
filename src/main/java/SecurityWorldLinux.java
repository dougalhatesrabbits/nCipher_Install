import com.file.ReadIso;
import com.file.*;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.platform.MyMultipleCommandsEx;
import com.platform.RunProcessBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class SecurityWorldLinux extends SecurityWorld {
    // Always use the classname, this way you can refactor
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    /***
     * Child class
     *
     * @param name
     * @param version
     */

    String sw_filename = "Clive.iso";
    short  sw_version = 12603;
    String sw_location = "mnt/iso/";

    final String NFAST_HOME = "/opt/nfast/";
    final String NFAST_KMDATA = "/opt/nfast/kmdata";
    final String CLASSPATH = null;
    final String PATH = "opt/nfast/bin";
    final String JAVA_PATH = null;

    // Path to input file, which is a
    // tar file compressed to create gzip file
    String INPUT_FILE = "linux.tar.gz";
    // This folder should exist, that's where
    // .tar file will go
    String TAR_FOLDER = "tmp";
    // After untar files will go to this folder
    String DESTINATION_FOLDER = "mnt";

    // Default constructor
    public SecurityWorldLinux(String name, short version, String location) {
        super(name, version, location);
    }



    void check_Mount() {
        LOGGER.info("New instance of *check_Mount* started");
        System.out.println("Checking if ISO is mounted");
        File destFile = new File(sw_location);
        try {
            if(destFile.exists()){
                FileUtils.deleteDirectory(destFile);
            }
        } catch (FileAlreadyExistsException ex){
            LOGGER.logp(Level.WARNING, "SecurityWorldLinux", "checkMount", "Cannot mount", ex);
            ex.printStackTrace();
        } catch (IOException e) {
            LOGGER.logp(Level.WARNING, "SecurityWorldLinux", "checkMount", "Cannot mount", e);
            e.printStackTrace();
        }


        // TODO Do checksum on iso
        ReadIso tempIso = new ReadIso(new File(sw_filename), destFile);
        System.out.println("Mounted " + sw_filename + " at /" + sw_location);
    }

    void check_Users() throws IOException {
        LOGGER.fine("New instance of -check_Users- started");
        System.out.println("Checking if Users are in correct Groups");
        //Process p = Runtime.getRuntime().exec("////command////");
        //Process p = Runtime.getRuntime().exec("pwd");
        new RunProcessBuilder().run(new String[]{"/bin/bash", "-c", "pwd"});
        new RunProcessBuilder().run(new String[]{"/bin/bash", "-c", "ls -l"});
        new RunProcessBuilder().run(new String[]{"/bin/bash", "-c", "source .bash_profile"});

        // TODO

    }


    void unpackSecurityWorld(String tar, String dest) {
        LOGGER.fine("New instance of -UnpackSecurityWorld- started");
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
                LOGGER.logp(Level.WARNING, "SecurityWorldLinux", "unpackSecurityWorld", "Cannot unpack", e.getCause());
                e.printStackTrace();
            }
        } else if (ext.equals("zip")) {
            System.out.println("We have a standard zip file " + ext);
            UnTarFile untar = new UnTarFile();
            try {
                String inputFile = new String(tar);
                File destFile = new File(dest);
                untar.unzip(inputFile, destFile);
            } catch (IOException e) {
                LOGGER.logp(Level.WARNING, "SecurityWorldLinux", "unpackSecurityWorld", "Cannot unpack", e.fillInStackTrace());
                LOGGER.logp(Level.WARNING, "SecurityWorldLinux", "unpackSecurityWorld", "Cannot unpack", e.getCause());
                e.printStackTrace();
            }
        } else {
            System.out.println("We have a standard compressed tarball " + ext);
            UnTarFile untar = new UnTarFile();
            try {
                File inputFile = new File(tar);
                File destFile = new File(dest);
                untar.unTarFile(inputFile, destFile);
            } catch (IOException e) {
                LOGGER.logp(Level.WARNING, "SecurityWorldLinux", "unpackSecurityWorld", "Cannot unpack", e.fillInStackTrace());
                LOGGER.logp(Level.WARNING, "SecurityWorldLinux", "unpackSecurityWorld", "Cannot unpack", e.getCause());
                e.printStackTrace();


            }

        }





    }





}
