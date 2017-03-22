package Patcher;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;

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
    private ProgressBar progressBar;
    @FXML
    private Button openButton;
    @FXML
    private ProgressBar progressBar2;
    @FXML
    private Label launcherLabel;


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
        else printOutput("Please select a correct location.",true);
    }

    @FXML
    private void patchHandle(){
        output.clear();
        Runnable task = ()-> con.patch(installLoc.getText(),serverAddress.getText());
        new Thread(task).start();
    }

    @FXML
    protected void initialize(){
        //set install location to appdata/.minecraft
        installLoc.setText(System.getenv("Appdata")+"\\.minecraft");
        launchButton.setDisable(true);
        Runnable task = ()-> con.patch(installLoc.getText(),serverAddress.getText());
        new Thread(task).start();
    }

    @FXML
    private void openHandle(){
        try {
            Runtime.getRuntime().exec("explorer.exe \"" + installLoc.getText()+"\"");
        } catch (IOException e) {
            e.printStackTrace();
            printOutput("Cannot open chosen directory.",true);
        }
    }

    void launchFailed() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        printOutput("launcher.jar not found, please install minecraft",true);
        Platform.runLater(()->launchButton.setDisable(false));
        Platform.runLater(()->launchButton.setText("Launch"));
    }

    void printOutput(String value, boolean fancy){
        if (fancy) Platform.runLater(()->output.appendText("=== "+value+" ===\n"));
        else Platform.runLater(()->output.appendText(value+"\n"));
        Platform.runLater(()->launcherLabel.setText(value));
    }

    void updateProgress(double value){
        Platform.runLater(()->progressBar.setProgress(value));
        Platform.runLater(()->progressBar2.setProgress(value));
    }

    void resetInterface() {
        Platform.runLater(()->launchButton.setDisable(false));
    }
}
