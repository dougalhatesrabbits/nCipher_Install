import com.platform.*;
import org.apache.commons.io.IOUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
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
        // TODO apply fw
    }

    void applyHotFix(){
        LOGGER.fine("running -applyHotFix- method");
        System.out.println("Installing hotfix");
        // TODO apply hotfix
    }

    void applySecWorld(Platform os, Linux linux, Windows windows, OSX mac) throws IOException {
        LOGGER.fine("running -applySecurityWorld- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nInstalling Security World" +ConsoleColours.RESET);
        if (os.isWindows()){
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
        // TODO check for root user
    }

    void checkEnvVariables() {
        LOGGER.fine("running -checkEnvironmentVariables- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nChecking environment variables" + ConsoleColours.RESET);
        LOGGER.info("Checking environment variables");

        Linux linux = new Linux();

        System.out.println(ConsoleColours.YELLOW + "NFAST_HOME= " + ConsoleColours.RESET +System.getenv("NFAST_HOME"));
        System.out.println(ConsoleColours.YELLOW + "PATH= " + ConsoleColours.RESET +System.getenv("PATH"));
        System.out.println(ConsoleColours.YELLOW + "NFAST_KMDATA= " + ConsoleColours.RESET +System.getenv("NFAST_KMDATA"));

        //HashMap<String, String> envVars = new HashMap<String, String>();

        System.out.println();
        Map<String, String> envVars = System.getenv();
        for (String envName : envVars.keySet()) {
            System.out.format("%s=%s%n",
                    envName,
                    envVars.get(envName));
            switch (envName) {
                case "NFAST_HOME":
                    if (envVars.get(envName).contains(linux.NFAST_HOME)){
                        System.out.println(ConsoleColours.GREEN + "[OK]" +
                                ConsoleColours.RESET);
                    } else {
                        System.out.println(ConsoleColours.RED + "[NOK]" +
                                ConsoleColours.RESET);
                    }
                    //todo validate NFAST_HOME
                    break;
                case "NFAST_KMDATA" :
                    if (envVars.get(envName).contains(linux.NFAST_KMDATA)){
                        System.out.println(ConsoleColours.GREEN + "[OK]" +
                                ConsoleColours.RESET);
                    } else {
                        System.out.println(ConsoleColours.RED + "[NOK]" +
                                ConsoleColours.RESET);
                    }
                    //todo validate NFAST_KMDATA
                    break;
                case "PATH" :
                    if (envVars.get(envName).contains(linux.PATH)){
                        System.out.println(ConsoleColours.GREEN + "[OK]" +
                                ConsoleColours.RESET);
                    } else {
                        System.out.println(ConsoleColours.RED + "[NOK]" +
                                ConsoleColours.RESET);
                    }
                    //todo validate PATH
                    break;
                //default:
                    //System.out.println("Cannot find NFAST Variables");
            }
        }

        Properties envProps = System.getProperties();
        for (Object propName : envProps.keySet()) {
            System.out.format("%s=%s%n",
                    propName,
                    envProps.get(propName));
        }
    }

    void checkJava() throws IOException {
        LOGGER.fine("running -checkJava- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nChecking Java" + ConsoleColours.RESET);
        LOGGER.info("Checking Java environment variables");

        System.out.println(ConsoleColours.YELLOW + "$JAVA_HOME= " + ConsoleColours.RESET +System.getenv("JAVA_HOME"));
        System.out.println(ConsoleColours.YELLOW + "$JAVA_PATH= " + ConsoleColours.RESET +System.getenv("JAVA_PATH"));
        System.out.println(ConsoleColours.YELLOW + "$CLASSPATH= " + ConsoleColours.RESET +System.getenv("CLASSPATH"));

        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "java --version"}); // this may be a JRE (not reqd)
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "$JAVA_HOME/bin/javac -version"}); // This is what we want a JDK
        System.out.println(System.getProperty("java.version"));
        System.out.println(System.getProperty("java.runtime.version"));
        System.out.println(System.getProperty("java.home"));
        System.out.println(System.getProperty("java.vendor"));
        System.out.println(System.getProperty("java.vendor.url"));
        System.out.println(System.getProperty("java.class.path"));
        // TODO validate java logic
    }

    void installJava() throws IOException {
        LOGGER.fine("running -installJava- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nInstalling Java" + ConsoleColours.RESET);
        LOGGER.info("Installing Java");

        String home = System.getProperty("user.home");
        /*
        try {
            IOUtils.copy(
                    new URL("https://www.oracle.com/java/technologies/javase-jdk13-doc-downloads.html#license-lightbox").openStream(),
                    new FileOutputStream(home + "/Documents/oracle")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

         */
        // JDK 9
        /***
         * https://www.tecmint.com/install-java-jdk-jre-in-linux/
         */
        try {
            System.out.println("sudo cd /opt/java");
            new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "sudo cd /opt/java"});

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.println("wget");
            String cmd = "sudo wget --no-cookies --no-check-certificate --header \"Cookie: oraclelicense=accept-securebackup-cookie\" http://download.oracle.com/otn-pub/java/jdk/9.0.4+11/c2514751926b4512b076cc82f959763f/jdk-9.0.4_linux-x64_bin.tar.gz";
            new RunProcBuilder().run(new String[]{"/bin/bash", "-c", cmd});
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "sudo tar -zxvf jdk-9.0.4_linux-x64_bin.tar.gz"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "sudo cd jdk-9.0.4/"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "sudo update-alternatives --install /usr/bin/java java /opt/java/jdk-9.0.4/bin/java 100"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "sudo update-alternatives --config java"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "sudo update-alternatives --install /usr/bin/javac javac /opt/java/jdk-9.0.4/bin/javac 100"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "sudo update-alternatives --config javac"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "sudo update-alternatives --install /usr/bin/jar jar /opt/java/jdk-9.0.4/bin/jar 100"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "sudo update-alternatives --config jar"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        //TODO validate java install logic
    }

    void configureJava() throws IOException {
        // JDK 9
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "export JAVA_HOME=/opt/java/jdk-9.0.4/"});
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "export JRE_HOME=/opt/java/jdk-9.0.4/jre"});
        new RunProcBuilder().run(new String[]{"/bin/bash", "-c", "export PATH=$PATH:/opt/java/jdk-9.0.4/bin:/opt/java/jdk-9.0.4/jre/bin"});
        //TODO validate java config logic

    }

    public void checkExistingSW(Platform osx) throws IOException {
        LOGGER.fine("running -checkExistingSW- method");
        Linux linux = new Linux();
        Windows windows = new Windows();
        OSX mac = new OSX();

        if (osx.isWindows()){
            path = Paths.get(windows.NFAST_HOME);
        } else {
            path = Paths.get(linux.NFAST_HOME);
        }
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nChecking for existing Security World in $NFAST_HOME: " +path +ConsoleColours.RESET);

        if (Files.exists(path)) {
            System.out.println("Found SW, removing previous install: \nConfirm (Y) to proceed, (N) to abort installation >");
            Scanner myObj = new Scanner(System.in);  // Create a Scanner object
            String confirm = myObj.nextLine();  // Read user input

            if (confirm.equalsIgnoreCase("y")) {
                System.out.println("You entered proceed " + confirm);
                removeExistingSW(osx, linux, windows, mac);// Output user input
            } else {
                System.out.println("You entered stop " + confirm);
                System.exit(1);
            }
        } else {
            System.out.println("No existing SW found, proceeding with install");
        }
    }

    public void removeExistingSW(Platform osx, Linux linux, Windows windows, OSX mac) throws IOException {
        LOGGER.fine("running removeExistingSW method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED +"\nRemoving old Security World"+ConsoleColours.RESET);
        if (osx.isWindows() ){
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
