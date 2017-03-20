package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;

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
    private void dirHandle(){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose Minecraft install location");
        File location = chooser.showDialog(installLoc.getScene().getWindow());
        installLoc.setText(location.getAbsolutePath());
    }

    @FXML
    protected void initialize(){
        //set install location to appdata/.minecraft
        installLoc.setText(System.getenv("Appdata")+"\\.minecraft");
    }

}
