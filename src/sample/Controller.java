package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {

    private Downloader con = new Downloader();

    @FXML
    private Button launchButton;
    @FXML
    private TextField installLoc;

    @FXML
    private void launchHandle(){
        launchMinecraft(installLoc.getText());
    }

    private void launchMinecraft(String location) {
        try {
            Process proc = Runtime.getRuntime().exec("java -jar " + location +"launcher.jar" );
            ((Stage)launchButton.getScene().getWindow()).setIconified(true);
            proc.waitFor();
            ((Stage)launchButton.getScene().getWindow()).setIconified(false);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.print("failed");
        }
    }

    @FXML
    protected void initialize(){
        System.out.println("Application loaded");
        installLoc.setText(System.getenv("Appdata")+"\\.minecraft\\");

    }

}
