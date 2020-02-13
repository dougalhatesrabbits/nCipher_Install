package com.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

public class UnTarFile {

    public UnTarFile (String tarFile, String destFile){
        String tar = tarFile;
        String dest = destFile;

    }

    /**
     *
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
                //File outputFile = new File(destFile + File.separator + tarEntry.getName());
                System.out.println("outputFile File ---- " + outputFile.getAbsolutePath());
                outputFile.getParentFile().mkdirs();
                //outputFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(outputFile);
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
}
