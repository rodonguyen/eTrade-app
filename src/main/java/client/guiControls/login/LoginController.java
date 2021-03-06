package client.guiControls.login;

import client.data.IServerConnection;
import client.data.ServerConnection;
import client.guiControls.Controller;
import client.guiControls.LocalDatabase;
import client.guiControls.MainController;
import client.guiControls.MessageFactory;
import common.Exceptions.InvalidArgumentValueException;
import common.Request;
import common.Response;
import common.dataClasses.IData;
import common.dataClasses.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A controller for the login screen that authenticate the user and redirect to another main scene.
 */
public class LoginController extends Controller {
    @FXML TextField nameTextField;
    @FXML PasswordField passwordField;
    @FXML Label statusLabel;
    @FXML Button exitButton;
    @FXML Button loginButton;

    /**
     * Initialise the session by creating a server connection.
     */
    @FXML
    public void initialize(){
        setUser(null);
        Platform.runLater(() -> {
            if (getServerConnection() == null){
                serverConnection = new ServerConnection();
            }
            try {
                update();
            } catch (InvalidArgumentValueException e) {
                e.printStackTrace();
            }
            startBackgroundThread();
        });
    }

    /**
     * Sends a request to the server, attempt to login with the provided username and password.
     * @param event The event linked with the method (the button click).
     * @throws IOException
     */
    public void attemptLogin(ActionEvent event) throws IOException {
        //TODO: Connect to server to authenticate the user
        String username = nameTextField.getText();
        String password = passwordField.getText();
        User tempUser = new User(username, password);
        tempUser.hashPassword();
        this.setUser(tempUser);
        Response response = this.sendRequest(Request.ActionType.LOGIN, null);
        boolean loginSuccess = response.isAccepted();

        //TODO: Wait for response from server
        if(loginSuccess){
            User currentUser = (User)response.getAttachment();
            this.setUser(currentUser);
            switchToMainScreen(event);
        }
        else{
            reset();
        }
    }

    /**
     * Switch to the main screen, based on the current user.
     * @param event The event triggering the method.
     * @throws IOException
     */
    private void switchToMainScreen(ActionEvent event) throws IOException {
        String resourcePath = "";
        switch(this.getUser().getAccountType()){
            case "user":
                resourcePath = "UserGUI/UserMain.fxml";
                break;

            case "admin":
                resourcePath = "AdminGUI/AdminMain.fxml";
        }

        // Gets the loader
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(resourcePath));

        // Loads the scene & pass current user and connection to main scene
        Parent root = fxmlLoader.load();
        MainController mainController = fxmlLoader.getController();
        mainController.setUser(this.getUser());
        mainController.setServerConnection(this.getServerConnection());
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);

        // Applies css
        String css = this.getClass().getClassLoader().getResource("client.css").toExternalForm();
        scene.getStylesheets().add(css);

        // Show the scene
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * Empties the fields on incorrect password.
     */
    private void reset(){
        nameTextField.clear();
        passwordField.clear();
        statusLabel.setText("Incorrect username or password. Please try again!");
    }

    /**
     * Prompts to exit the program.
     */
    public void exit(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Program");
        alert.setHeaderText("Close the program?");
        alert.setContentText("Do you want to exit?");
        if (alert.showAndWait().get() == ButtonType.OK){
            Stage stage = (Stage)(exitButton.getScene().getWindow());
            stage.close();
            System.out.println("You successfully exit the program");
        }
    }

    @Override
    public void update() throws InvalidArgumentValueException {
        boolean isActive = serverConnection.pingServer();
        if (isActive){
            statusLabel.setText("The server is active...");
            loginButton.setDisable(false);
        }
        else{
            statusLabel.setText("Cannot connect to server...");
            loginButton.setDisable(true);
        }
    }
}