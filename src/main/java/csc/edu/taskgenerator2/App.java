/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.application.Application
 *  javafx.fxml.FXMLLoader
 *  javafx.scene.Parent
 *  javafx.scene.Scene
 *  javafx.stage.Stage
 */
package csc.edu.taskgenerator2;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App
extends Application {
    private static Scene scene;

    public void start(Stage stage) throws IOException {
        scene = new Scene(App.loadFXML("primary"), 600.0, 350.0);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(App.loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return (Parent)fxmlLoader.load();
    }

    public static void main(String[] args) {
        App.launch((String[])new String[0]);
    }
}
