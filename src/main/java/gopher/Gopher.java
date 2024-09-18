package gopher;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeParseException;

import gopher.exception.EmptyTaskDescriptionException;
import gopher.exception.InvalidTaskNumberException;
import gopher.exception.InvalidTokenException;
import gopher.exception.MissingTaskNumberException;
import gopher.exception.MissingTokenException;
import gopher.exception.UnknownCommandException;
import gopher.parser.Parser;
import gopher.storage.TaskManager;
import gopher.task.Task;
import gopher.task.TaskList;
import gopher.ui.UI;
import javafx.application.Platform;

/**
 * Represents the chatbot Gopher.
 */
public class Gopher {

    /**
     * TaskList object used by Gopher to track user tasks
     */
    private static TaskList taskList;

    /**
     * Constructor for Gopher class
     */
    public Gopher() {
        TaskManager.initialize();
        assert Files.exists(Paths.get("./task/task.txt"))
                : "Task save file should exist after successful initialization";

        taskList = new TaskList();
    }

    /**
     * Executes the relevant actions when user input exit program command
     *
     * @return response by gopher after successful action
     */
    public static String executeExitCommand() {
        Platform.exit();
        return UI.getExitMessage();
    }

    /**
     * Executes the relevant actions when user input list tasks command
     *
     * @return response by gopher after successful action
     */
    public static String executeListTasksCommand() {
        return UI.getTaskListMessage(taskList);
    }

    /**
     * Executes the relevant actions when user input mark task as done command.
     *
     * @param userInput command input by the user
     * @return response by gopher after successful action
     */
    public static String executeMarkTaskCommand(String userInput) {
        try {
            int[] taskNumbers = Parser.parseMarkCommand(userInput);
            taskList.markAsDone(taskNumbers);
            StringBuilder message = new StringBuilder();
            for (int taskNumber : taskNumbers) {
                message.append(UI.getMarkAsDoneMessage(taskList.getTask(taskNumber)));
            }
            return message.toString();
        } catch (MissingTaskNumberException | InvalidTaskNumberException e) {
            return e.getMessage();
        } finally {
            taskList.save();
        }
    }

    /**
     * Executes the relevant actions when user input mark task as not done command.
     *
     * @param userInput command input by the user
     * @return response by gopher after successful action
     */
    public static String executeUnmarkTaskCommand(String userInput) {
        try {
            int[] taskNumbers = Parser.parseUnmarkCommand(userInput);
            taskList.markAsUndone(taskNumbers);
            StringBuilder message = new StringBuilder();
            for (int taskNumber : taskNumbers) {
                message.append(UI.getMarkAsUndoneMessage(taskList.getTask(taskNumber)));
            }
            return message.toString();
        } catch (MissingTaskNumberException | InvalidTaskNumberException e) {
            return e.getMessage();
        } finally {
            taskList.save();
        }
    }

    /**
     * Executes the relevant actions when user input delete task command.
     *
     * @param userInput command input by the user
     * @return response by gopher after successful action
     */
    public static String executeDeleteTaskCommand(String userInput) {
        try {
            int[] taskNumbers = Parser.parseDeleteCommand(userInput);
            StringBuilder message = new StringBuilder();
            for (int taskNumber : taskNumbers) {
                message.append(UI.getDeleteTaskMessage(taskList.getTask(taskNumber)));
            }
            taskList.delete(taskNumbers);
            return message.toString();
        } catch (MissingTaskNumberException | InvalidTaskNumberException e) {
            return e.getMessage();
        } finally {
            taskList.save();
        }
    }

    /**
     * Executes the relevant actions when user input find task command.
     *
     * @param userInput command input by the user
     * @return response by gopher after successful action
     */
    public static String executeFindTaskCommand(String userInput) {
        String keyword = Parser.parseFindCommand(userInput);
        TaskList matchedTasks = taskList.find(keyword);
        return UI.getMatchedTasksMessage(matchedTasks);
    }

    /**
     * Executes the relevant actions when user input create task command.
     *
     * @param userInput command input by the user
     * @return response by gopher after successful action
     */
    public static String executeCreateTaskCommand(String userInput)
            throws UnknownCommandException {
        try {
            Task task = Task.of(userInput);
            taskList.add(task);
            return UI.getAddTaskMessage(task);
        } catch (DateTimeParseException e) {
            return UI.getInvalidDateWarning();
        } catch (EmptyTaskDescriptionException | MissingTokenException
                 | InvalidTokenException e) {
            return e.getMessage();
        }
    }

    /**
     * Executes the relevant actions when user input update task command.
     *
     * @param userInput command input by the user
     * @return response by gopher after successful action
     */
    public static String executeUpdateTaskCommand(String userInput) {
        try {
            String[] tokens = userInput.split(" ");
            return taskList.update(tokens);
        } catch (DateTimeParseException e) {
            return UI.getInvalidDateWarning();
        } catch (InvalidTokenException | MissingTaskNumberException
                 | InvalidTaskNumberException e) {
            return e.getMessage();
        }
    }

    /**
     * Start the main event loop.
     */
    public static String getResponse(String userInput)
            throws UnknownCommandException {
        if (userInput.equalsIgnoreCase("bye")) {
            return executeExitCommand();
        } else if (userInput.equalsIgnoreCase("list")) {
            return executeListTasksCommand();
        } else if (userInput.toLowerCase().startsWith("mark")) {
            return executeMarkTaskCommand(userInput);
        } else if (userInput.toLowerCase().startsWith("unmark")) {
            return executeUnmarkTaskCommand(userInput);
        } else if (userInput.toLowerCase().startsWith("delete")) {
            return executeDeleteTaskCommand(userInput);
        } else if (userInput.toLowerCase().startsWith("find")) {
            return executeFindTaskCommand(userInput);
        } else if (Parser.isValidTaskType(userInput.split(" ")[0])) {
            return executeCreateTaskCommand(userInput);
        } else if (userInput.toLowerCase().startsWith("update")) {
            return executeUpdateTaskCommand(userInput);
        } else {
            throw new UnknownCommandException(userInput);
        }
    }
}
