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
    final String NFAST_HOME = "%PROGRAMFILES%/nfast/";
    final String NFAST_KMDATA = "%PROGRAMDATA%/key management data";
    final String CLASSPATH = null;
    final String PATH = "%NFAST_HOME%/bin";
    final String JAVA_PATH = null;

    // Default constructor
    public SecurityWorldWindows(String name, short version, String location) {
        super(name, version, location);
    }
}
