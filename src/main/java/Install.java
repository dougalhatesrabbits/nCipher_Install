import com.log.*;
import com.platform.*;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Install {
    // Always use the classname, this way you can refactor
    private static final  Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

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
                .required(false)
                .help("Version 0.1");
        parser.addArgument("-f", "--file")
                .help("Hotfix/Security World File to install");
        parser.addArgument("-s", "--silent")
                .help("Silent mode install");
        Namespace ns = null;

        try {
            ns = parser.parseArgs(args);

        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }
        String argLevel = null;
        String argFile = null;
        String argType = null;
        String argSilent = null;
        try {
            argLevel = ns.getString("level");
        } catch (Exception e) {
            LOGGER.logp(Level.SEVERE,
                    "Install",
                    "main",
                    "Could not get logging level", e);
            System.err.printf("Could not get logging level %s: %s",
                    ns.getString("level"), e.getMessage());
            System.exit(1);
        }

        InstallLogger log = new InstallLogger();
        log.setup(argLevel);

        LOGGER.info("New instance of -Install- started");

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
       // System.out.println("\n" + os);

        switch(osx.getOsName()) {
            case "windows":
                System.out.println("Windows OS");
                Windows windows = new Windows("a","125040", "c");

                windows.checkExistingSW(osx);
                //windows.remove_Existing_SW(osx, null, windows);
                windows.checkJava();
                //windows.unpackSecurityWorld();
                windows.applySecWorld(osx, null, windows);
                windows.checkEnvVariables();
                windows.applyFirmware();
                break;
            case "mac os x":
                System.out.println("MAC OS machine");
                Linux linux = new Linux("a", "125040", "c");

                //linux.check_Existing_SW(osx);
                /*linux.remove_Existing_SW(osx, linux, null);*/
                //linux.checkEnvVariables();

                //linux.checkJava();
                linux.getSecWorld();
                linux.sw_filename= linux.getIsoChoices();
                linux.checkMount(linux.sw_filename);
                //linux.unpackSecWorld("SecWorld-linux64-user-12.60.3.iso", "mnt");
                //linux.unpackSecurityWorld("Archive.zip", "mnt");
                //linux.unpackSecurityWorld("commons-compress-1.20-bin.tar.gz", "mnt");
                //linux.unpackSecurityWorld("apache-maven-3.6.3-bin.tar", "mnt");
                //linux.applyDrivers
                //linux.applySecWorld(osx, linux, null);
                //linux.checkUsers();

                int version = Integer.parseInt(linux.sw_version);
                if (version > 125040) {
                    // This is really separate to Client install but can still prep by copy over to RFS
                    linux.applyFirmware();
                }
                break;

            case "nix":
                System.out.println("Unix OS");
                Linux linuxreal = new Linux("a", "125050", "c");
                linuxreal.checkMount(linuxreal.sw_filename);
                linuxreal.checkEnvVariables();
                linuxreal.checkExistingSW(osx);
                linuxreal.checkJava();
                linuxreal.checkUsers();
                break;
            case "sunos":
                System.out.println("Solaris OS");
                break;
            default:
                System.out.println("Unknown OS");
        }

        log.close();
        System.exit(0);
    }
}
