import com.platform.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

public class SecurityWorld {
    // Always use the classname, this way you can refactor
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    //private final static Logger LOGGER = Logger.getLogger(Install.class.getName());
    /***
     * This is a parent
     */
    // Class attributes
    String sw_filename;
    short  sw_version;
    String sw_location;
    final String NFAST_HOME = null;
    final String NFAST_KMDATA = null;
    final String CLASSPATH = null;
    final String PATH = null;
    final String JAVA_PATH = null;
    Path path = null;



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
        // TODO
    }

    void applyHotFix(){
        LOGGER.fine("New instance of -ApplyHotFix- started");
        System.out.println("Installing hotfix");
        // TODO
    }

    void applySecurityWorld(Platform osx, SecurityWorldLinux linux, SecurityWorldWindows windows) throws IOException {
        LOGGER.fine("New instance of -ApplySecurityWorld- started");
        System.out.println("Installing Security World");
        if (osx.isWindows()){
            String cmd = windows.NFAST_HOME + "/sbin/install";
            System.out.println(cmd);
            // TODO
        } else {
            String cmd =linux.NFAST_HOME + "sbin/install";
            System.out.println(cmd);
            // TODO
        }
        // TODO
        // check for root user
        Process p = Runtime.getRuntime().exec("id -u");
        p.info();

    }

    void checkEnvironmentVariables() throws IOException {
        LOGGER.fine("New instance of -check_Environment_Variables- started");
        System.out.println("Checking environment variables");
        System.out.println(System.getenv("NFAST_HOME"));
        System.out.println(System.getenv("PATH"));

        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            System.out.format("%s=%s%n",
                    envName,
                    env.get(envName));
        }

        // TODO
    }

    void checkJava(){
        LOGGER.fine("New instance of -check_Java- started");
        System.out.println("Checking Java");
        System.out.println(System.getenv("JAVA_HOME"));
        System.out.println(System.getenv("JAVA_PATH"));
        // TODO
    }

    public void check_Existing_SW(Platform osx){
        LOGGER.fine("New instance of -check_Existing_SW- started");
        System.out.println("Checking for existing Security World");

        SecurityWorldLinux linux = new SecurityWorldLinux("a", (short) 123, "c");
        SecurityWorldWindows windows = new SecurityWorldWindows("a", (short) 21, "b");

        if (osx.isWindows()){
            path = Paths.get(windows.NFAST_HOME);
        } else {
            path = Paths.get(linux.NFAST_HOME);
        }

        System.out.println(path);

        if (Files.exists(path)) {
            System.out.println("Found SW, removing previous install: \nconfirm (Y) to proceed, (N) to abort installation >");
            Scanner myObj = new Scanner(System.in);  // Create a Scanner object

            String confirm = myObj.nextLine();  // Read user input
            if (confirm.toLowerCase().equals("y")){
                System.out.println("You entered proceed " + confirm);
                remove_Existing_SW(osx, linux, windows);// Output user input
            } else {
                System.out.println("You entered stop " + confirm);
                System.exit(1);
            }
        } else {
            System.out.println("No SW aha");
        }
    }

    public void remove_Existing_SW(Platform osx, SecurityWorldLinux linux, SecurityWorldWindows windows){
        LOGGER.fine("New instance of -remove_Existing_SW- started");
        System.out.println("Removing old Security World");
        if (osx.isWindows()){
            String cmd = windows.NFAST_HOME + "/sbin/install -d";
            System.out.println(cmd);
            // TODO
        } else {
            String cmd =linux.NFAST_HOME + "sbin/install -d";
            System.out.println(cmd);
            // TODO
        }
    }
}
