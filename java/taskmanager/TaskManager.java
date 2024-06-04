package taskmanager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private List<Task> tasks;
    private static final String FILE_NAME = "tasks.txt";

    public TaskManager() {
        this.tasks = new ArrayList<>();
        loadTasks();
    }

    public void addTask(Task task) {
        tasks.add(task);
        saveTasks();
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        saveTasks();
    }

    public void editTask(Task oldTask, String newName, String newDescription) {
        oldTask.setName(newName);
        oldTask.setDescription(newDescription);
        saveTasks();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<Task> getCompletedTasks() {
        List<Task> completedTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.isCompleted()) {
                completedTasks.add(task);
            }
        }
        return completedTasks;
    }

    public List<Task> getPendingTasks() {
        List<Task> pendingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (!task.isCompleted()) {
                pendingTasks.add(task);
            }
        }
        return pendingTasks;
    }

    void saveTasks() {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(FILE_NAME)))) {
            for (Task task : tasks) {
                writer.println(task.getName() + ";" + task.getDescription() + ";" + task.isCompleted());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTasks() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length == 3) {
                        String name = parts[0];
                        String description = parts[1];
                        boolean completed = Boolean.parseBoolean(parts[2]);
                        Task task = new Task(name, description);
                        if (completed) {
                            task.complete();
                        }
                        tasks.add(task);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
