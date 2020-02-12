package com.log;

import java.io.IOException;
import java.util.logging.*;

public class MyLogger {
    static private FileHandler fileTxt;
    //static private SimpleFormatter formatterTxt;
    static private CustomFormatter formatterTxt;

    static private FileHandler fileHTML;
    //static private Formatter formatterHTML;
    static private MyHtmlFormatter formatterHTML;

    static public void setup() throws IOException {

        // Get the global LOGGER to configure it
        Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        LOGGER.setLevel(Level.SEVERE);


        //LOGGER.addHandler(new ConsoleHandler());
        // suppress the logging output to the console
        /*
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }
         */

        // Create Handlers/Appenders
        fileTxt = new FileHandler("Logging.txt", true);
        fileHTML = new FileHandler("Logging.html");

        // Create txt Formatter
        //formatterTxt = new SimpleFormatter();
        formatterTxt = new CustomFormatter();
        fileTxt.setFormatter(formatterTxt);
        LOGGER.addHandler(fileTxt);

        // Create HTML Formatter
        formatterHTML = new MyHtmlFormatter();
        fileHTML.setFormatter(formatterHTML);
        LOGGER.addHandler(fileHTML);

    }
}
