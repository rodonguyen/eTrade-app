package client.guiControls;

import client.data.IServerConnection;
import client.guiControls.adminMain.AdminLocalDatabase;
import common.Request;
import common.Response;
import common.dataClasses.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The main controller acts as the local storage of data, containing the current User and is able to connect to a server
 * connection class.
 * // TODO: Needs redesign & refactor & documentation.
 */
public class MainController {
    protected ILocalDatabase localDatabase;

    /**
     * The server connection
     */
    private IServerConnection serverConnection;

    /**
     * The current user
     */
    private User user;

    /**
     * Sets the user that is currently using the application.
     * @param user The current user.
     */
    public void setUser(User user){
        this.user = user;
    }

    /**
     * Sets the server connection that connects the controller to the server.
     * @param serverConnection The server connection.
     */
    public void setServerConnection(IServerConnection serverConnection){
        this.serverConnection = serverConnection;
    }

    /**
     * Returns the current server connection.
     * @return The current server connection.
     */
    public IServerConnection getServerConnection(){
        return serverConnection;
    }

    /**
     * Asks the server connection to send a request to the server.
     * @param request The request to be sent.
     */
    public Response sendRequest(Request request){
        return serverConnection.sendRequest(request);
    }

    /**
     * Returns the current user.
     * @return The current user.
     */
    public User getUser(){
        return user;
    }

    /**
     * Logs the current user out of the session.
     * @param event The event triggering the method.
     * @throws IOException
     */
    public void logOut(ActionEvent event) throws IOException {
        // Sets the loader
        Parent root = FXMLLoader.load(getClass().getResource("../login/Login.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);

        // Applies CSS
        String css = this.getClass().getResource("../client.css").toExternalForm();
        scene.getStylesheets().add(css);

        // Shows the scene
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * Returns the local database for the admin.
     * @return The local database for the admin.
     */
    public ILocalDatabase getDatabase(){
        return localDatabase;
    }
}