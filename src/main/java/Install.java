import com.file.SecWorld;
import com.log.*;
import com.platform.*;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Install {
    // Always use the classname, this way you can refactor
    private static final  Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static String argLevel = null;
    private static String argFile = null;
    private static String argType = null;
    private static String argSilent = null;

    public static void main(String[] args) throws IOException {

        final ArgumentParser parser = ArgumentParsers.newFor("nCipher Install").build()
                .defaultHelp(true)
                .version("${prog} 2.0")
                .description("Installs and configures a Security World.");
        parser.addArgument("-l", "--level")
                .choices("debug", "info", "warning").setDefault("info")
                .help("Specify logging level.");
        parser.addArgument("-t", "--type")
                .choices("install", "remove").setDefault("install")
                .help("Specify install type.");
        parser.addArgument("--version");

        parser.addArgument("-f", "--file")
                .setDefault((Object) null)
                .help("Hotfix/Security World File to install");
        parser.addArgument("-s", "--silent")
                .choices("Yes", "No").setDefault("No")
                .help("Silent mode install");
        Namespace ns = null;

        try {
            ns = parser.parseArgs(args);
            System.out.println("Namespace = " + ns);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }

        try {
            argLevel = ns.getString("level");
        } catch (Exception e) {
            LOGGER.logp(Level.WARNING,
                    "Install",
                    "main",
                    "Could not get logging level", e);
            System.err.printf("Could not get logging level %s: %s",
                    ns.getString("level"), e.getMessage());
            System.exit(0);
        }

        try {
            argFile = ns.getString("file");
        } catch (Exception e) {
            LOGGER.logp(Level.WARNING,
                    "Install",
                    "main",
                    "Could not get security world file from args, none supplied. Will search file system", e);
            System.err.printf("Could not get security world file from args, none supplied. Will search file system %s: %s",
                    ns.getString("level"), e.getMessage());
        }

        try {
            argType = ns.getString("type");
        } catch (Exception e) {
            LOGGER.logp(Level.WARNING,
                    "Install",
                    "main",
                    "Could not get install type from args, none supplied. Will search file system", e);
            System.err.printf("Could not get security world file from args, none supplied. Will search file system %s: %s",
                    ns.getString("level"), e.getMessage());
            System.exit(1);

        }

        try {
            argSilent = ns.getString("silent");
        } catch (Exception e) {
            LOGGER.logp(Level.WARNING,
                    "Install",
                    "main",
                    "Could not get security world file from args, none supplied. Will search file system", e);
            System.err.printf("Could not get security world file from args, none supplied. Will search file system %s: %s",
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

        // Sanity check args
        if (argSilent.equals("Yes") && argFile == null) {
            System.err.println("Silent mode requires iso file");
            LOGGER.severe("Silent mode requires iso file");
            System.exit(1);
        }

        if (argSilent.equals("Yes")) {
            // Silent mode Redirecting System.out.println() output to a file using print stream
            // Creating a File object that represents the disk file.
            PrintStream o = new PrintStream(new File("silent.txt"));

            // Store current System.out before assigning a new value
            PrintStream console = System.out;

            // Assign o to output stream
            System.setOut(o);
            //System.out.println("This will be written to the text file");

            // Use stored value for output stream return to console
            //System.setOut(console);
            //System.out.println("This will be written on the console!");
        }


        Platform os = new Platform();
        //String os = osx.getOsName();
        // System.out.println("\n" + os);

        switch(os.getOsName()) {
            case "windows":
                System.out.println("Windows OS");
                LOGGER.info("Windows OS");
                Windows windows = new Windows();

                windows.checkExistingSW(os);
                //windows.remove_Existing_SW(osx, null, windows);
                windows.checkJava();
                //windows.unpackSecurityWorld();
                windows.applySecWorld(os, null, windows, null);
                windows.checkEnvVariables();
                windows.applyFirmware();
                break;
            case "mac os x":
                System.out.println(ConsoleColours.BLUE_BRIGHT + "MAC OS machine" + ConsoleColours.RESET);
                LOGGER.info("MAC OS machine");
                OSX osx = new OSX();

                // **Synchronous** //
                // ******************
                if (argType.equals("remove")) {
                    osx.removeExistingSW(os, null, null, osx);
                    System.exit(0);
                }
                if (argSilent.equals("No")) {
                    osx.checkExistingSW(os);
                } else {
                    osx.removeExistingSW(os, null, null, osx);
                }
                if (argFile == null) {
                    osx.getSecWorld();
                    osx.sw_filename = osx.getIsoChoices();
                } else {
                    osx.sw_filename = osx.getSecWorld(argFile);
                }
                osx.checkMount(osx.sw_filename);
                osx.getTars();
                /*
                //linux.unpackSecWorld("SecWorld-linux64-user-12.60.3.iso", "mnt");
                //linux.unpackSecurityWorld("Archive.zip", "mnt");
                //linux.unpackSecurityWorld("commons-compress-1.20-bin.tar.gz", "mnt");
                //linux.unpackSecurityWorld("apache-maven-3.6.3-bin.tar", "mnt");
                 */
                osx.applySecWorld(os, null, null, osx);
                //linux.applyDrivers
                int version = Integer.parseInt(osx.sw_version);
                if (version > 125040) {
                    // This is really separate to Client install but can still prep by copy over to RFS
                    //linux.applyFirmware();
                }

                // **Asynchronous** //
                // *******************
                osx.checkEnvVariables();
                osx.checkJava();
                //linux.installJava();
                //linux.configureJava();
                //linux.checkUsers();
                break;
            case "linux":
                System.out.println(ConsoleColours.BLUE_BRIGHT + "Linux OS machine" + ConsoleColours.RESET);
                LOGGER.info("Linux OS");

                Linux linux = new Linux();

                // **Synchronous** //
                // ******************
                if (argType.equals("remove")) {
                    linux.removeExistingSW(os, linux, null, null);
                    System.exit(0);
                }
                if (argSilent.equals("No")) {
                    linux.checkExistingSW(os);
                } else {
                    linux.removeExistingSW(os, linux, null, null);
                }

                ObjectMapper mapper = new ObjectMapper();
                String jsonfile = System.getProperty("user.dir") + "/secWorld.json";
                try {
                    // JSON file to Java object
                    SecWorld world = mapper.readValue(new File(jsonfile), SecWorld.class);
                    if (argFile == null) {
                            for (String search : world.getLinuxSearch()) {
                                System.out.println("\nSearching " + search);
                                linux.sw_files = linux.getSecWorld(search);
                            }
                            System.out.println(linux.sw_files);
                            linux.sw_filename = linux.getIsoChoices();
                    } else {
                        for (String search : world.getLinuxSearch()) {
                            linux.sw_filename = linux.getSecWorld(search, argFile);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                linux.checkMount(linux.sw_filename);
                linux.getTars();

                /*
                //linux.unpackSecWorld("SecWorld-linux64-user-12.60.3.iso", "mnt");
                //linux.unpackSecurityWorld("Archive.zip", "mnt");
                //linux.unpackSecurityWorld("commons-compress-1.20-bin.tar.gz", "mnt");
                //linux.unpackSecurityWorld("apache-maven-3.6.3-bin.tar", "mnt");
                 */

                linux.applySecWorld(os, linux, null, null);
                //linux.applyDrivers
                int linuxversion = Integer.parseInt(linux.sw_version);
                if (linuxversion > 125040) {
                    // This is really separate to Client install but can still prep by copy over to RFS
                    //linux.applyFirmware();
                }

                // **Asynchronous** //
                // *******************
                linux.checkEnvVariables();
                linux.checkJava();
                //linux.installJava();
                //linux.configureJava();
                //linux.checkUsers();
                break;
            case "nix":
                System.out.println("Unix OS");
                LOGGER.info("Unix OS");
                OSX nix = new OSX();
                nix.checkMount(nix.sw_filename);
                nix.checkEnvVariables();
                nix.checkExistingSW(os);
                nix.checkJava();
                nix.checkUsers();
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
