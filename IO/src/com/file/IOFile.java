package com.file;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IOFile {
    //Add to th LogRecord
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public void readFile(String fileName) {
        LOGGER.fine("New instance of +readFile+ started");


        // The name of the file to open.
        String cwd = System.getProperty("user.dir");

        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.

            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }

            // Always close files.
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            ex.printStackTrace();
            LOGGER.logrb(Level.WARNING, "IOFile", "readFile", (String) null, "Unable to open file", ex);
        }
        catch(IOException ex) {
            ex.printStackTrace();
            LOGGER.logrb(Level.WARNING, "IOFile", "readFile", (String) null, "Error reading file", ex);
        }
    }

    public void readBinaryFile(String fileName) {
        LOGGER.fine("New instance of +readBinaryFile+ started");

        String cwd = System.getProperty("user.dir");
        try {
            // Use this for reading the data.
            byte[] buffer = new byte[1000];

            FileInputStream inputStream = new FileInputStream(fileName);

            // read fills buffer with data and returns
            // the number of bytes read (which of course
            // may be less than the buffer size, but
            // it will never be more).
            int total = 0;
            int nRead = 0;
            while ((nRead = inputStream.read(buffer)) != -1) {
                // Convert to String so we can display it.
                // Of course you wouldn't want to do this with
                // a 'real' binary file.
                System.out.println(new String(buffer));
                total += nRead;
            }

            // Always close files.
            inputStream.close();

            System.out.println("Binary read " + total + " bytes");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            LOGGER.logrb(Level.WARNING, "IOFile", "readBinaryFile", (String) null, "Unable to open file", ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            LOGGER.logrb(Level.WARNING, "IOFile", "readBinaryFile", (String) null, "Unable to open file", ex);
        }
    }

    public void writeFile(String fileName, String txt) {
        LOGGER.fine("New instance of +writeFile+ started");;

        try {
            // Assume default encoding.
            FileWriter fileWriter =
                    new FileWriter(fileName);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter =
                    new BufferedWriter(fileWriter);

            // Note that write() does not automatically
            // append a newline character.
            bufferedWriter.write("Hello there,");
            bufferedWriter.write(" here is some text.");
            bufferedWriter.newLine();
            bufferedWriter.write("We are writing");
            bufferedWriter.write(" the text to the file.\n");
            bufferedWriter.write(txt + "\n");

            // Always close files.
            bufferedWriter.close();
        }
        catch(IOException ex) {
            ex.printStackTrace();
            LOGGER.logrb(Level.WARNING, "IOFile", "writeFile", (String) null, "Unable to open file", ex);
        }
    }

    public void writeBinaryFile(String fileName, String txt) {
        LOGGER.fine("New instance of +writeBinaryFile+ started");

        try {
            // Put some bytes in a buffer so we can
            // write them. Usually this would be
            // image data or something. Or it might
            // be unicode text.
            String bytes = "Binary writing Hello there.\n";
            String morebytes = txt;

            byte[] buffer = bytes.getBytes();
            byte[] morebuffer = morebytes.getBytes();

            FileOutputStream outputStream = new FileOutputStream(fileName);

            // write() writes as many bytes from the buffer
            // as the length of the buffer. You can also
            // use
            // write(buffer, offset, length)
            // if you want to write a specific number of
            // bytes, or only part of the buffer.
            outputStream.write(buffer);
            outputStream.write(morebuffer);

            // Always close files.
            outputStream.close();

            System.out.println("Wrote " + buffer.length +
                    " bytes");
        }
        catch(IOException ex) {
            ex.printStackTrace();
            LOGGER.logrb(Level.WARNING, "IOFile", "writeBinaryFile", (String) null, "Unable to write binary file", ex);
        }
    }
}
