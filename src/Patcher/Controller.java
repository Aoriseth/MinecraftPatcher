package Patcher;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

@SuppressWarnings("WeakerAccess")
public class Controller {


    private final Downloader con = new Downloader(this);
    private final Launcher launch = new Launcher(this);
    private final Loader load = new Loader(this);
    private final Uploader upl = new Uploader(this);

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
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button syncButton;
    @FXML
    private Button loginButton;


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

        if (location!=null) {
            installLoc.setText(location.getAbsolutePath());
            load.setPath(location.getAbsolutePath());
        } else printOutput("Please select a correct location.",true);
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
        //installLoc.setText(System.getenv("Appdata")+"\\.minecraft");
        if (load.checkFirstRun()){
            try {
                installLoc.setText(new File(new File(Controller.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent()).getAbsolutePath());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            tabView.getSelectionModel().select(1);
            installLoc.setText(load.getPath());
            serverAddress.setText(load.getServer());
            printOutput("First time running the application: enter a server address",true);
        }else{
            installLoc.setText(load.getPath());
            serverAddress.setText(load.getServer());
            usernameField.setText(load.getUser());
            passwordField.setText(load.getPass());
//            launchButton.setDisable(true);
//            Runnable task = ()-> con.patch(installLoc.getText(),serverAddress.getText());
//            new Thread(task).start();
        }
    }

    @FXML
    private void openHandle(){
        try {
            Desktop.getDesktop().open(new File(installLoc.getText()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void changeServerHandle(){
        load.setServer(serverAddress.getText());
        printOutput("Server address changed to "+serverAddress.getText(),true);
    }

    void launchFailed() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        printOutput("Minecraft not found, please install minecraft",true);
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

    @FXML
    private void tryLogin(){
        output.clear();
        load.setUser(usernameField.getText());
        load.setPass(passwordField.getText());
        upl.login(serverAddress.getText(),usernameField.getText(),passwordField.getText());
    }

    void resetInterface() {
        Platform.runLater(()->launchButton.setDisable(false));
    }

    public void uploadConnectSuccess() {
        usernameField.setDisable(true);
        passwordField.setDisable(true);
        syncButton.setDisable(false);
        loginButton.setDisable(true);
    }

    @FXML
    private void syncToServer(){
        output.clear();
        upl.upload(installLoc.getText());
    }
}
