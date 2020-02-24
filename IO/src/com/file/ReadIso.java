/*
 *   Copyright (c) 2020. David Brooke
 *   This file is subject to the terms and conditions defined in
 *   file 'LICENSE.txt', which is part of this source code package.
 */

package com.file;

import com.github.stephenc.javaisotools.loopfs.iso9660.Iso9660FileEntry;
import com.github.stephenc.javaisotools.loopfs.iso9660.Iso9660FileSystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

// for VMs

/***
 * This is a adaptation of https://github.com/danveloper/provisioning-gradle-plugin/blob/master/src/main/groovy/
 * gradle/plugins/provisioning/tasks/image/ImageAssemblyTask.groovy
 */

public class ReadIso {
    // Always use the classname, this way you can refactor
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    Iso9660FileSystem discFs;

    public ReadIso(File isoToRead, File saveLocation) {
        LOGGER.fine("running -ReadIso- method");

        try {
            //Give the file and mention if this is treated as a read only file.
            discFs = new Iso9660FileSystem(isoToRead, true);
        } catch (IOException e) {
            LOGGER.logp(Level.WARNING,
                    "ReadIso",
                    "ReadIso",
                    "Cannot unpack", e.fillInStackTrace());
            LOGGER.logp(Level.WARNING,
                    "ReadIso",
                    "ReadIso",
                    "Cannot unpack", e.getCause());
        }

        //Make our saving folder if it does not exist
        if (!saveLocation.exists()) {
            saveLocation.mkdirs();
        }

        //Go through each file on the disc and save it.
        assert discFs != null;
        for (Iso9660FileEntry singleFile : discFs) {
            if (singleFile.isDirectory()) {
                new File(saveLocation, singleFile.getPath()).mkdirs();
            } else {
                File tempFile = new File(saveLocation, singleFile.getPath());
                try {
                    //This is java 7, sorry if that is too new for some people
                    Files.copy(discFs.getInputStream(singleFile), tempFile.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                    LOGGER.logp(Level.WARNING,
                            "ReadIso",
                            "ReadIso",
                            "Cannot unpack", e.fillInStackTrace());
                }
            }
        }
    }
}
