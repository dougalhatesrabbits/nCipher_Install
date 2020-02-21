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
    /***
     * This is a parent
     */
    // Class attributes
    Path path = null;



    // Constructor
    public SecurityWorld(String name, String version, String location){
        String sw_filename = name;
        String sw_version = version;
        String sw_location = location; //basename
    }

    // Methods
    void applyFirmware(){
        LOGGER.fine("running -applyFirmware- method");
        System.out.println("Installing firmware");
        // TODO
    }

    void applyHotFix(){
        LOGGER.fine("running -applyHotFix- method");
        System.out.println("Installing hotfix");
        // TODO
    }

    void applySecWorld(Platform osx, Linux linux, Windows windows) throws IOException {
        LOGGER.fine("running -applySecurityWorld- method");
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

        // check for root user
        Process p = Runtime.getRuntime().exec("id -u");
        //p.info();
        // TODO

    }

    void checkEnvVariables() throws IOException {
        LOGGER.fine("running -checkEnvironmentVariables- method");
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
        LOGGER.fine("running -checkJava- method");
        System.out.println("Checking Java");
        System.out.println(System.getenv("JAVA_HOME"));
        System.out.println(System.getenv("JAVA_PATH"));
        // TODO
    }

    public void checkExistingSW(Platform osx) throws IOException {
        LOGGER.fine("running -checkExistingSW- method");
        System.out.println("Checking for existing Security World");

        Linux linux = new Linux("a", (String) "123", "c");
        Windows windows = new Windows("a", (String) "21", "b");

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
                removeExistingSW(osx, linux, windows);// Output user input
            } else {
                System.out.println("You entered stop " + confirm);
                System.exit(1);
            }
        } else {
            System.out.println("No existing SW found, proceeding with install");
        }
    }

    public void removeExistingSW(Platform osx, Linux linux, Windows windows) throws IOException {
        LOGGER.fine("running removeExistingSW method");
        System.out.println("Removing old Security World");
        if (osx.isWindows()){
            String cmd = windows.NFAST_HOME + "/sbin/install -d";
            new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "pwd"});
            System.out.println(cmd);
            // TODO
        } else {
            String cmd =linux.NFAST_HOME + "sbin/install -d";
            new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "sudo " + cmd});
            System.out.println(cmd);
            // TODO
        }
    }
}
