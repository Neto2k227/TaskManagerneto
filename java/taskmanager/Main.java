package taskmanager;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Estudar Java", "Estudar os fundamentos da linguagem Java");
        Task task2 = new Task("Fazer exercícios", "Praticar exercícios físicos pela manhã");

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        System.out.println("Todas as tarefas:");
        for (Task task : taskManager.getTasks()) {
            System.out.println(task);
        }

        task1.complete();

        System.out.println("\nTarefas concluídas:");
        for (Task task : taskManager.getCompletedTasks()) {
            System.out.println(task);
        }

        System.out.println("\nTarefas pendentes:");
        for (Task task : taskManager.getPendingTasks()) {
            System.out.println(task);
        }
    }
}
