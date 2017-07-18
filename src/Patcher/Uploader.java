package Patcher;

import jdk.internal.util.xml.impl.Input;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import sun.net.ftp.FtpClient;

import java.io.*;
import java.util.ArrayList;

class Uploader {

    private final Controller cont;
    private FTPClient ftp;
    private String serverFolder = "/var/ftp/mods/";

    Uploader(Controller parent) {
        cont=parent;
    }


    void login(String link, String user, String pass) {
        String[] uri = link.split("/",2);
        String serverAddress = uri[0];
        cont.printOutput("Trying to connect with username: "+user,true);
        ftp = ftpConnect(serverAddress,user,pass);

    }

    private FTPClient ftpConnect(String serverAddress, String user, String pass) {
        ftp = new FTPClient();
        try {
            ftp.connect(serverAddress);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        ftp.enterLocalPassiveMode();

        try {
            boolean success = ftp.login(user,pass);
            if(success){
                cont.printOutput("Connected successfully",true);
                cont.uploadConnectSuccess();
            }else{
                cont.printOutput("Cannot connect, please check username and password ",true);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return ftp;
    }

    private File[] getLocalFiles(String dir) {
        File localFolder = new File(dir+"\\mods");
        return localFolder.listFiles();
    }

    private FTPFile[] getServerFiles(String folder) {
        try {
            return ftp.listFiles(folder);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }




    void upload(String text) {
        File[] localFiles = getLocalFiles(text);
        FTPFile[] serverFiles = getServerFiles(serverFolder);

        final ArrayList<String> excessMods = getMissing(localFiles,serverFiles);
        ArrayList<String> missingMods = getExcess(localFiles,serverFiles);

        for (String mod:excessMods){
            cont.printOutput("This mod will be removed from server: "+mod,false);
            try {
                ftp.deleteFile(serverFolder+mod);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (String mod:missingMods){
            cont.printOutput("This mod will be uploaded to server: "+mod,false);
            try {
                ftp.setFileType(FTP.BINARY_FILE_TYPE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            File localFile = new File(text+"/mods/"+mod);
            InputStream inputStream = null;
            try {
                ftp.changeWorkingDirectory(serverFolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream = new FileInputStream(localFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            boolean success = false;
            try {
                success = ftp.storeFile(mod,inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (success){
                cont.printOutput("File "+ mod+" successfully uploaded",false);
            }
            try {
                if(inputStream!=null){
                    inputStream.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        cont.printOutput("Files successfully synced to server",true);



//        cont.printOutput("Trying to sync mods with server",false);
//        try {
//            ftp.setFileType(FTP.BINARY_FILE_TYPE);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        File firstLocalFile = new File(text+"/mods/test.jar");
//        InputStream inputStream = null;
//        String remote = null;
//        try {
//            ftp.changeWorkingDirectory("/var/ftp/mods/");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            inputStream = new FileInputStream(firstLocalFile);
//            remote = "test.jar";
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//
//        }
//        try {
//            boolean success = ftp.storeFile(remote,inputStream);
//            cont.printOutput(ftp.printWorkingDirectory(),false);
////            boolean test = ftp.sendSiteCommand("chmod 744 mods/*");
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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

}
