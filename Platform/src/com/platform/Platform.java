package com.platform;

import java.util.logging.Logger;

public class Platform {
    //Add to the LogRecord
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static String OS = null;

    public String getOsName() {
        LOGGER.fine("New instance of *getOSName* started");
        if(OS == null) { OS = System.getProperty("os.name").toLowerCase(); }
        return OS;
    }

    public boolean isWindows() {
        LOGGER.fine("New instance of *isWindows* started");
        return getOsName().startsWith("windows");
    }

    public boolean isUnix() {
        LOGGER.fine("New instance of *isUnix* started");
        return getOsName().startsWith("nix");
    }

    public boolean isMac() {
        LOGGER.fine("New instance of *isMac* started");
        return getOsName().startsWith("mac");
    }

    public boolean isSolaris() {
        LOGGER.fine("New instance of *isSolaris* started");
        return getOsName().startsWith("sunos");
    }

}
