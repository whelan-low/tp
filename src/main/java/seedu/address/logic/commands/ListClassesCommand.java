package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_CLASSES;

import seedu.address.model.Model;

/**
 * Command to list all available classes.
 */
public class ListClassesCommand extends Command {
    public static final String COMMAND_WORD = "list_classes";

    public static final String MESSAGE_SUCCESS = "List of all classes available";
    public static final String MESSAGE_NO_CLASSES = "No classes available!";
    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredClassList(PREDICATE_SHOW_ALL_CLASSES);

        // Check if there are no classes available
        if (model.getFilteredClassList().isEmpty()) {
            return new CommandResult(MESSAGE_NO_CLASSES);
        } else {
            return new CommandResult(MESSAGE_SUCCESS);
        }
    }
}
