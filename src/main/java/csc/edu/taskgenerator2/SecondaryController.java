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

public class SecondaryController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private TableView<Task> taskView;

    @FXML
    private TableColumn<Task, String> taskColumn;

    @FXML
    private TableColumn<Task, LocalDate> dateColumn;

    @FXML
    private TableColumn<Task, String> statusColumn;

    @FXML
    private TableColumn<Task, String> deleteColumn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.taskView.setItems(PrimaryController.taskList);
        this.taskColumn.setCellValueFactory(task -> task.getValue().getTask());
        this.dateColumn.setCellValueFactory(date -> date.getValue().getDate());
        this.statusColumn.setCellValueFactory(status -> status.getValue().getStatus());

        // Replace this block with the new one
        ObservableList<String> statusOptionsList = FXCollections.observableArrayList("New Task", "In Progress", "Completed");
        this.statusColumn.setCellFactory(ComboBoxTableCell.forTableColumn(statusOptionsList));

        // ← This is the replacement block
        statusColumn.setOnEditCommit(e -> {
            Task editedTask = e.getRowValue();
            editedTask.getStatus().set(e.getNewValue());
            editedTask.writeToFile(); // Persist change to CSV immediately
        });

        deleteColumn.setCellFactory(a -> new TableCell<Task, String>() {
            Button deleteButton = new Button("Delete");
            {
                deleteButton.setOnAction(b -> {
                    Task task = getTableView().getItems().get(getIndex());
                    getTableView().getItems().remove(task);
                    Task.deleteTaskFromFile(task.getDate(), task.getTask());
                });
            }

            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });

        this.taskView.setEditable(true);

        this.backButton.setOnAction(e -> {
            try {
                App.setRoot("primary");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
}