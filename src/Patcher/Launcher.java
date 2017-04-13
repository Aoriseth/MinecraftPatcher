package Patcher;

import javafx.application.Platform;

import java.io.File;
import java.io.IOException;

/**
 * Created by lennart on 3/20/2017.
 * This class will handle launching minecraft and related functions
 */


class Launcher {
    private final Controller contr;

    public Launcher(Controller parent) {
        contr = parent;
    }

    void launchMinecraft(String location) {
        File minecraftLauncher = new File(location + "\\launcher.jar");
        if (minecraftLauncher.exists()) {
            run(minecraftLauncher);
            return;
        }
        minecraftLauncher = new File(new File(location).getParentFile().getParentFile().getParentFile()+"\\MultiMC.exe");
        if (minecraftLauncher.exists()) {
            winRun(minecraftLauncher);
            return;
        }
        minecraftLauncher = new File(System.getenv("APPDATA")+"\\Curse Client\\Bin\\Curse.exe");
        if (minecraftLauncher.exists()) {
            winRun(minecraftLauncher);
            return;
        }

        contr.launchFailed();
    }

    private void winRun(File minecraftLauncher) {
        try {
            new ProcessBuilder(minecraftLauncher.getAbsolutePath()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        run(minecraftLauncher);
    }

    private void run(File minecraftLauncher) {
        try {
            Runtime.getRuntime().exec("java -jar " + minecraftLauncher.getAbsolutePath());
            Thread.sleep(1000);
            Platform.exit();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("interrupted");
        }
    }
}
