package taskmanager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TaskManagerGUI {
    private JFrame frame;
    private JTextField taskNameField;
    private JTextField taskDescriptionField;
    private DefaultListModel<Task> pendingTaskListModel;
    private DefaultListModel<Task> completedTaskListModel;
    private JList<Task> pendingTaskList;
    private JList<Task> completedTaskList;
    private TaskManager taskManager;

    public TaskManagerGUI() {
        taskManager = new TaskManager();
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Gerenciador de Tarefas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.setBackground(new Color(220, 220, 220));

        JLabel nameLabel = new JLabel("Nome da Tarefa:");
        nameLabel.setForeground(new Color(0, 102, 204));
        inputPanel.add(nameLabel);
        taskNameField = new JTextField();
        inputPanel.add(taskNameField);

        JLabel descriptionLabel = new JLabel("Descrição:");
        descriptionLabel.setForeground(new Color(0, 102, 204));
        inputPanel.add(descriptionLabel);
        taskDescriptionField = new JTextField();
        inputPanel.add(taskDescriptionField);

        JButton addButton = new JButton("Adicionar Tarefa");
        addButton.setBackground(new Color(0, 204, 102));
        addButton.setForeground(Color.WHITE);
        inputPanel.add(addButton);

        frame.add(inputPanel, BorderLayout.NORTH);

        pendingTaskListModel = new DefaultListModel<>();
        pendingTaskList = new JList<>(pendingTaskListModel);
        pendingTaskList.setBackground(new Color(255, 255, 204));

        completedTaskListModel = new DefaultListModel<>();
        completedTaskList = new JList<>(completedTaskListModel);
        completedTaskList.setBackground(new Color(204, 255, 204));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Tarefas Pendentes", new JScrollPane(pendingTaskList));
        tabbedPane.addTab("Tarefas Concluídas", new JScrollPane(completedTaskList));

        frame.add(tabbedPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBackground(new Color(220, 220, 220));

        JButton completeButton = new JButton("Concluir Tarefa");
        completeButton.setBackground(new Color(0, 204, 102));
        completeButton.setForeground(Color.WHITE);
        JButton editButton = new JButton("Editar Tarefa");
        editButton.setBackground(new Color(0, 102, 204));
        editButton.setForeground(Color.WHITE);
        JButton deleteButton = new JButton("Excluir Tarefa");
        deleteButton.setBackground(new Color(204, 0, 0));
        deleteButton.setForeground(Color.WHITE);
        JButton deleteCompletedButton = new JButton("Excluir Tarefa Concluída");
        deleteCompletedButton.setBackground(new Color(204, 0, 0));
        deleteCompletedButton.setForeground(Color.WHITE);

        buttonPanel.add(completeButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(deleteCompletedButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });

        completeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                completeTask();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editTask();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTask();
            }
        });

        deleteCompletedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCompletedTask();
            }
        });

        loadTasksToListModels();

        frame.setVisible(true);
    }

    private void addTask() {
        String name = taskNameField.getText();
        String description = taskDescriptionField.getText();
        if (!name.isEmpty() && !description.isEmpty()) {
            Task task = new Task(name, description);
            taskManager.addTask(task);
            pendingTaskListModel.addElement(task);
            taskNameField.setText("");
            taskDescriptionField.setText("");
        } else {
            JOptionPane.showMessageDialog(frame, "Por favor, insira nome e descrição");
        }
    }

    private void completeTask() {
        Task selectedTask = pendingTaskList.getSelectedValue();
        if (selectedTask != null) {
            selectedTask.complete();
            pendingTaskListModel.removeElement(selectedTask);
            completedTaskListModel.addElement(selectedTask);
            taskManager.saveTasks();
        } else {
            JOptionPane.showMessageDialog(frame, "Por favor, selecione uma tarefa pendente para concluir");
        }
    }

    private void editTask() {
        Task selectedTask = pendingTaskList.getSelectedValue();
        if (selectedTask != null) {
            String newName = JOptionPane.showInputDialog(frame, "Novo Nome:", selectedTask.getName());
            String newDescription = JOptionPane.showInputDialog(frame, "Nova Descrição:", selectedTask.getDescription());
            if (newName != null && newDescription != null && !newName.isEmpty() && !newDescription.isEmpty()) {
                taskManager.editTask(selectedTask, newName, newDescription);
                pendingTaskList.repaint();
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Por favor, selecione uma tarefa pendente para editar");
        }
    }

    private void deleteTask() {
        Task selectedTask = pendingTaskList.getSelectedValue();
        if (selectedTask != null) {
            pendingTaskListModel.removeElement(selectedTask);
            taskManager.removeTask(selectedTask);
        } else {
            JOptionPane.showMessageDialog(frame, "Por favor, selecione uma tarefa pendente para excluir");
        }
    }

    private void deleteCompletedTask() {
        Task selectedTask = completedTaskList.getSelectedValue();
        if (selectedTask != null) {
            completedTaskListModel.removeElement(selectedTask);
            taskManager.removeTask(selectedTask);
        } else {
            JOptionPane.showMessageDialog(frame, "Por favor, selecione uma tarefa concluída para excluir");
        }
    }

    private void loadTasksToListModels() {
        for (Task task : taskManager.getPendingTasks()) {
            pendingTaskListModel.addElement(task);
        }
        for (Task task : taskManager.getCompletedTasks()) {
            completedTaskListModel.addElement(task);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TaskManagerGUI();
            }
        });
    }
}
