package com.platform;

import java.util.logging.Logger;

public class Platform {
    //Add to the LogRecord
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static String OS = null;

    public String getOsName() {
        LOGGER.fine("running -getOSName- method");
        if(OS == null) { OS = System.getProperty("os.name").toLowerCase(); }
        return OS;
    }

    public boolean isWindows() {
        LOGGER.fine("running -isWindows- method");
        return getOsName().startsWith("windows");
    }

    public boolean isLinux() {
        LOGGER.fine("running -isLinux- method");
        return getOsName().startsWith("linux");
    }

    public boolean isUnix() {
        LOGGER.fine("running -isUnix- method");
        return getOsName().startsWith("nix");
    }

    public boolean isMac() {
        LOGGER.fine("running -isMac- method");
        return getOsName().startsWith("mac");
    }

    public boolean isSolaris() {
        LOGGER.fine("running -isSolaris- method");
        return getOsName().startsWith("sunos");
    }

}
