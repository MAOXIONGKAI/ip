import java.util.Scanner;

public class Gopher {
    // Initialize the input reader
    private final static Scanner inputReader = new Scanner(System.in);

    // Tasks List Data to handle user input tasks
    private final static String[] taskList = new String[100];
    private static int currentTaskNumber = 0;

    // Common Interface elements for easy reuse
    private final static String gopherLogo = """
              ____             _              \s
             / ___| ___  _ __ | |__   ___ _ __\s
            | |  _ / _ \\| '_ \\| '_ \\ / _ \\ '__|
            | |_| | (_) | |_) | | | |  __/ |  \s
             \\____|\\___/| .__/|_| |_|\\___|_|  \s
                        |_|                   \s
            """;
    private final static String horizontalSeparator = "==================================================";

    private static void greet () {
        System.out.println(horizontalSeparator);
        System.out.println(gopherLogo);
        System.out.println("Hello! I am Gopher.\nWhat can I do for you?\n");
    }

    private static void addTask (String input) {
        taskList[currentTaskNumber] = input;
        currentTaskNumber++;
        System.out.println(horizontalSeparator);
        System.out.println("added: " + input);
        System.out.println(horizontalSeparator + "\n");
    }

    private static void listTasks() {
        System.out.println(horizontalSeparator);
        for (int i = 1; i <= currentTaskNumber; i++) {
            int currentTaskIndex = i - 1;
            String message = String.format("%d. %s", i, taskList[currentTaskIndex]);
            System.out.println(message);
        }
        System.out.println(horizontalSeparator + "\n");
    }

    private static void exit () {
        System.out.println(horizontalSeparator);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(horizontalSeparator + "\n");
    }

    public static void main(String[] args) {
        // Greet the user when starting application
        greet();

        // Start the event loop for responding user input and query
        boolean isRunning = true;
        String userInput;
        while (isRunning) {
            userInput = inputReader.nextLine();
            if (userInput.equalsIgnoreCase("Bye")) {
                isRunning = false;
            } else if (userInput.equalsIgnoreCase("List")) {
                listTasks();
            } else {
                addTask(userInput);
            }
        }

        // Send exit message to user when event loop ends
        exit();
    }
}
