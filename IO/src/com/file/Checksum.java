/*
 *   Copyright (c) 2020. David Brooke
 *   This file is subject to the terms and conditions defined in
 *   file 'LICENSE.txt', which is part of this source code package.
 */

package com.file;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

public class Checksum {
    // Always use the classname, this way you can refactor
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static void main(String[] args) {
        ArgumentParser parser = ArgumentParsers.newFor("com.file.Checksum").build()
                .defaultHelp(true)
                .description("Calculate checksum of given files.");
        parser.addArgument("-t", "--type")
                .choices("SHA-256", "SHA-512", "SHA1").setDefault("SHA-256")
                .help("Specify hash function to use");
        parser.addArgument("file").nargs("*")
                .help("File to calculate checksum");
        Namespace ns = null;
        try {
            ns = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(ns.getString("type"));
        } catch (NoSuchAlgorithmException e) {
            System.err.printf("Could not get instance of algorithm %s: %s",
                    ns.getString("type"), e.getMessage());
            System.exit(1);
        }
        for (String name : ns.<String> getList("file")) {
            Path path = Paths.get(name);
            try (ByteChannel channel = Files.newByteChannel(path,
                    StandardOpenOption.READ)) {
                ByteBuffer buffer = ByteBuffer.allocate(4096);
                while (channel.read(buffer) > 0) {
                    buffer.flip();
                    digest.update(buffer);
                    buffer.clear();
                }
            } catch (IOException e) {
                System.err
                        .printf("%s: failed to read data: %s", e.getMessage(), path);
                continue;
            }
            byte[] md = digest.digest();
            StringBuffer sb = new StringBuffer();
            for (byte b : md) {
                String x = Integer.toHexString(0xff & b);
                if (x.length() == 1) {
                    sb.append("0");
                }
                sb.append(x);
            }
            System.out.printf("%s  %s\n", sb.toString(), name);
        }
    }
}
