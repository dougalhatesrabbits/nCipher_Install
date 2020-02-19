import java.util.Map;

public class SecurityWorldWindows extends SecurityWorld {
    /***
     * Child class
     *
     * @param name
     * @param version
     */

    String sw_filename;
    short    sw_version;
    String sw_location;
    String value = System.getenv("ProgramFiles");
    //System.out.println(System.getenv("ProgramFiles(X86)"));
    final String NFAST_HOME = "%PROGRAM_FILES%/ncipher/";
    final String NFAST_KMDATA = "%PROGRAM_DATA%/key management data";
    final String CLASSPATH = null;
    final String PATH = NFAST_HOME + "/bin";
    final String JAVA_PATH = null;

    // Default constructor
    public SecurityWorldWindows(String name, short version, String location) {
        super(name, version, location);
    }
}
