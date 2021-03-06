package client.guiControls.adminMain.profileController;

import client.guiControls.DisplayController;
import client.guiControls.adminMain.AdminLocalDatabase;
import common.Exceptions.InvalidArgumentValueException;
import common.Request;

import common.dataClasses.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * A controller for the profile page
 */
public class AdminProfileController extends DisplayController {
    @FXML
    private Label fullNameLabel;
    @FXML
    private Label usernameLabel;

    @FXML
    private Label roleLabel;
    @FXML
    private TextField currentPasswordField;
    @FXML
    private TextField newPasswordField;
    @FXML
    private TextField confirmNewPasswordField;

    private User user;

    /**
     * Updates with database and displays user's information
     */
    @Override
    public void update() throws InvalidArgumentValueException{
        AdminLocalDatabase localDatabase = (AdminLocalDatabase) controller.getDatabase();
        user = controller.getUser();
        fullNameLabel.setText(user.getFullName());
        usernameLabel.setText(user.getUsername());
        roleLabel.setText(user.getAccountType());

    }

    /**
     * Changes the user's password.
     */
    public void changePassword() throws InvalidArgumentValueException {
        User tempUser = new User(user.getId(), user.getFullName(), user.getUsername(),
                currentPasswordField.getText(), user.getAccountType(), user.getUnitId());
        tempUser.setPassword(tempUser.getPassword());
        tempUser.hashPassword();
        // Checks if the password entered is correct
        // and the new password is confirmed
        if (tempUser.equals(user) && confirmPassword()){
            user.setPassword(newPasswordField.getText());
            user.hashPassword();
            controller.sendRequest(Request.ActionType.UPDATE, user, Request.ObjectType.USER);
            currentPasswordField.clear();
            newPasswordField.clear();
            confirmNewPasswordField.clear();
        }
    }

    /**
     * Checks if the password in the confirm new password text field is the same
     * as the new password text field
     * @return
     */
    public boolean confirmPassword(){
        if (newPasswordField.getText().equals(confirmNewPasswordField.getText())){
            confirmNewPasswordField.setStyle("-fx-control-inner-background: lightgreen");
        }
        else{
            confirmNewPasswordField.setStyle("-fx-control-inner-background: lightcoral ");
        }
        return newPasswordField.getText().equals(confirmNewPasswordField.getText());
    }
}

