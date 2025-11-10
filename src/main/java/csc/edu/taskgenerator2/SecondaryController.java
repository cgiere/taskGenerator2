/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.collections.FXCollections
 *  javafx.collections.ObservableList
 *  javafx.fxml.FXML
 *  javafx.fxml.Initializable
 *  javafx.scene.Node
 *  javafx.scene.control.Button
 *  javafx.scene.control.TableCell
 *  javafx.scene.control.TableColumn
 *  javafx.scene.control.TableView
 *  javafx.scene.control.cell.ComboBoxTableCell
 */
package csc.edu.taskgenerator2;

import csc.edu.taskgenerator2.App;
import csc.edu.taskgenerator2.PrimaryController;
import csc.edu.taskgenerator2.Task;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;

public class SecondaryController
implements Initializable {
    @FXML
    Button backButton;
    @FXML
    TableView<Task> taskView;
    @FXML
    TableColumn<Task, String> taskColumn;
    @FXML
    TableColumn<Task, LocalDate> dateColumn;
    @FXML
    TableColumn<Task, String> statusColumn;
    @FXML
    TableColumn<Task, String> deleteColumn;

    public void initialize(URL url, ResourceBundle rb) {
        this.taskView.setItems(PrimaryController.taskList);
        this.taskColumn.setCellValueFactory(task -> ((Task)task.getValue()).getTask());
        this.dateColumn.setCellValueFactory(date -> ((Task)date.getValue()).getDate());
        this.statusColumn.setCellValueFactory(status -> ((Task)status.getValue()).getStatus());
        ObservableList statusOptionsList = FXCollections.observableArrayList((Object[])new String[]{"New Task", "In Progress", "Completed"});
        this.statusColumn.setCellFactory(ComboBoxTableCell.forTableColumn((ObservableList)statusOptionsList));
        this.statusColumn.setOnEditCommit(e -> {
            Task newTask = (Task)e.getRowValue();
            Task oldTask = (Task)e.getRowValue();
            oldTask.getStatus().set((Object)((String)e.getOldValue()));
            Task.deleteTaskFromFile(oldTask.getDate(), oldTask.getTask());
            newTask.getStatus().set((Object)((String)e.getNewValue()));
            newTask.writeToFile();
        });
        this.taskView.setEditable(true);
        this.deleteColumn.setCellFactory(a -> new TableCell<Task, String>(){
            Button deleteButton = new Button("Delete");
            {
                this.deleteButton.setOnAction(b -> {
                    Task task = (Task)SecondaryController.this.taskView.getItems().get(this.getIndex());
                    this.getTableView().getItems().remove((Object)task);
                    Task.deleteTaskFromFile(task.getDate(), task.getTask());
                });
            }

            protected void updateItem(String item, boolean empty) {
                super.updateItem((Object)item, empty);
                if (empty) {
                    this.setGraphic(null);
                } else {
                    this.setGraphic((Node)this.deleteButton);
                }
            }
        });
        this.backButton.setOnAction(e -> {
            try {
                App.setRoot("primary");
            }
            catch (IOException ex) {
                ex.getStackTrace();
            }
        });
    }
}
