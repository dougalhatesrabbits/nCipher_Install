import java.util.Map;
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

    // Default constructor
    public Windows(String name, String version, String location) {
        super(name, version, location);
    }
}
