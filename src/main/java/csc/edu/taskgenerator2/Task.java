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
        try {
            Throwable throwable = null;
            Object var2_4 = null;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/csc/edu/taskgenerator2/tasklist.csv", true));){
                writer.write(((LocalDate)this.date.get()).toString() + "," + (String)this.task.get() + "," + (String)this.status.get() + "\n");
                writer.close();
            }
            catch (Throwable throwable2) {
                if (throwable == null) {
                    throwable = throwable2;
                } else if (throwable != throwable2) {
                    throwable.addSuppressed(throwable2);
                }
                throw throwable;
            }
        }
        catch (IOException e) {
            System.out.println("Error writing tasks to file: " + e.getMessage());
        }
    }

    public void readFile() {
        try {
            Throwable throwable = null;
            Object var2_4 = null;
            try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/csc/edu/taskgenerator2/tasklist.csv"));){
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",", 3);
                    if (parts.length != 3) continue;
                    LocalDate datePart = LocalDate.parse(parts[0], DateTimeFormatter.ISO_LOCAL_DATE);
                    SimpleStringProperty taskPart = new SimpleStringProperty(parts[1]);
                    SimpleStringProperty statusPart = new SimpleStringProperty(parts[2]);
                    Task newTask = new Task((ObjectProperty<LocalDate>)new SimpleObjectProperty((Object)datePart), (StringProperty)taskPart, (StringProperty)statusPart);
                    PrimaryController.taskList.add((Object)newTask);
                }
            }
            catch (Throwable throwable2) {
                if (throwable == null) {
                    throwable = throwable2;
                } else if (throwable != throwable2) {
                    throwable.addSuppressed(throwable2);
                }
                throw throwable;
            }
        }
        catch (IOException | DateTimeParseException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void deleteTaskFromFile(ObjectProperty<LocalDate> date, StringProperty taskDescription) {
        block18: {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            try {
                Throwable throwable = null;
                Object var4_7 = null;
                try {
                    BufferedReader reader = new BufferedReader(new FileReader("src/main/java/csc/edu/taskgenerator2/tasklist.csv"));
                    try {
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/csc/edu/taskgenerator2/tasklist_temporary.csv"));){
                            String line;
                            while ((line = reader.readLine()) != null) {
                                String[] parts = line.split(",", 3);
                                if (parts.length != 3) continue;
                                SimpleObjectProperty fileDate = new SimpleObjectProperty((Object)LocalDate.parse(parts[0], formatter));
                                SimpleStringProperty fileTaskDescription = new SimpleStringProperty(parts[1]);
                                if (((LocalDate)fileDate.get()).equals(date.get()) && ((String)fileTaskDescription.get()).equals(taskDescription.get())) continue;
                                writer.write(line);
                                writer.newLine();
                            }
                        }
                        if (reader == null) break block18;
                    }
                    catch (Throwable throwable2) {
                        if (throwable == null) {
                            throwable = throwable2;
                        } else if (throwable != throwable2) {
                            throwable.addSuppressed(throwable2);
                        }
                        if (reader == null) throw throwable;
                        reader.close();
                        throw throwable;
                    }
                    reader.close();
                }
                catch (Throwable throwable3) {
                    if (throwable == null) {
                        throwable = throwable3;
                        throw throwable;
                    }
                    if (throwable == throwable3) throw throwable;
                    throwable.addSuppressed(throwable3);
                    throw throwable;
                }
            }
            catch (IOException e) {
                System.out.println("Error updating file: " + e.getMessage());
            }
        }
        try {
            Files.deleteIfExists(Paths.get("src/main/java/csc/edu/taskgenerator2/tasklist.csv", new String[0]));
            Files.move(Paths.get("src/main/java/csc/edu/taskgenerator2/tasklist_temporary.csv", new String[0]), Paths.get("src/main/java/csc/edu/taskgenerator2/tasklist.csv", new String[0]), StandardCopyOption.REPLACE_EXISTING);
            return;
        }
        catch (IOException e) {
            System.out.println("Error replacing file: " + e.getMessage());
        }
    }
}
