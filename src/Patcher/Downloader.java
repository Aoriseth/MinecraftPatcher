package Patcher;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by lennart on 3/20/2017.
 * This class will contain methods to download files via ftp and determine locally installed mods
 */
class Downloader {
    private final Controller cont;

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
            ftp.setBufferSize(1024000);
            cont.printOutput("Successfully connected to ftp server", true);
            cont.printOutput("Connected to "+serverAddress,true);
            cont.printOutput("Loading folder "+serverFolder,true);
            FTPFile[] remoteFiles = getServerFiles(ftp, serverFolder);
            final ArrayList<String> missingMods = getMissing(localFiles,remoteFiles);
            ArrayList<String> excessMods = getExcess(localFiles,remoteFiles);

            removeExcess(excessMods,locdir);
            cont.printOutput(locdir,true);
            downloadMissing(missingMods,locdir,serverFolder,ftp);
        }

    }

    private void removeExcess(ArrayList<String> excessMods, String locdir) {
        for (String mod:excessMods) {
            cont.printOutput("Removing excess mod " + mod,false);
            boolean success = new File(locdir + "\\mods\\" + mod).delete();
            if (success){
                cont.printOutput("Excess mod "+mod+" removed",false);
            }
        }
        cont.printOutput("Excess mods successfully removed",true);
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
            cont.printOutput(localMod.getAbsolutePath(),true);

            boolean success = false;
            try (OutputStream stream = new BufferedOutputStream(new FileOutputStream(localMod))) {
                success= ftp.retrieveFile(remoteMod,stream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            progress+=step;
            cont.updateProgress(progress);

            if (success) {
                cont.printOutput("Download success.", false);
            }else {
                boolean ok = localMod.delete();
                if(ok){
                    cont.printOutput("Local temp file removed",false);
                }
                cont.printOutput("Download Failed.",false);
            }

        }
        cont.printOutput("Patching Finished, ready to launch!",true);
        cont.updateProgress(1.0);
        cont.resetInterface();
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
