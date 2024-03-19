package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.module.ModuleCode;
import seedu.address.model.person.Person;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

    /**
     * Returns an unmodifiable view of the modules list.
     * This list will not contain any duplicate modules.
     */
    ObservableList<ModuleCode> getModuleList();

    /**
     * Checks if the address book contains the specified module code.
     *
     * @param moduleCode The module code to check.
     * @return {@code true} if the address book contains the specified module code, {@code false} otherwise.
     */
    boolean hasModule(ModuleCode moduleCode);

    /**
     * Adds a module code to the address book.
     *
     * @param moduleCode The module code to add.
     */
    void addModule(ModuleCode moduleCode);
}
