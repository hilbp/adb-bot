package com.hilbp.adb.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class FileSaveUtil {

//    public static void listSaveToFile(String path, List<?> list) throws IOException {
//
//        OutputStream errorOs = new FileOutputStream(path);
//        IOUtils.writeLines(list, IOUtils.LINE_SEPARATOR_UNIX, errorOs, StandardCharsets.UTF_8);
//        errorOs.close();
//    }


    public static void writeLines(String path, List<?> list, boolean append) throws IOException {

        FileUtils.writeLines(new File(path), list, append);
    }


    public static List<String> fileToList(String path) throws IOException {

        List<String> data = IOUtils.readLines(new FileInputStream(path), "UTF-8");
        return data;
    }
}
