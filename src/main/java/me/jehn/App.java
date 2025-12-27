package me.jehn;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

import static me.jehn.SQL.createDB;


public class App extends Application {

    private static Scene scene;




    @Override
    public void start(Stage stage) throws IOException {
        createDB();
        scene = new Scene(loadFXML("primary"));
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image("file:icon.png"));
        stage.show();
    }




    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }




    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }




    public static void main(String[] args) {
        launch();
    }

}