package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class Controller {

    private Downloader con = new Downloader();
    private Launcher launch = new Launcher();

    @FXML
    private TextField installLoc;
    @FXML
    private Button launchButton;

    @FXML
    private void launchHandle(){
        launchButton.setDisable(true);
        launchButton.setText("Launching...");
        Runnable task1 = () -> launch.launchMinecraft(installLoc.getText());
        new Thread(task1).start();
    }

    @FXML
    private void dirHandle(){
        DirectoryChooser chooser = new DirectoryChooser();
        File chosen = new File(installLoc.getText());
        if(chosen.exists()) chooser.setInitialDirectory(new File(installLoc.getText()));

        chooser.setTitle("Choose Minecraft install location");
        File location = chooser.showDialog(installLoc.getScene().getWindow());

        if (location!=null) installLoc.setText(location.getAbsolutePath());
        else System.out.println("Please select a correct location.");
    }

    @FXML
    protected void initialize(){
        //set install location to appdata/.minecraft
        installLoc.setText(System.getenv("Appdata")+"\\.minecraft");
    }

}
