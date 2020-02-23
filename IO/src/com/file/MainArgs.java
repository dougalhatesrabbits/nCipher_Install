package com.file;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.util.logging.Logger;

public class MainArgs {
    // Always use the classname, this way you can refactor
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public void getArgs(String[] args) {
        LOGGER.fine("running -getArgs- method");
        final ArgumentParser parser = ArgumentParsers.newFor("nCipher Install").build()
                .defaultHelp(true)
                .description("Installs and configures a Security World.");
        parser.addArgument("-l", "--level")
                .choices("debug", "info", "warning").setDefault("warning")
                .help("Specify logging level.");
        parser.addArgument("-v", "version")
                .help("Gets program version");
        parser.addArgument("file").nargs("*")
                .help("File to calculate checksum");
        Namespace ns = null;

        try {
            ns = parser.parseArgs(args);

        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }
        String level = null;
        try {
            level = ns.getString("level");
        } catch (Exception e) {
            System.err.printf("Could not get loggging level %s: %s",
                    ns.getString("level"), e.getMessage());
            System.exit(1);
        }
        System.exit(0);
        //return level;


    }

}
