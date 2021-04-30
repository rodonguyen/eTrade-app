package client.guiControls.login;

import client.Main;
import client.data.InvalidUserException;
import server.Features.LoginSystem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML TextField nameTextField;
    @FXML PasswordField passwordField;
    @FXML Label statusLabel;
    @FXML Label userLabel;

    // Attempt to log the user in
    public void attemptLogin(ActionEvent event) throws IOException, InvalidUserException {
        //TODO: Connect to server to authenticate the user
        String username = nameTextField.getText();
        String password = passwordField.getText();
        boolean loginSuccess = Main.mainController.requestLogin(username, password);;

        //TODO: Wait for response from server
        if(loginSuccess){
            switchToMainScreen(event);
        }
        else{
            reset();
        }
    }

    private boolean authenticate(String username, String password){
        LoginSystem newLogin = new LoginSystem();
        boolean loginSuccess = newLogin.login(username, password);
//        boolean loginSuccess = username.equals("admin") || username.equals("root") && password.equals("root");
        return loginSuccess;
    }

    private void switchToMainScreen(ActionEvent event) throws IOException {
        String resourcePath = "";
        switch(Main.mainController.getUser().getAccountType()){
            case "user":
                resourcePath = "../userMain/userMain.fxml";
                break;

            case "admin":
                resourcePath = "../adminMain/adminMain.fxml";
        }

        System.out.println(resourcePath);

        root = FXMLLoader.load(getClass().getResource(resourcePath));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        String css = this.getClass().getResource("../client.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    private void reset(){
        nameTextField.clear();
        passwordField.clear();
        statusLabel.setText("Incorrect username or password. Please try again!");
    }
}