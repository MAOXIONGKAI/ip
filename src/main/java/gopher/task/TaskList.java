package gopher.task;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gopher.storage.TaskManager;
import gopher.ui.UI;

/**
 * Represents TaskList that tracks user input tasks.
 * Supports functionalities such as:
 *     1. Add/Delete task.
 *     2. Mark/Unmark task as done.
 *     3. Display tasks as list.
 *     4. Find tasks based on keywords.
 *     5. Get task by number.
 *     6. Get size of the list.
 */
public class TaskList {
    /**
     * Tasks tracked by the TaskList
     */
    private ArrayList<Task> tasks;

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds the given task to the task list.
     * Triggers the TaskManager to update the local saved tasks.
     * Prints the add message on the UI.
     *
     * @param task task to be added
     */
    public void add(Task task) {
        this.tasks.add(task);
        TaskManager.saveTasks(tasks);
        UI.printAddTaskMessage(this, task);
    }

    /**
     * Deletes the task with the given task number from the task list.
     * Triggers the TaskManager to update the local saved tasks.
     * Prints the delete message on the UI.
     *
     * @param taskNumber number of the task to be deleted
     */
    public void delete(int taskNumber) {
        Task task = getTask(taskNumber);
        tasks.remove(taskNumber - 1);
        TaskManager.saveTasks(tasks);
        UI.printDeleteTaskMessage(this, task);
    }

    /**
     * Finds tasks whose description contains the given keyword.
     *
     * @param keyword keyword used to search for tasks
     * @return TaskList object containing all the matching tasks
     */
    public TaskList find(String keyword) {
        ArrayList<Task> matchedTasks = new ArrayList<>();
        Pattern keywordPattern = Pattern.compile(keyword,
                Pattern.CASE_INSENSITIVE);

        for (Task task : tasks) {
            Matcher keywordMatcher = keywordPattern.matcher(task.toString());
            if (keywordMatcher.find()) {
                matchedTasks.add(task);
            }
        }

        return new TaskList(matchedTasks);
    }

    /**
     * Marks the task with the given task number as done.
     * Triggers the TaskManager to update the local saved tasks.
     * Prints the mark as done message on the UI.
     *
     * @param taskNumber number of the task to be marked as done
     */
    public void markAsDone(int taskNumber) {
        Task task = getTask(taskNumber);
        task.markAsDone();
        TaskManager.saveTasks(tasks);
        UI.printMarkAsDoneMessage(task);
    }

    /**
     * Marks the task with the given task number as not done.
     * Triggers the TaskManager to update the local saved tasks.
     * Prints the mark as not done message on the UI.
     *
     * @param taskNumber number of the task to be marked as not done
     */
    public void markAsUndone(int taskNumber) {
        Task task = getTask(taskNumber);
        task.markAsNotDone();
        TaskManager.saveTasks(tasks);
        UI.printMarkAsUndoneMessage(task);
    }

    /**
     * Gets the task with the given task number.
     *
     * @param taskNumber number of the task wanted
     * @return task with the specified number
     */
    public Task getTask(int taskNumber) {
        return this.tasks.get(taskNumber - 1);
    }

    /**
     * Gets the number of tasks on the task list.
     *
     * @return number of tasks on the list
     */
    public int getSize() {
        return this.tasks.size();
    }

    @Override
    public String toString() {
        StringBuilder list = new StringBuilder();
        for (int i = 1; i <= tasks.size(); i++) {
            String message = String.format("%d. %s",
                    i,
                    tasks.get(i - 1));
            list.append(message);
            if (i <= tasks.size() - 1) {
                list.append("\n");
            }
        }
        return list.toString();
    }
}
