package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class Controller {


    private Downloader con = new Downloader(this);
    private Launcher launch = new Launcher(this);

    @FXML
    private TextField installLoc;
    @FXML
    private Button launchButton;
    @FXML
    private TabPane tabView;
    @FXML
    private TextArea output;
    @FXML
    private Button patchButton;
    @FXML
    private TextField serverAddress;


    @FXML
    private void launchHandle(){

        launchMinecraft();
    }

    private void launchMinecraft() {
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
    private void patchHandle(){
        con.patch(installLoc.getText(),serverAddress.getText());
    }

    @FXML
    protected void initialize(){
        //set install location to appdata/.minecraft
        installLoc.setText(System.getenv("Appdata")+"\\.minecraft");
    }

    void launchFailed() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Platform.runLater(()->launchButton.setDisable(false));
        Platform.runLater(()->launchButton.setText("Launch"));
    }

    void printOutput(String value, boolean fancy){
        if (fancy) Platform.runLater(()->output.appendText("=== "+value+" ===\n"));
        else Platform.runLater(()->output.appendText(value+"\n"));
    }
}
