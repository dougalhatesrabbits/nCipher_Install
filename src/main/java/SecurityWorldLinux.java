import com.platform.Platform;

import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Logger;

public class SecurityWorldLinux extends SecurityWorld {
    // Always use the classname, this way you can refactor
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    /***
     * Child class
     *
     * @param name
     * @param version
     */

    String sw_filename;
    short    sw_version;
    String sw_location;
    final String NFAST_HOME = "/opt/nfast/";
    final String NFAST_KMDATA = "/opt/nfast/kmdata";
    final String CLASSPATH = null;
    final String PATH = "opt/nfast/bin";
    final String JAVA_PATH = null;



    // Default constructor
    public SecurityWorldLinux(String name, short version, String location) {
        super(name, version, location);
    }



    void check_Mount(){
        LOGGER.info("New instance of *check_Mount* started");
        System.out.println("Checking if ISO is mounted");
    }

    void check_Users(){
        LOGGER.fine("New instance of -check_Users- started");
        System.out.println("Checking if Users are in correct Groups");
    }

    public void check_Existing_SW(){
        LOGGER.fine("New instance of -check_Existing_SW- started");
        System.out.println("Checking for existing Security World");
        SecurityWorldLinux linux = new SecurityWorldLinux("a", (short) 123, "c");
        Path path = Paths.get(linux.NFAST_HOME);
        System.out.println(path);

        if (Files.exists(path)) {
            System.out.println("Found SW, removing previous Linux install: \nconfirm (Y) to proceed, (N) to abort installation >");
            Scanner myObj = new Scanner(System.in);  // Create a Scanner object


            String confirm = myObj.nextLine();  // Read user input
            if (confirm.toLowerCase().equals("y")){
                System.out.println("You entered proceed " + confirm);
                remove_Existing_SW();// Output user input
            } else {
                System.out.println("You entered stop " + confirm);
                System.exit(1);
            }
        } else {
            System.out.println("No linux SW aha");

        }


    }

    public void remove_Existing_SW(){
        LOGGER.fine("New instance of -remove_Existing_SW- started");
        System.out.println("Removing old Linux Security World");
    }

}
