package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class Controller {

    private Downloader con = new Downloader();

    @FXML
    private TextField installLoc;

    @FXML
    private void launchHandle(){
        launchMinecraft(installLoc.getText());
    }

    private void launchMinecraft(String location) {
        try {
            Runtime.getRuntime().exec("java -jar " + location +"launcher.jar" );
            Platform.exit();
            System.exit(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void initialize(){
        //set install location to appdata/.minecraft
        System.out.println("Application loaded");
        installLoc.setText(System.getenv("Appdata")+"\\.minecraft\\");

    }

}
