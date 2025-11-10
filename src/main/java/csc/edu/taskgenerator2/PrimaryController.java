/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.ObjectProperty
 *  javafx.beans.property.SimpleObjectProperty
 *  javafx.beans.property.SimpleStringProperty
 *  javafx.beans.property.StringProperty
 *  javafx.collections.FXCollections
 *  javafx.collections.ObservableList
 *  javafx.fxml.FXML
 *  javafx.fxml.Initializable
 *  javafx.scene.control.Alert
 *  javafx.scene.control.Alert$AlertType
 *  javafx.scene.control.Button
 *  javafx.scene.control.DatePicker
 *  javafx.scene.control.TextField
 */
package csc.edu.taskgenerator2;

import csc.edu.taskgenerator2.App;
import csc.edu.taskgenerator2.Task;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class PrimaryController
implements Initializable {
    @FXML
    DatePicker dateField;
    @FXML
    TextField taskField;
    @FXML
    Button taskAdderBtn;
    @FXML
    Button taskListBtn;
    static ObservableList<Task> taskList = FXCollections.observableArrayList();

    public void initialize(URL url, ResourceBundle rb) {
        taskList.clear();
        Task primaryTask = new Task();
        primaryTask.readFile();
        this.taskAdderBtn.setOnAction(e -> {
            if (this.dateField.getValue() != null && !((LocalDate)this.dateField.getValue()).isBefore(LocalDate.now()) && this.taskField.getText() != null) {
                this.addTaskData((LocalDate)this.dateField.getValue(), this.taskField.getText());
            } else if (this.dateField.getValue() == null || this.taskField.getText() == null) {
                this.showAlert();
            }
        });
        this.taskListBtn.setOnAction(e -> {
            try {
                App.setRoot("secondary");
            }
            catch (IOException ex) {
                ex.getStackTrace();
            }
        });
    }

    public void addTaskData(LocalDate date, String item) {
        SimpleObjectProperty day = new SimpleObjectProperty((Object)date);
        SimpleStringProperty task = new SimpleStringProperty(item);
        SimpleStringProperty status = new SimpleStringProperty("New Task");
        Task newTask = new Task((ObjectProperty<LocalDate>)day, (StringProperty)task, (StringProperty)status);
        taskList.add((Object)newTask);
        newTask.writeToFile();
        this.dateField.setValue(null);
        this.taskField.clear();
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Please fill in all fields.");
        alert.showAndWait();
    }
}
