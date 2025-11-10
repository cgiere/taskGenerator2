/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.ObjectProperty
 *  javafx.beans.property.SimpleObjectProperty
 *  javafx.beans.property.SimpleStringProperty
 *  javafx.beans.property.StringProperty
 */
package csc.edu.taskgenerator2;

import csc.edu.taskgenerator2.PrimaryController;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Task {
    private ObjectProperty<LocalDate> date;
    private StringProperty task;
    private StringProperty status;

    public Task() {
    }

    public Task(ObjectProperty<LocalDate> date, StringProperty task) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String textDate = ((LocalDate)date.getValue()).format(formatter);
        LocalDate parsedDate = LocalDate.parse(textDate, formatter);
        this.date = new SimpleObjectProperty((Object)parsedDate);
        this.task = new SimpleStringProperty(task.getValue());
        this.status = new SimpleStringProperty("New Task");
    }

    public Task(ObjectProperty<LocalDate> date, StringProperty task, StringProperty status) {
        this.date = date;
        this.task = task;
        this.status = status;
    }

    public ObjectProperty<LocalDate> getDate() {
        return this.date;
    }

    public StringProperty getTask() {
        return this.task;
    }

    public StringProperty getStatus() {
        return this.status;
    }

    public void writeToFile() {
        String filePath = "src/main/java/csc/edu/taskgenerator2/tasklist.csv";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            StringBuilder sb = new StringBuilder();
            boolean updated = false;

            // Read existing tasks
            if (Files.exists(Paths.get(filePath))) {
                try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",", 3);
                        if (parts.length != 3) continue;

                        LocalDate fileDate = LocalDate.parse(parts[0], formatter);
                        String fileTask = parts[1];

                        if (fileDate.equals(this.date.get()) && fileTask.equals(this.task.get())) {
                            // Update status if task exists
                            sb.append(this.date.get().toString())
                            .append(",")
                            .append(this.task.get())
                            .append(",")
                            .append(this.status.get())
                            .append("\n");
                            updated = true;
                        } else {
                            sb.append(line).append("\n");
                        }
                    }
                }
            }

            // Append if new task
            if (!updated) {
                sb.append(this.date.get().toString())
                .append(",")
                .append(this.task.get())
                .append(",")
                .append(this.status.get())
                .append("\n");
            }

            // Overwrite file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write(sb.toString());
            }

        } catch (IOException e) {
            System.out.println("Error writing tasks to file: " + e.getMessage());
        }
    }


    public void readFile() {
        String filePath = "src/main/java/csc/edu/taskgenerator2/tasklist.csv";
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length != 3) continue;

                LocalDate datePart = LocalDate.parse(parts[0], formatter);
                String taskPart = parts[1];
                String statusPart = parts[2];

                // Check if task already exists to avoid duplicates
                boolean exists = PrimaryController.taskList.stream().anyMatch(
                    t -> t.getDate().get().equals(datePart) && t.getTask().get().equals(taskPart)
                );
                if (exists) continue;

                Task newTask = new Task(
                    new SimpleObjectProperty<>(datePart),
                    new SimpleStringProperty(taskPart),
                    new SimpleStringProperty(statusPart)
                );

                PrimaryController.taskList.add(newTask);
            }
        } catch (IOException | DateTimeParseException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    
    public static void deleteTaskFromFile(ObjectProperty<LocalDate> date, StringProperty taskDescription) {
        String filePath = "src/main/java/csc/edu/taskgenerator2/tasklist.csv";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (!Files.exists(Paths.get(filePath))) return;

        try {
            StringBuilder sb = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",", 3);
                    if (parts.length != 3) continue;

                    LocalDate fileDate = LocalDate.parse(parts[0], formatter);
                    String fileTask = parts[1];

                    // Keep lines that are NOT the one to delete
                    if (!(fileDate.equals(date.get()) && fileTask.equals(taskDescription.get()))) {
                        sb.append(line).append("\n");
                    }
                }
            }

            // Overwrite CSV without the deleted task
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write(sb.toString());
            }

        } catch (IOException e) {
            System.out.println("Error deleting task from file: " + e.getMessage());
        }
    }
}
