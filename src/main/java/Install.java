import com.log.*;
import com.platform.*;


import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Install {
    // Always use the classname, this way you can refactor
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    //private final static Logger LOGGER = Logger.getLogger(Install.class.getName());

    public static void main(String[] args) throws IOException {

        final ArgumentParser parser = ArgumentParsers.newFor("nCipher Install").build()
                .defaultHelp(true)
                .description("Installs and configures a Security World.");
        parser.addArgument("-l", "--level")
                .choices("debug", "info", "warning", "error").setDefault("info")
                .help("Specify logging level.");
        parser.addArgument("-t", "--type")
                .choices("install", "upgrade", "remove").setDefault("install")
                .help("Specify install type.");
        parser.addArgument("-v", "--version")
                .help("Version to install");
        parser.addArgument("-f", "--file")
                .help("Hotfix File to install");
        Namespace ns = null;

        try {
            ns = parser.parseArgs(args);

        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }
        String _level_ = null;
        try {
            _level_ = ns.getString("level");
        } catch (Exception e) {
            LOGGER.logp(Level.SEVERE, "Install", "main", "Could not get logging level", e);
            System.err.printf("Could not get logging level %s: %s",
                    ns.getString("level"), e.getMessage());
            System.exit(1);
        }

        InstallLogger log = new InstallLogger();
        log.setup(_level_);

        LOGGER.info("New instance of Install started");

        /*
        IOFile file = new IOFile();

        file.readFile("temp1.txt");
        file.writeFile("temp2.txt", "Hello darling");
        file.readBinaryFile("temp2.txt");
        file.writeBinaryFile("temp3.txt", " Lovely day");


        IOImage imagefile = new IOImage();
        imagefile.readImage("temp2.txt");
        imagefile.readImage("apple.jpg");

         */

        /* Get platform properties listing
        System.out.println(System.getProperty("os.name"));
        System.getProperties().list(System.out);
        */

        Platform osx = new Platform();
        //String os = osx.getOsName();
        //System.out.println("\n" + os);

        switch(osx.getOsName()) {
            case "windows":
                System.out.println("Windows OS");
                SecurityWorldWindows windows = new SecurityWorldWindows("a", (short) 12504, "c");
                windows.check_Existing_SW(osx);
                //windows.remove_Existing_SW(osx, null, windows);
                windows.checkJava();
                //windows.unpackSecurityWorld();
                windows.applySecurityWorld(osx, null, windows);
                windows.checkEnvironmentVariables();
                windows.applyFirmware();
                break;
            case "mac os x":

                System.out.println("MAC OS machine");
                SecurityWorldLinux linuxosx = new SecurityWorldLinux("a", (short) 12504, "c");

                //linux.check_Existing_SW(osx);
                /*linux.remove_Existing_SW(osx, linux, null);*/
                linuxosx.checkEnvironmentVariables();
                linuxosx.check_Mount();
                linuxosx.checkJava();
                linuxosx.unpackSecurityWorld("task.tgz", "mnt");
                //linux.unpackSecurityWorld("Archive.zip", "mnt");
                //linux.unpackSecurityWorld("linux.tar.gz", "mnt");
                linuxosx.applySecurityWorld(osx, linuxosx, null);
                linuxosx.check_Users();

                if (linuxosx.sw_version > 12504) {
                    // This is really separate to Client install but can still prep by copy over to RFS
                    linuxosx.applyFirmware();
                }
                break;
            case "nix":
                System.out.println("Unix OS");
                SecurityWorldLinux nix = new SecurityWorldLinux("a", (short) 12505, "c");
                nix.check_Mount();
                nix.checkEnvironmentVariables();
                nix.check_Existing_SW(osx);
                nix.checkJava();
                nix.check_Users();
                break;
            case "linux":
                System.out.println("Linux OS");
                SecurityWorldLinux linux = new SecurityWorldLinux("a", (short) 12505, "c");

                linux.check_Existing_SW(osx);
                linux.checkEnvironmentVariables();
                linux.check_Mount();
                linux.unpackSecurityWorld("linux.tar.gz", "mnt");
                linux.applySecurityWorld(osx, linux, null);
                linux.checkJava();
                linux.check_Users();
                break;
            case "sunos":
                System.out.println("Solaris OS");
                SecurityWorldLinux sun = new SecurityWorldLinux("a", (short) 12505, "c");

                sun.check_Existing_SW(osx);
                sun.checkEnvironmentVariables();
                sun.check_Mount();
                sun.unpackSecurityWorld("task.tgz", "mnt");
                sun.applySecurityWorld(osx, sun, null);
                sun.checkJava();
                sun.check_Users();
                break;
            default:
                System.out.println("Unknown OS");
        }

        log.close();
        System.exit(0);
    }
}
