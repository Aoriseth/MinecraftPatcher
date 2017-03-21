package Patcher;

import javafx.application.Platform;

import java.io.File;
import java.io.IOException;

/**
 * Created by lennart on 3/20/2017.
 * This class will handle launching minecraft and related functions
 */


class Launcher {
    private Controller contr;

    public Launcher(Controller parent) {
        contr = parent;
    }

    void launchMinecraft(String location) {
        File minecraftLauncher = new File(location + "\\launcher.jar");
        if (minecraftLauncher.exists()) {
            try {
                Runtime.getRuntime().exec("java -jar " + location + "\\launcher.jar");
                Thread.sleep(1000);
                Platform.exit();
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("interrupted");
            }
        } else contr.launchFailed();
    }
}
