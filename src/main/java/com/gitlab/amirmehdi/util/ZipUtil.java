package com.gitlab.amirmehdi.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

    /**
     * This method zips the directory
     *
     * @param dir
     * @param zipDirName
     */
    public static void zipDirectory(File dir, String zipDirName) throws IOException {
        List<String> filesListInDir = new ArrayList<String>();
        populateFilesList(dir, filesListInDir);
        //now zip files one by one
        //create ZipOutputStream to write to the zip file
        FileOutputStream fos = new FileOutputStream(zipDirName);
        ZipOutputStream zos = new ZipOutputStream(fos);
        for (String filePath : filesListInDir) {
            System.out.println("Zipping " + filePath);
            //for ZipEntry we need to keep only relative file path, so we used substring on absolute path
            ZipEntry ze = new ZipEntry(filePath.substring(dir.getAbsolutePath().length() + 1, filePath.length()));
            zos.putNextEntry(ze);
            //read the file and write to ZipOutputStream
            FileInputStream fis = new FileInputStream(filePath);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
            zos.closeEntry();
            fis.close();
        }
        zos.close();
        fos.close();

    }

    /**
     * This method populates all the files in a directory to a List
     *
     * @param dir
     * @param filesListInDir
     * @throws IOException
     */
    private static void populateFilesList(File dir, List<String> filesListInDir) {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) filesListInDir.add(file.getAbsolutePath());
            else populateFilesList(file, filesListInDir);
        }
    }

    public static BufferedReader getBufferedReader(byte[] responseEntity) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(responseEntity);
        GZIPInputStream gzis = new GZIPInputStream(bais);
        InputStreamReader reader = new InputStreamReader(gzis);
        return new BufferedReader(reader);
    }
}
