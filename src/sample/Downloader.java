package sample;

import java.io.File;

/**
 * Created by lennart on 3/20/2017.
 * This class will contain methods to download files via ftp and determine locally installed mods
 */
class Downloader {
    private Controller cont;
    Downloader(Controller parent) {
        cont=parent;
    }

    File[] getLocalFiles(String dir) {
        File localFolder = new File(dir);
        boolean success = localFolder.mkdir();
        if(!success) cont.printOutput("Mod folder already exists or could not be created",true);
        File[] filesList = localFolder.listFiles();

        // Print name of each file in mods folder
        if (filesList != null) {
            for (File file : filesList)
                if (file.isFile()) cont.printOutput(file.getName(), false);
        }

        return filesList;
    }

}
