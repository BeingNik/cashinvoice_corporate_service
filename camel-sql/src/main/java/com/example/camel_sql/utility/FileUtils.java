package com.example.camel_sql.utility;

public class FileUtils {

    public static String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }

        int dotIndex = fileName.lastIndexOf('.');

        // If there's no dot or the dot is at the beginning of the string
        if (dotIndex == -1 || dotIndex == 0) {
            return "";
        }

        return fileName.substring(dotIndex + 1);
    }
}
