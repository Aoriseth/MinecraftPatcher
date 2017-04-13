package Patcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * Created by lenna on 4/13/2017.
 * loads and saves configurations files
 */
class Loader {
    private Controller parent;
    private File installLocation;

    Loader(Controller controller) {
        parent = controller;
        try {
            installLocation = new File(Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            parent.printOutput("Failed to determine install location",true);
        }

    }

    void saveToFile(String data, String LocalDirectory) {
        Properties prop = new Properties();
        prop.put("InstallLocation","C://");
        prop.put("ServerAddress",data);

        File file = new File("C:\\Users\\lenna\\OneDrive\\Documents\\store\\settings.ini");
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
}