package Patcher;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * Created by lenna on 4/13/2017.
 * loads and saves configurations files
 */
class Loader {
    private final Controller parent;
    private File installLocation;
    private final File settings;

    Loader(Controller controller) {
        parent = controller;
        try {
            installLocation = new File(new File(Controller.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            parent.printOutput("Failed to determine install location", true);
        }
        settings = new File(installLocation.getAbsolutePath() + "\\patcher.properties");

    }

    String getPath() {
        Properties props = new Properties();
        if (!settings.exists()){
            createIni();
            parent.printOutput("Creating properties file",true);
        }
            FileInputStream in = null;
            try {
                in = new FileInputStream(settings);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                props.load(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return props.getProperty("InstallLocation");
    }

    private void createIni() {
        Properties props = new Properties();
        props.put("ServerAddress", "cockx.me/mods/");
        props.put("InstallLocation", settings.getParentFile().getAbsolutePath());
        saveProperties(props);
    }

    private void saveProperties(Properties props) {
        try {
            props.store(new FileOutputStream(settings),"Properties file for minecraftpatcher");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String getServer() {
        Properties props = new Properties();
        if (!settings.exists()){
            createIni();
            parent.printOutput("Creating properties file",true);
        }
        FileInputStream in = null;
        try {
            in = new FileInputStream(settings);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props.getProperty("ServerAddress");
    }

    void setPath(String absolutePath) {
        Properties props = getProperties();
        props.setProperty("InstallLocation",absolutePath);
        saveProperties(props);
    }

    private Properties getProperties() {
        Properties props = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream(settings);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }

    void setServer(String text) {
        Properties props = getProperties();
        props.setProperty("ServerAddress",text);
        saveProperties(props);
    }

    boolean checkFirstRun() {
        return (!settings.exists());
    }
}