package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller {

    private Downloader con = new Downloader();

    @FXML
    private Button launchButton;

    @FXML
    private void launchHandle(){
        con.getLocalFiles("mods");
    }

}
