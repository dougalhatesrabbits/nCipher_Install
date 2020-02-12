package com.file;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


public class IOImage {
    //Add to th LogRecord
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    int width = 963;    //width of the image
    int height = 640;   //height of the image
    BufferedImage image = null;
    File f = null;

    public void readImage(String filename) {
        LOGGER.fine("New instance of |readImage| started");

        try {
            //
            f = new File(filename); //image file path
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            image = ImageIO.read(f);
            System.out.println("Reading image complete.");
        } catch (IOException ex) {
            ex.printStackTrace();
            LOGGER.logrb(Level.WARNING, "IOFile", "readImage", (String) null, "Unable to open file", ex);
        }
    }

    public void writeImage (String filename, String format) {
        LOGGER.fine("New instance of |writeImage| started");

            try {
                f = new File(filename);  //output file path
                ImageIO.write(image, format, f);
                System.out.println("Writing image complete.");
            } catch (IOException ex) {
                ex.printStackTrace();
                LOGGER.logrb(Level.WARNING, "IOFile", "writeImage", (String) null, "Unable to write file", ex);
            }
        }
}
