import java.io.IOException;
import java.util.logging.Logger;

import com.log.MyLogger;

public class UseLogger {
    // Always use the classname, this way you can refactor
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);


    public static void doSomeThingAndLog() {
        // Image here some real work

        // Now we demo the logging
        /*
        // Set the LogLevel to Severe, only severe Messages will be written
        LOGGER.setLevel(Level.SEVERE);
        LOGGER.severe("Severe Log");
        LOGGER.warning("Severe Log");
        LOGGER.info("Severe Log");
        LOGGER.finest("Really not important");

        // Set the LogLevel to Info, severe, warning and info will be written
        // Finest is still not written
        LOGGER.setLevel(Level.INFO);
        LOGGER.severe("Info Log");
        LOGGER.warning("Info Log");
        LOGGER.info("Info Log");
        LOGGER.finest("Really not important");

         */

        //LOGGER.setLevel(Level.WARNING);
        LOGGER.severe("Warning Log");
        LOGGER.warning("Warning Log");
        LOGGER.info("Warning Log");
        LOGGER.config("Warning Log");
        LOGGER.fine("Warning Log");
        LOGGER.finer("Warning Log");
        LOGGER.finest("Really not important");
    }

    public static void main(String[] args) {
        UseLogger tester = new UseLogger();
        //LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME).setLevel(Level.INFO);
        try {
            MyLogger.setup();
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Problems with creating the log files");
        }
        tester.doSomeThingAndLog();
    }
}
