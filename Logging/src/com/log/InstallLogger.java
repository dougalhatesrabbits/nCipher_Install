package com.log;

import java.io.IOException;
import java.util.logging.*;

public class InstallLogger {

    public FileHandler fileTxt = null;
    static private CustomFormatter formatterTxt;
    public FileHandler fileHTML = null;
    static private MyHtmlFormatter formatterHTML;
    public FileHandler fileXML = null;
    static private XMLFormatter formatterXML;

    // Obtain a suitable logger.
    //private final static Logger logger = Logger.getLogger(InstallLogger.class.getName());
    //public static Logger logger = Logger.getLogger(InstallLogger.class.getName());

    public void setup(String level) throws IOException {
        // Log Manager
        LogManager.getLogManager().reset();
        //LogManager.getLogManager().readConfiguration(new FileInputStream("InstallLogger.properties"));
        //LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME).setLevel(Level.WARNING);
        // Get the global LOGGER to configure it
        Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        // This affects console and log files
        switch(level) {
            case "debug":
                LOGGER.setLevel(Level.FINE);
                break;
            case "error":
                LOGGER.setLevel(Level.SEVERE);
                break;
            case "warning":
                LOGGER.setLevel(Level.WARNING);
                break;
            default:
                LOGGER.setLevel(Level.INFO);
        }


        // Suppress Console at class level
        //LOGGER.setUseParentHandlers(false);

       /*
        // Suppress Console at Global level
        Logger globalLogger = Logger.getLogger("global");
        Handler[] handlers = globalLogger.getHandlers();
        for(Handler handler : handlers) {
            globalLogger.removeHandler(handler);
        }
        */

        /*
        // Suppress Console at Global level
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        for(Handler handler : handlers) {
            rootLogger.removeHandler(handler);
        }

        // suppress the logging output to the console
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }
        */

        try {
            // Create Handlers/Appenders
            fileTxt = new FileHandler("install-%g.log", 1024 * 1024, 10,  true);
            fileHTML = new FileHandler("install-log-%g.html", 1024 * 1024, 10,  true);
            fileXML = new FileHandler("install-log-%g.xml",1024 * 1024, 10,  true);

            // Create txt Formatter
            formatterTxt = new CustomFormatter();
            fileTxt.setFormatter(formatterTxt);
            LOGGER.addHandler(fileTxt);

            // Create HTML Formatter
            formatterHTML = new MyHtmlFormatter();
            fileHTML.setFormatter(formatterHTML);
            LOGGER.addHandler(fileHTML);

            // Create XML Formatter
            formatterXML = new XMLFormatter();
            fileXML.setFormatter(formatterXML);
            LOGGER.addHandler(fileXML);

        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        fileTxt.close();
        fileHTML.close();
        fileXML.close();
    }
}
