/*
 *   Copyright (c) 2020. David Brooke
 *   This file is subject to the terms and conditions defined in
 *   file 'LICENSE.txt', which is part of this source code package.
 */

package com.file;

import com.platform.ConsoleColours;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Logger;

import static java.nio.file.FileVisitResult.CONTINUE;

//for VMs

public class Find {
    // Always use the classname, this way you can refactor
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static class Finder
            extends SimpleFileVisitor<Path> {
        // Always use the classname, this way you can refactor
        private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        private final PathMatcher matcher;
        private int numMatches = 0;

        public Finder(String pattern) {
            LOGGER.fine("running -Finder- method");
            matcher = FileSystems.getDefault()
                    .getPathMatcher("glob:" + pattern);
        }

        // Compares the glob pattern against
        // the file or directory name.
        void find(Path file) {
            LOGGER.fine("running -find- method");
            Path name = file.getFileName();
            if (name != null && matcher.matches(name)) {
                numMatches++;
                //System.out.println(file);
                LOGGER.info(file.toString());
            }
        }

        // Prints the total number of
        // matches to standard out.
        void done() {
            LOGGER.fine("running -done- method");
            System.out.println(ConsoleColours.YELLOW +"Matched: "
                    + numMatches +ConsoleColours.RESET);
            LOGGER.info("Matched: "
                    + numMatches);
        }

        // Invoke the pattern matching
        // method on each file.
        @Override
        public FileVisitResult visitFile(Path file,
                                         BasicFileAttributes attrs) {
            find(file);
            return CONTINUE;
        }

        // Invoke the pattern matching
        // method on each directory.
        @Override
        public FileVisitResult preVisitDirectory(Path dir,
                                                 BasicFileAttributes attrs) {
            find(dir);
            return CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file,
                                               IOException exc) {
            System.err.println(exc);
            LOGGER.severe(exc.getMessage());
            return CONTINUE;
        }
    }

    static void usage() {
        System.err.println("java Find <path>" +
                " -name \"<glob_pattern>\"");
        System.exit(-1);
    }

    public static void main(String[] args)
            throws IOException {

        if (args.length < 3 || !args[1].equals("-name"))
            usage();

        Path startingDir = Paths.get(args[0]);
        String pattern = args[2];

        Finder finder = new Finder(pattern);
        Files.walkFileTree(startingDir, finder);
        finder.done();
    }
}
