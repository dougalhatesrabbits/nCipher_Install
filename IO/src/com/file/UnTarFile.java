package com.file;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

public class UnTarFile {

    /*
    public UnTarFile (String tarFile, String destFile){
        //String tar = tarFile;
        //String dest = destFile;
    }

    */

    //for zip
    static final int BUFFER = 2048;

    /**
     * https://netjs.blogspot.com/2017/05/how-to-untar-file-java-program.html
     *
     * In this post we'll see a Java program showing how to untar a tar file. It has both the steps to first decompress a .tar.gz file and later untar it.
     *
     * Refer Creating tar file and GZipping multiple files to see how to create a tar file.
     * Using Apache Commons Compress
     * Apache Commons Compress library is used in the code for untarring a file. You can download it from here – https://commons.apache.org/proper/commons-compress/download_compress.cgi.
     *
     * Make sure to add commons-compress-xxx.jar in your application’s class path. I have used commons-compress-1.13 version.
     *
     * Java example to untar a file
     * This Java program has two methods deCompressGZipFile() method is used to decompress a .tar.gz file to get a .tar file. Using unTarFile() method this .tar file is untarred.
     * @param tar
     * @param dest
     * @throws IOException
     */
    public void unTarFile(File tar, File dest) throws IOException{
        FileInputStream fis = new FileInputStream(tar);
        TarArchiveInputStream tis = new TarArchiveInputStream(fis);
        TarArchiveEntry tarEntry = null;

        // tarIn is a TarArchiveInputStream
        while ((tarEntry = tis.getNextTarEntry()) != null) {
            File outputFile = new File(dest + File.separator + tarEntry.getName());

            if(tarEntry.isDirectory()){

                System.out.println("outputFile Directory ---- "
                        + outputFile.getAbsolutePath());
                if(!outputFile.exists()){
                    outputFile.mkdirs();
                }
            }else{
                File output = new File(dest + File.separator + tarEntry.getName());
                System.out.println("outputFile File ---- " + outputFile.getAbsolutePath());
                output.getParentFile().mkdirs();
                output.createNewFile();
                FileOutputStream fos = new FileOutputStream(output);
                IOUtils.copy(tis, fos);
                fos.close();
            }
        }
        tis.close();
    }

    /**
     * Method to decompress a gzip file
     * @param gZippedFile
     * @param tarFile
     * @throws IOException
     */
    public File deCompressGZipFile(File gZippedFile, File tarFile) throws IOException{
        FileInputStream fis = new FileInputStream(gZippedFile);
        GZIPInputStream gZIPInputStream = new GZIPInputStream(fis);
        FileOutputStream fos = new FileOutputStream(tarFile);

        byte[] buffer = new byte[1024];
        int len;
        while((len = gZIPInputStream.read(buffer)) > 0){
            fos.write(buffer, 0, len);
        }

        fos.close();
        gZIPInputStream.close();
        return tarFile;

    }

    /**
     * This method is used to get the tar file name from the gz file
     * by removing the .gz part from the input file
     * @param inputFile
     * @param outputFolder
     * @return
     */
    public static String getFileName(File inputFile, String outputFolder){
        return outputFolder + File.separator +
                inputFile.getName().substring(0, inputFile.getName().lastIndexOf('.'));
    }

    public void unzip(String zip, File output) throws IOException {
        try {
            String infolder = zip.substring(0,zip.lastIndexOf('.'));
            File folder = new File(output + "/" + infolder);
            if (!folder.exists()) {
                folder.mkdir();
            }

            BufferedOutputStream dest = null;
            // zipped input
            FileInputStream fis = new FileInputStream(zip);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                System.out.println("Extracting: " + entry);
                int count;
                byte data[] = new byte[BUFFER];
                String fileName = entry.getName();
                File newFile = new File(folder + File.separator + fileName);
                // If directory then just create the directory (and parents if required)
                if (entry.isDirectory()) {
                    if (!newFile.exists()) {
                        newFile.mkdirs();
                    }
                } else {

                    // write the files to the disk
                    FileOutputStream fos = new FileOutputStream(newFile);
                    dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                    }
                    dest.flush();
                    dest.close();
                }
                zis.closeEntry();

            }
            zis.close();
        } catch(Exception e) {
                e.printStackTrace();
        }

    }
}
