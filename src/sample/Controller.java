package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class Controller {

    private Downloader con = new Downloader();
    private Launcher launch = new Launcher();

    @FXML
    private TextField installLoc;

    @FXML
    private void launchHandle(){
        launch.launchMinecraft(installLoc.getText());
    }

    @FXML
    protected void initialize(){
        //set install location to appdata/.minecraft
        installLoc.setText(System.getenv("Appdata")+"\\.minecraft\\");
    }

}
