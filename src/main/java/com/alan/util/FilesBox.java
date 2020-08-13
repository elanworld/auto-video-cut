package com.alan.util;

import java.io.File;
import java.nio.file.Path;

public class FilesBox {
    public static void move(String source, String dir) {
        String[] paths = com.alan.util.StringContainer.pathSplit(source);
        File file = Path.of(dir, paths[1] + paths[2]).toFile();
        new File(source).renameTo(file);
    }
}
