package sample;

import java.io.File;

/**
 * Created by lennart on 3/20/2017.
 * This class will contain methods to download files via ftp and determine locally installed mods
 */
public class Downloader {
    Downloader() {
    }

    public File[] getLocalFiles(String dir) {
        File localFolder = new File(dir);
        boolean success = localFolder.mkdir();
        if(!success){
            System.out.println("Mod folder already exists");
        }
        File[] filesList = localFolder.listFiles();

        // Print name of each file in mods folder
        if (filesList != null) {
            for (File file : filesList)
                if (file.isFile()) System.out.println(file.getName());
        }

        return filesList;
    }

}
