package sample;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.lang.reflect.Array;
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

            final ArrayList<String> missingMods = getMissing(localFiles,remoteFiles);
            for (String test:missingMods) cont.printOutput(test, false);

            ArrayList<String> excessMods = getExcess(localFiles,remoteFiles);

            Runnable task1 = () -> downloadMissing(missingMods,locdir,serverFolder,ftp);
            new Thread(task1).start();
            cont.printOutput("Patching Finished, ready to launch!",true);
        }

    }

    private void downloadMissing(ArrayList<String> missingMods, String locdir, String serverFolder, FTPClient ftp) {
        double progress = 0.0;
        cont.updateProgress(progress);
        double total = missingMods.size();
        double step = 1.0;
        if (total>0.0) step = 1.0/total;

        for (String missing:missingMods) {
            cont.printOutput("Missing file: "+missing+", Downloading...",false);
            String remoteMod = serverFolder + missing;
            File localMod = new File(locdir+"\\mods\\"+missing);

            boolean success = false;
            try (OutputStream stream = new BufferedOutputStream(new FileOutputStream(localMod))) {
                success= ftp.retrieveFile(remoteMod,stream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (success) {
                cont.printOutput("Download success.", false);
                progress+=step;
                cont.updateProgress(progress);
            }

        }
    }

    private ArrayList<String> getExcess(File[] localFiles, FTPFile[] remoteFiles) {
        ArrayList<String> unfoundMods = new ArrayList<>();
        for (File mod:localFiles) {
            boolean found = false;
            for (FTPFile remote:remoteFiles) {
                if (mod.getName().equals(remote.getName())) found = true;
            }
            if (!found&&mod.isFile()) unfoundMods.add(mod.getName());
        }
        return unfoundMods;
    }

    private ArrayList<String> getMissing(File[] localFiles, FTPFile[] remoteFiles) {
        ArrayList<String> foundMods = new ArrayList<>();
        for (FTPFile mod:remoteFiles) {
            boolean found = false;
            for (File local:localFiles) {
                if (mod.getName().equals(local.getName())) found = true;
            }
            if (!found&&mod.isFile()) foundMods.add(mod.getName());
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
