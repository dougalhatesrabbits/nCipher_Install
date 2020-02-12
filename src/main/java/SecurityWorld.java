import com.log.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;

public class SecurityWorld {
    // Always use the classname, this way you can refactor
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    //private final static Logger LOGGER = Logger.getLogger(Install.class.getName());
    /***
     * This is a parent
     */
    // Class attributes
    String sw_filename;
    short    sw_version;
    String sw_location;
    final String NFAST_HOME = null;
    final String NFAST_KMDATA = null;
    final String CLASSPATH = null;
    final String PATH = null;
    final String JAVA_PATH = null;



    // Constructor
    public SecurityWorld(String name, short version, String location){
        sw_filename = name;
        sw_version = version;
        sw_location = location; //basename
    }



    // Methods
    void applyFirmware(){
        LOGGER.fine("New instance of -ApplyFirmware- started");
        System.out.println("Installing firmware");


    }

    void applyHotFix(){
        LOGGER.fine("New instance of -ApplyHotFix- started");
        System.out.println("Installing hotfix");

    }

    void applySecurityWorld(){
        LOGGER.fine("New instance of -ApplySecurityWorld- started");
        System.out.println("Installing Security World");
    }

    void unpackSecurityWorld(){
        LOGGER.fine("New instance of -UnpackSecurityWorld- started");
        System.out.println("Unpacking Security World...");
    }

    void checkEnvironmentVariables(){
        LOGGER.fine("New instance of -check_Environment_Variables- started");
        System.out.println("Checking environment variables");
    }

    void checkJava(){
        LOGGER.fine("New instance of -check_Java- started");
        System.out.println("Checking Java");
    }



    public void check_Existing_SW(){
        LOGGER.fine("New instance of -check_Existing_SW- started");
        System.out.println("Checking for existing Security World");
        SecurityWorldLinux linux = new SecurityWorldLinux("a", (short) 123, "c");
        Path path = Paths.get(linux.NFAST_HOME);
        System.out.println(path);

        if (Files.exists(path)) {
            System.out.println("Found SW, removing previous install: confirm (Y) to proceed, (N) to abort installation");
            Scanner myObj = new Scanner(System.in);  // Create a Scanner object


            String confirm = myObj.nextLine();  // Read user input
            if (confirm.toLowerCase() == "y"){
                remove_Existing_SW();// Output user input
            } else {
                System.exit(1);
            }
        } else {
            remove_Existing_SW();
        }

    }

    public void remove_Existing_SW(){
        LOGGER.fine("New instance of -remove_Existing_SW- started");
        System.out.println("Removing old Security World");
    }
}
