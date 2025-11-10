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
        // Load tasks once (do NOT clear the list)
        Task primaryTask = new Task();
        primaryTask.readFile();

        taskAdderBtn.setOnAction(e -> {
            LocalDate date = dateField.getValue();
            String taskText = taskField.getText();

            if (date != null && !date.isBefore(LocalDate.now()) && taskText != null && !taskText.isBlank()) {
                addTaskData(date, taskText);
            } else {
                showAlert();
            }
        });

        taskListBtn.setOnAction(e -> {
            try {
                App.setRoot("secondary");  // Switch scene
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    public void addTaskData(LocalDate date, String item) {
        SimpleObjectProperty<LocalDate> day = new SimpleObjectProperty<>(date);
        SimpleStringProperty task = new SimpleStringProperty(item);
        SimpleStringProperty status = new SimpleStringProperty("New Task");

        Task newTask = new Task(day, task, status);
        taskList.add(newTask);  // Add to observable list
        newTask.writeToFile();  // Save to CSV immediately

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
