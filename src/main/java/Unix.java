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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Unix extends SecurityWorld {
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
    String sw_location = "/mnt/iso/";
    // List of source iso filepaths i.e /home/myiso.iso
    ArrayList<Path> sw_files = new ArrayList<Path>();
    ArrayList<Path> tar_files = new ArrayList<Path>();
    File sw_iso = null;
    Path iso_File = null;

    final String NFAST_HOME = "/opt/nfast";
    final String NFAST_KMDATA = "$NFAST_HOME/kmdata";
    final String CLASSPATH = null;
    final String PATH = "$NFAST_HOME/bin:$NFAST_HOME/sbin";
    final String JAVA_PATH = null;

    // Path to input file, which is a
    // tar file compressed to create gzip file
    // String INPUT_FILE = "linux.tar.gz";
    // This folder should exist, that's where
    // .tar file will go
    String TAR_FOLDER = "/tmp";
    // After untar files will go to this folder
    // String DESTINATION_FOLDER = "mnt";

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

        String cmd = "sudo " + NFAST_HOME + "/sbin/install -u";
        //String cmd = "sudo " + NFAST_HOME + "rm -rf linux";
        try {
            new RunProcBuilder().run(new String[]{cmd});
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




}
