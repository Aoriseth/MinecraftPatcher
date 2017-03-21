package sample;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by lennart on 3/20/2017.
 * This class will contain methods to download files via ftp and determine locally installed mods
 */
class Downloader {
    private Controller cont;
    Downloader(Controller parent) {
        cont=parent;
    }

    private File[] getLocalFiles(String dir) {
        File localFolder = new File(dir+"\\mods");
        boolean success = localFolder.mkdir();
        if(!success) cont.printOutput("Mod folder already exists or could not be created",true);
        return localFolder.listFiles();
    }

    void patch(String locdir, String link) {
        String[] uri = link.split("/",2);
        String serverAddress = uri[0];
        String serverFolder = "/"+uri[1];

        File[] localFiles = getLocalFiles(locdir);
        FTPClient ftp = ftpConnect(serverAddress);
        if (ftp!=null) {
            cont.printOutput("Successfully connected to ftp server", true);
            cont.printOutput("Connected to "+serverAddress,true);
            cont.printOutput("Loading folder "+serverFolder,true);
            FTPFile[] remoteFiles = getServerFiles(ftp, serverFolder);
            cont.printOutput("Missing Mods",true);
            ArrayList<String> missingMods = getMissing(localFiles,remoteFiles);
            for (String test:missingMods
                 ) {
                cont.printOutput(test,false);
            }
            ArrayList<String> excessMods = getExcess(localFiles,remoteFiles);
        }

    }

    private ArrayList<String> getExcess(File[] localFiles, FTPFile[] remoteFiles) {

        return null;
    }

    private ArrayList<String> getMissing(File[] localFiles, FTPFile[] remoteFiles) {
        ArrayList<String> foundMods = new ArrayList<>();
        for (FTPFile mod:remoteFiles) {
            boolean found = false;
            for (File local:localFiles) {
                if (mod.getName().equals(local.getName())) found = true;
            }
            if (!found&&mod.isFile()) {
                foundMods.add(mod.getName());
            }
        }
        return foundMods;
    }

    private FTPClient ftpConnect(String serverAddress) {
        FTPClient ftp = new FTPClient();
        try {
            ftp.connect(serverAddress);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        ftp.enterLocalPassiveMode();
        try {
            ftp.login("anonymous","");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return ftp;
    }

    private FTPFile[] getServerFiles(FTPClient ftp, String folder) {
        try {
            return ftp.listFiles(folder);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
