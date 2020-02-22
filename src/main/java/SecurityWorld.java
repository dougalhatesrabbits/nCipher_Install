import com.platform.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SecurityWorld {
    // Always use the classname, this way you can refactor
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    /***
     * This is a parent
     */
    // Class attributes
    Path path = null;

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
        System.out.println("\nInstalling Security World");
        if (osx.isWindows()){
            String cmd = windows.NFAST_HOME + "/sbin/install";
            try {
                new RunProcBuilder().run(new String[]{"cmd", "-c", cmd});
                System.out.println(cmd);
                LOGGER.info(cmd);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.logp(Level.WARNING,
                        "SecurityWorld",
                        "removeExistingSW",
                        "Cannot uninstall", e.fillInStackTrace());
            }
        } else {
            String cmd =linux.NFAST_HOME + "/sbin/install";
            try {
                new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "sudo " + cmd});
                System.out.println(cmd);
                LOGGER.info(cmd);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.logp(Level.WARNING,
                        "SecurityWorld",
                        "removeExistingSW",
                        "Cannot uninstall", e.fillInStackTrace());
            }
        }

        // check for root user
        Process p = Runtime.getRuntime().exec("id -u");
        //p.info();
        // TODO
    }

    void checkEnvVariables() {
        LOGGER.fine("running -checkEnvironmentVariables- method");
        System.out.println("\nChecking environment variables");
        LOGGER.info("Checking environment variables");
        System.out.println("NFAST_HOME= " +System.getenv("NFAST_HOME"));
        System.out.println("PATH= " +System.getenv("PATH"));
        System.out.println("NFAST_KMDATA= " +System.getenv("NFAST_KMDATA"));
        System.out.println("CLASSPATH= " +System.getenv("CLASSPATH"));

        System.out.println();
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
        System.out.println("\nChecking Java");
        System.out.println("$JAVA_HOME= " +System.getenv("JAVA_HOME"));
        System.out.println("$JAVA_PATH= " +System.getenv("JAVA_PATH"));
        // TODO
    }

    public void checkExistingSW(Platform osx) throws IOException {
        LOGGER.fine("running -checkExistingSW- method");
        Linux linux = new Linux();
        Windows windows = new Windows();

        if (osx.isWindows()){
            path = Paths.get(windows.NFAST_HOME);
        } else {
            path = Paths.get(linux.NFAST_HOME);
        }
        System.out.println("\nChecking for existing Security World in $NFAST_HOME: " +path);

        if (Files.exists(path)) {
            System.out.println("Found SW, removing previous install: \nConfirm (Y) to proceed, (N) to abort installation >");
            Scanner myObj = new Scanner(System.in);  // Create a Scanner object
            String confirm = myObj.nextLine();  // Read user input

            if (confirm.equalsIgnoreCase("y")) {
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
        System.out.println("\nRemoving old Security World");
        if (osx.isWindows()){
            String cmd = windows.NFAST_HOME + "/sbin/install -d";
            try {
                new RunProcBuilder().run(new String[]{"cmd", "-c", cmd});
                System.out.println("Using NFAST " +cmd);
                LOGGER.info("Using NFAST " +cmd);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.logp(Level.WARNING,
                        "SecurityWorld",
                        "removeExistingSW",
                        "Cannot uninstall", e.fillInStackTrace());
            }
        } else {
            String cmd =linux.NFAST_HOME + "/sbin/install -d";
            try {
                new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "sudo " + cmd});
                System.out.println("Using NFAST " +cmd);
                LOGGER.info("Using NFAST " +cmd);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.logp(Level.WARNING,
                        "SecurityWorld",
                        "removeExistingSW",
                        "Cannot uninstall", e.fillInStackTrace());
            }
        }
    }
}
