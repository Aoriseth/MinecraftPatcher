package Patcher;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * Created by lenna on 4/13/2017.
 * loads and saves configurations files
 */
class Loader {
    private Controller parent;
    private File installLocation;
    private File settings;

    Loader(Controller controller) {
        parent = controller;
        try {
            installLocation = new File(new File(Controller.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            parent.printOutput("Failed to determine install location",true);
        }
        settings = new File(installLocation.getAbsolutePath()+"\\settings.ini");

    }

    void saveToFile(String data, String LocalDirectory) {
        Properties prop = new Properties();
        prop.put("InstallLocation","C://");
        prop.put("ServerAddress",data);

        File file = new File(installLocation+"\\settings.ini");
        file.getParentFile().mkdirs();
        PrintWriter printWriter = null;
        parent.printOutput("Trying to write",true);
        try {
            printWriter = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("error writing");
        }
        if (printWriter!=null){
            prop.list(printWriter);
            printWriter.close();
        }


    }

    public String getPath() {
        if(settings.exists()){
            Properties prop = new Properties();
            try {
                prop.load(new FileInputStream(settings));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return prop.getProperty("InstallLocation");

        }
        return installLocation.getAbsolutePath();
    }

    public String getServer() {
        return "cockx.me/mods";
    }
}