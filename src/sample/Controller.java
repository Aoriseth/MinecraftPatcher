package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controller {

    private Downloader con = new Downloader();

    @FXML
    private Button launchButton;
    @FXML
    private TextField installLoc;

    @FXML
    private void launchHandle(){
        con.getLocalFiles(installLoc.getText()+"mods\\");
    }

    @FXML
    protected void initialize(){
        System.out.println("Application loaded");
        installLoc.setText(System.getenv("Appdata")+"\\.minecraft\\");

    }

}
