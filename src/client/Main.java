package client;

import client.data.MockServerConnection;
import client.data.Session;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class  Main extends Application {

    public static Session session;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.initStyle(StageStyle.UNDECORATED);
        Parent root = FXMLLoader.load(getClass().getResource("guiControls/login/Login.fxml"));
        Scene scene = new Scene(root);

        String css = this.getClass().getResource("guiControls/client.css").toExternalForm();
        scene.getStylesheets().add(css);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        session = new Session();
        session.setServerConnection(new MockServerConnection());
        launch(args);
    }
}
