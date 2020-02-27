import com.file.Find;
import com.platform.ConsoleColours;
import com.platform.RunProcBuilder;
import org.apache.tools.ant.DirectoryScanner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Windows extends SecurityWorld {
    // Always use the classname, this way you can refactor
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    /***
     * Child class
     *
     * @param name
     * @param version
     */

    String value = System.getenv("ProgramFiles");
    //System.out.println(System.getenv("ProgramFiles(X86)"));
    final String NFAST_HOME = "%PROGRAM_FILES%/ncipher/";
    final String NFAST_KMDATA = "%PROGRAM_DATA%/key management data";
    final String CLASSPATH = null;
    final String PATH = NFAST_HOME + "/bin";
    final String JAVA_PATH = null;
    Boolean removeStatus = false;

    ArrayList<Path> getSecWorld(String searchpath) throws IOException {
        LOGGER.fine("running -getSecWorld- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "Getting Security World" +ConsoleColours.RESET);
        LOGGER.info("Getting Security World");

        /*
        try {
            Find.Finder iso = new Find.Finder("*linux*.iso");
            //String ext = FilenameUtils.getExtension(tar);
            //Find.main(new String[]{"/Users/david/IdeaProjects/nCipher_Install", "-name", "*test"});
            Find.main(new String[]{searchpath, "-name", "*linux*.iso"});
            //isoList = iso.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
         */

        /*
        File[] dirs = new File(searchpath).listFiles((FileFilter) new WildcardFileFilter("*linux*.iso"));
        for (File dir : dirs) {
            System.out.println(dir);
            LOGGER.info((Supplier<String>) dir);
            if (dir.isDirectory()) {
                File[] files = dir.listFiles((FileFilter) new WildcardFileFilter("*linux*.iso"));
            }
        }
        */

        // https://ant.apache.org/manual/api/org/apache/tools/ant/DirectoryScanner.html
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setIncludes(new String[]{"**/*windows*.iso"});
        scanner.setBasedir(searchpath);
        scanner.setCaseSensitive(false);
        scanner.setFollowSymlinks(false);
        //scanner.setExcludes(new String[]{"**/."});
        try {
            scanner.scan();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] files = scanner.getIncludedFiles();
        System.out.println("Files: " +files.length);
        LOGGER.info("Files: " +files.length);
        //ArrayList<Path> swfiles = new ArrayList<Path>();
        //String found = null;
        for (String file : files) {
            String found = scanner.getBasedir() + "/" + file;
            //String ext = FilenameUtils.getExtension(found);
            //System.out.println(ext);
            Path path = Paths.get(found);
            System.out.println("Path: " +path);
            LOGGER.info("Path: " +path);
            sw_files.add(path);
        }
        return sw_files;
    }

    public Boolean checkExistingSW() throws IOException {
        LOGGER.fine("running -checkExistingSW- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nChecking Existing SW" + ConsoleColours.RESET);
        LOGGER.info("Checking existing SW");

        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nChecking for existing Security World in $NFAST_HOME: " + path + ConsoleColours.RESET);

        if (Files.exists(Paths.get(NFAST_HOME))) {
            System.out.println("Found SW, removing previous install: \nConfirm (Y) to proceed, (N) to abort installation >");
            Scanner myObj = new Scanner(System.in);  // Create a Scanner object
            String confirm = myObj.nextLine();  // Read user input

            if (confirm.equalsIgnoreCase("y")) {
                System.out.println("You entered proceed " + confirm);
                removeStatus = true;// Output user input
            } else {
                System.out.println("You entered stop " + confirm);
                System.exit(1);
            }
        } else {
            System.out.println("No existing SW found, proceeding with install");
        }
        return removeStatus;
    }

    public void removeExistingSW() {
        LOGGER.fine("running removeExistingSW method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nRemoving old Security World" + ConsoleColours.RESET);

        String cmd = NFAST_HOME + "/sbin/install -u";
        try {
            new RunProcBuilder().run(new String[]{cmd});
            System.out.println("Using NFAST " + cmd);
            LOGGER.info("Using NFAST " + cmd);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.logp(Level.WARNING,
                    "SecurityWorld",
                    "removeExistingSW",
                    "Cannot uninstall", e.fillInStackTrace());
        }
    }

    void applySecWorld() throws IOException {
        LOGGER.fine("running -applySecurityWorld- method");
        System.out.println(ConsoleColours.BLUE_UNDERLINED + "\nInstalling Security World" + ConsoleColours.RESET);

        //String cmd = "sudo " + NFAST_HOME + "/sbin/install";
        String cmd = "sudo ls -l" + NFAST_HOME;
        System.out.println(cmd);
        try {
            new RunProcBuilder().run(new String[]{"cmd", "-c", cmd});

            LOGGER.info(cmd);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.logp(Level.WARNING,
                    "SecurityWorld",
                    "removeExistingSW",
                    "Cannot uninstall", e.fillInStackTrace());
        }
    }
}

