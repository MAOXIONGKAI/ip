package gopher.task;

import gopher.exception.EmptyTaskDescriptionException;
import gopher.exception.MissingTokenException;
import gopher.exception.UnknownCommandException;
import gopher.parser.Parser;

/**
 * Represents the task that the user needs to track.
 */
public abstract class Task {

    /** Name of the Task */
    protected String name;

    /** Whether the task is done */
    protected boolean isDone;

    /**
     * Constructor for abstract class Task.
     * Only to be used by the subclasses.
     *
     * @param name name of the task
     */
    public Task(String name) {
        this.name = name;
        this.isDone = false;
    }

    /**
     * Factory method which creates different types of task
     * depending on the input command.
     *
     * @param command task creation command
     * @return task of the correct type
     * @throws UnknownCommandException if command is not recognized
     * @throws EmptyTaskDescriptionException if task description is empty
     * @throws MissingTokenException if tokens is missing for the given task type
     */
    public static Task of(String command) throws UnknownCommandException,
            EmptyTaskDescriptionException, MissingTokenException {
        return Parser.parseCreateTaskCommand(command);
    }

    /**
     * Gets the save file message representation of this task.
     *
     * @return save string format of the task
     */
    public abstract String getSaveMessage();

    /**
     * Marks the given task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks the given task as not done.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Gets the stats icon of the task.
     *
     * @return stats icon of the task
     */
    protected String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    @Override
    public String toString() {
        return String.format("[%s] %s",
                getStatusIcon(),
                this.name);
    }
}
