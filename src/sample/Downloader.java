package sample;

import java.io.File;

/**
 * Created by lennart on 3/20/2017.
 */
public class Downloader {
    public Downloader() {
    }

    public File[] getLocalFiles(String dir) {
        File localFolder = new File(dir);
        localFolder.mkdir();
        File[] filesList = localFolder.listFiles();
        for (File file : filesList) {
            if (file.isFile()) {
                System.out.println(file.getName());
            }
        }
        return filesList;
    }

}
