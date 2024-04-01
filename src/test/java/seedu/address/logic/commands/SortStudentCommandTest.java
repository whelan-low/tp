package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.sortstudentcommands.SortStudentCommand.MESSAGE_INVALID_PARAMETER;
import static seedu.address.logic.commands.sortstudentcommands.SortStudentCommand.MESSAGE_SORT_STUDENT_SUCCESS;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.GEORGE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Comparator;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.sortstudentcommands.SortStudentCommand;
import seedu.address.logic.commands.sortstudentcommands.SortStudentParameter;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code SortStudentCommand}.
 */
public class SortStudentCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        SortStudentCommand firstCommand = new SortStudentCommand(SortStudentParameter.NAME);
        SortStudentCommand secondCommand = new SortStudentCommand(SortStudentParameter.STUDENTID);

        // same object -> returns true
        assertTrue(firstCommand.equals(firstCommand));

        // same values -> returns true
        SortStudentCommand sortFirstCommandCopy = new SortStudentCommand(SortStudentParameter.NAME);
        assertTrue(firstCommand.equals(sortFirstCommandCopy));

        // different types -> returns false
        assertFalse(firstCommand.equals(1));

        // null -> returns false
        assertFalse(firstCommand.equals(null));

        // different sortBy -> returns false
        assertFalse(firstCommand.equals(secondCommand));
    }

    @Test
    public void execute_validParameter_success() {
        String expectedMessage = MESSAGE_SORT_STUDENT_SUCCESS;

        // sort by name
        Comparator<Person> comparator = Comparator.comparing(p -> p.getName().toString());
        SortStudentCommand command = new SortStudentCommand(SortStudentParameter.NAME);

        expectedModel.getSortedPersonList(comparator);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);

        // sort by id
        comparator = Comparator.comparing(p -> p.getStudentId().toString());
        command = new SortStudentCommand(SortStudentParameter.STUDENTID);

        expectedModel.getSortedPersonList(comparator);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);

        // sort by email
        comparator = Comparator.comparing(p -> p.getEmail().toString());
        command = new SortStudentCommand(SortStudentParameter.STUDENTID);

        expectedModel.getSortedPersonList(comparator);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_sortByName_correctSorting() {

        // add persons in non sorted order
        model = new ModelManager();
        model.addPerson(BENSON);
        model.addPerson(DANIEL);
        model.addPerson(ALICE);
        model.addPerson(GEORGE);

        // add persons in sorted order
        expectedModel = new ModelManager();
        expectedModel.addPerson(ALICE);
        expectedModel.addPerson(BENSON);
        expectedModel.addPerson(DANIEL);
        expectedModel.addPerson(GEORGE);

        try {
            // sort by name
            SortStudentCommand command = new SortStudentCommand(SortStudentParameter.NAME);
            command.execute(model);
            ObservableList<Person> expectedPersonList = expectedModel.getAddressBook().getPersonList();
            ObservableList<Person> actualPersonList = model.getAddressBook().getSortedPersonList();
            assertEquals(expectedPersonList, actualPersonList);
        } catch (CommandException e) {
            fail();
        }
    }

    @Test
    public void execute_sortByEmail_correctSorting() {

        // setup new model for testing correctness of sort
        Person firstEmail = new PersonBuilder().withEmail("a@example.co").withStudentId("A1234567Z").build();
        Person secondEmail = new PersonBuilder().withEmail("a@example.com").withStudentId("A1234567F").build();
        Person thirdEmail = new PersonBuilder().withEmail("aaa@example.com").withStudentId("A1234567A").build();
        Person fourthEmail = new PersonBuilder().withEmail("b@anotheremail.com").withStudentId("A1234567G").build();
        Person fifthEmail = new PersonBuilder().withEmail("b@example.com").withStudentId("A1234567L").build();

        // add persons in non sorted order
        model = new ModelManager();
        model.addPerson(fifthEmail);
        model.addPerson(secondEmail);
        model.addPerson(fourthEmail);
        model.addPerson(firstEmail);
        model.addPerson(thirdEmail);

        // add persons in sorted order
        expectedModel = new ModelManager();
        expectedModel.addPerson(firstEmail);
        expectedModel.addPerson(secondEmail);
        expectedModel.addPerson(thirdEmail);
        expectedModel.addPerson(fourthEmail);
        expectedModel.addPerson(fifthEmail);

        try {
            // sort by email
            SortStudentCommand command = new SortStudentCommand(SortStudentParameter.EMAIL);
            command.execute(model);
            ObservableList<Person> expectedPersonList = expectedModel.getAddressBook().getPersonList();
            ObservableList<Person> actualPersonList = model.getAddressBook().getSortedPersonList();
            assertEquals(expectedPersonList, actualPersonList);
        } catch (CommandException e) {
            fail();
        }
    }

    @Test
    public void execute_sortByStudentId_correctSorting() {

        // setup new model for testing correctness of sort
        Person firstId = new PersonBuilder().withStudentId("A1123456A").withEmail("a@example.com").build();
        Person secondId = new PersonBuilder().withStudentId("A1234566B").withEmail("h@example.com").build();
        Person thirdId = new PersonBuilder().withStudentId("A1234567A").withEmail("k@example.com").build();
        Person fourthId = new PersonBuilder().withStudentId("A1234567B").withEmail("g@example.com").build();
        Person fifthId = new PersonBuilder().withStudentId("A2976237K").withEmail("f@example.com").build();

        // add persons in non sorted order
        model = new ModelManager();
        model.addPerson(firstId);
        model.addPerson(secondId);
        model.addPerson(thirdId);
        model.addPerson(fourthId);
        model.addPerson(fifthId);

        // add persons in sorted order
        expectedModel = new ModelManager();
        expectedModel.addPerson(firstId);
        expectedModel.addPerson(secondId);
        expectedModel.addPerson(thirdId);
        expectedModel.addPerson(fourthId);
        expectedModel.addPerson(fifthId);

        try {
            // sort by id
            SortStudentCommand command = new SortStudentCommand(SortStudentParameter.STUDENTID);
            command.execute(model);
            ObservableList<Person> expectedPersonList = expectedModel.getAddressBook().getPersonList();
            ObservableList<Person> actualPersonList = model.getAddressBook().getSortedPersonList();
            assertEquals(expectedPersonList, actualPersonList);
        } catch (CommandException e) {
            fail();
        }
    }

    @Test
    public void execute_invalidParameters_failure() {
        // empty parameter
        SortStudentCommand command = new SortStudentCommand(SortStudentParameter.EMPTY);
        assertCommandFailure(command, model,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortStudentCommand.MESSAGE_USAGE));

        // invalid parameter
        // testing of invalid strings is covered by SortStudentCommandParserTest
        command = new SortStudentCommand(SortStudentParameter.INVALID);
        assertCommandFailure(command, model, MESSAGE_INVALID_PARAMETER);
    }

    @Test
    public void toStringMethod() {
        SortStudentCommand command = new SortStudentCommand(SortStudentParameter.NAME);
        String expected = SortStudentCommand.class.getCanonicalName() + "{sortBy=NAME}";
        assertEquals(expected, command.toString());
    }
}
