/*
 *   Copyright (c) 2020. David Brooke
 *   This file is subject to the terms and conditions defined in
 *   file 'LICENSE.txt', which is part of this source code package.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.file.SecWorld;
import com.log.InstallLogger;
import com.platform.ConsoleColours;
import com.platform.Platform;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Install {
    long start = System.currentTimeMillis();
    // Always use the classname, this way you can refactor
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static String argLevel = null;
    private static String argFile = null;
    private static String argType = null;
    private static String argSilent = null;

    public static String read(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }

    public static void main(String[] args) throws IOException {

        //TODO SUDO startup!
        /*
        Process p = Runtime.getRuntime().exec("id -u");
        String output = read(p.getInputStream());
        String error = read(p.getErrorStream());
        if (Integer.parseInt(output) > 0){
            System.out.println("User must be elevated as root/admin to run this installer: " +output +error);
            System.exit(1);
        }

         */

        final ArgumentParser parser = ArgumentParsers.newFor("nCipher_Install.jar").build()
                .defaultHelp(true)
                .version("${prog} 0.4")
                .description("Installs and configures a Security World. Run as sudo.\n\nTypically: " + ConsoleColours.YELLOW +
                        "sudo java -jar nCipher_Install.jar" + ConsoleColours.RESET + "\n\nJson files are needed at runtime");
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
            System.out.println(ConsoleColours.CLS + "Namespace = " + ns +ConsoleColours.RESET);
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
        long start = System.currentTimeMillis();

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
            //PrintStream console = System.out;

            // Assign o to output stream
            System.setOut(o);
            //System.out.println("This will be written to the text file");

            // Use stored value for output stream return to console
            //System.setOut(console);
            //System.out.println("This will be written on the console!");
        }

        Platform os = new Platform();
        String osName = os.getOsName();

        if (osName.equals("windows")) {
            System.out.println("Windows OS");
            LOGGER.info("Windows OS");
            Windows windows = new Windows();
        } else if (osName.equals("mac os x")) {
            System.out.println(ConsoleColours.BLUE_BRIGHT + "MAC OS machine" + ConsoleColours.RESET);
            LOGGER.info("MAC OS machine");
            OSX osx = new OSX();

            // **Synchronous** //
            // ******************
            if (argType.equals("remove")) {
                osx.removeExistingSW();
                System.exit(0);
            }

            if (argSilent.equals("No")) {
                Boolean macStatus = osx.checkExistingSW();
                if (macStatus) {
                    osx.removeExistingSW();
                }
            } else {
                osx.removeExistingSW();
            }

            ObjectMapper osxmapper = new ObjectMapper();
            String osxjsonfile = System.getProperty("user.dir") + "/secWorld.json";
            try {
                // JSON file to Java object
                SecWorld world = osxmapper.readValue(new File(osxjsonfile), SecWorld.class);
                if (argFile == null) {
                    for (String search : world.getLinuxSearch()) {
                        System.out.println("\nSearching " + search);
                        osx.sw_files = osx.getSecWorld(search);
                    }
                    //System.out.println(osx.sw_files);
                    osx.sw_filename = osx.getIsoChoices();
                } else {
                    for (String search : world.getLinuxSearch()) {
                        osx.sw_files = osx.getSecWorld(search, argFile);

                    }
                    Integer idx = osx.sw_files.size();
                    if (idx > 0) {
                        osx.sw_filename = osx.sw_files.get(idx - 1);

                    } else {
                        System.out.println("Could not find provided ISO " + osx.sw_filename);
                        System.exit(1);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            //osx.checkMount(osx.sw_filename);
            osx.checkMount2(osx.sw_filename);
            osx.getTars(); //also unpacks secWorld
            osx.applySecWorld();
            osx.applyDrivers();
            osx.restartService();

            int osxversion = Integer.parseInt(osx.sw_version);
            if (osxversion > 125040) {
                // This is really separate to Client install but can still prep by copy over to RFS
                //osx.applyFirmware();
            }

            // **Asynchronous** //
            // *******************
            osx.checkEnvVariables();
            osx.checkJava();
            //osx.installJava();
            //osx.configureJava();
            osx.checkUsers();
        } else if (osName.equals("linux")) {
            System.out.println(ConsoleColours.BLUE_BRIGHT + "Linux OS machine" + ConsoleColours.RESET);
            LOGGER.info("Linux OS");

            Linux linux = new Linux();

            // **Synchronous** //
            // ******************

            //TODO Backup
            // .cknfastrc
            // .ssl.conf
            // .kmdata
            if (argType.equals("remove") && argSilent.equals("Yes")) {
                linux.removeExistingSW();
                System.exit(0);
            }
            if (argType.equals("remove") && argSilent.equals("No")) {
                Boolean linuxStatus = linux.checkExistingSW();
                if (linuxStatus) {
                    linux.removeExistingSW();
                }
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
                    //System.out.println(linux.sw_files);
                    linux.sw_filename = linux.getIsoChoices();
                } else {
                    //overloadad
                    for (String search : world.getLinuxSearch()) {
                        linux.sw_files = linux.getSecWorld(search, argFile);
                    }
                    Integer idx = linux.sw_files.size();
                    if (idx > 0) {
                        linux.sw_filename = linux.sw_files.get(idx - 1);

                    } else {
                        System.out.println("Could not find provided ISO " + linux.sw_filename);
                        System.exit(1);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //linux.checkMount(linux.sw_filename);
            linux.checkMount2(linux.sw_filename);
            linux.getTars(); //also unpacks secWorld
            linux.applySecWorld(); //installs SW
            //If HSM = Solo or Edge
            linux.applyDrivers(); //install drivers
            linux.restartService();

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
            linux.checkUsers();
        } else if (osName.equals("nix")) {
            System.out.println("Unix OS");
            LOGGER.info("Unix OS");
            OSX nix = new OSX();
        } else if (osName.equals("sunos")) {
            System.out.println("Solaris OS");
        } else {
            System.out.println("Unknown OS");
            System.exit(1);
        }

        log.close();
        System.err.println("Execution time: " +
                ((System.currentTimeMillis() - start) / 1000 + " secs"));
        System.exit(0);
    }
}
