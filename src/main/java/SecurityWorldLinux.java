import com.iso.ReadIso;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class SecurityWorldLinux extends SecurityWorld {
    // Always use the classname, this way you can refactor
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    /***
     * Child class
     *
     * @param name
     * @param version
     */

    String sw_filename = "Clive.iso";
    short    sw_version = 12603;
    String sw_location = "mnt";

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
        // Do checksum on iso
        ReadIso tempIso = new ReadIso(new File(sw_filename), new File(sw_location));
        System.out.println("Mounted " + File.listRoots() + "at /" + sw_location);
    }

    void check_Users() throws IOException {
        LOGGER.fine("New instance of -check_Users- started");
        System.out.println("Checking if Users are in correct Groups");
        //Process p = Runtime.getRuntime().exec("////command////");
    }

    void unpackSecurityWorld(String tarFile, File destFile) {
        LOGGER.fine("New instance of -UnpackSecurityWorld- started");
        System.out.println("Unpacking Security World...");


    }





}
