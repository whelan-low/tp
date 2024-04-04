package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL;
import static seedu.address.logic.commands.CommandTestUtil.VALID_STUDENT_ID;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TEAM_NAME;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TEAM_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TEAM_NAME_NEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.allocatestudenttoteamcommands.AllocateStudentToTeamByEmailCommand;
import seedu.address.logic.commands.allocatestudenttoteamcommands.AllocateStudentToTeamByIndexCommand;
import seedu.address.logic.commands.allocatestudenttoteamcommands.AllocateStudentToTeamByStuIdCommand;
import seedu.address.logic.commands.allocatestudenttoteamcommands.AllocateStudentToTeamCommand;
import seedu.address.logic.messages.PersonMessages;
import seedu.address.logic.messages.TutorialClassMessages;
import seedu.address.logic.messages.TutorialTeamMessages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.module.ModuleCode;
import seedu.address.model.module.TutorialClass;
import seedu.address.model.module.TutorialTeam;
import seedu.address.model.person.Email;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.testutil.ModuleBuilder;

public class AllocateStudentToTeamCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private ModuleCode newModule;
    private TutorialTeam newTeam;
    private TutorialTeam tutTeam;

    @BeforeEach
    public void setUp() {
        // create a new test module and add it to model
        newModule = new ModuleBuilder().build();
        model.addModule(newModule);
        int newTeamSizeTest = 1;
        int tutTeamSizeTest = 3;
        newTeam = new TutorialTeam(VALID_TEAM_NAME, newTeamSizeTest);
        tutTeam = new TutorialTeam(VALID_TEAM_NAME_BOB, tutTeamSizeTest);
        newModule.getTutorialClasses().get(0).addTeam(tutTeam);
        newModule.getTutorialClasses().get(0).addTeam(newTeam);
    }

    @Test
    public void invalidAllocationToTeam_indexNotInSystem_failure() {
        Index index = Index.fromOneBased(10);
        TutorialClass tutorialClass = newModule.getTutorialClasses().get(0);
        AllocateStudentToTeamByIndexCommand allocateStudentToTeamByIndexCommand = new
                AllocateStudentToTeamByIndexCommand(index,
                newModule, tutorialClass, newTeam);
        assertCommandFailure(allocateStudentToTeamByIndexCommand, model,
                String.format(TutorialClassMessages.MESSAGE_PERSON_INDEX_NOT_FOUND_IN_CLASS,
                        index.getOneBased(), tutorialClass));
    }

    @Test
    public void invalidAllocationToTeam_teamSizeExceeded_failure() {
        TutorialClass tutorialClass = newModule.getTutorialClasses().get(0);
        Person validPerson = model.getAddressBook().getPersonList().get(0);
        Person otherValidPerson = model.getAddressBook().getPersonList().get(1);
        tutorialClass.addStudent(validPerson);
        tutorialClass.addStudent(otherValidPerson);
        newTeam.addStudent(validPerson);
        AllocateStudentToTeamByStuIdCommand allocateStudentToTeamByStuIdCommand = new
                AllocateStudentToTeamByStuIdCommand(otherValidPerson.getStudentId(), newModule, tutorialClass, newTeam);
        AllocateStudentToTeamByEmailCommand allocateStudentToTeamByEmailCommand = new
                AllocateStudentToTeamByEmailCommand(otherValidPerson.getEmail(), newModule, tutorialClass, newTeam);
        assertCommandFailure(allocateStudentToTeamByStuIdCommand, model,
                String.format(TutorialTeamMessages.MESSAGE_TEAM_SIZE_EXCEEDED, newTeam.getTeamSize()));
        assertCommandFailure(allocateStudentToTeamByEmailCommand, model,
                String.format(TutorialTeamMessages.MESSAGE_TEAM_SIZE_EXCEEDED, newTeam.getTeamSize()));
    }

    @Test
    public void invalidAllocationToTeam_studentDoesNotExist_failure() {
        TutorialClass tutorialClass = newModule.getTutorialClasses().get(0);
        AllocateStudentToTeamByStuIdCommand allocateStudentToTeamByStuIdCommand = new
                AllocateStudentToTeamByStuIdCommand(new StudentId(VALID_STUDENT_ID), newModule, tutorialClass, newTeam);
        AllocateStudentToTeamByEmailCommand allocateStudentToTeamByEmailCommand = new
                AllocateStudentToTeamByEmailCommand(new Email(VALID_EMAIL), newModule, tutorialClass, newTeam);
        assertCommandFailure(allocateStudentToTeamByStuIdCommand, model,
                String.format(PersonMessages.MESSAGE_PERSON_STUDENT_ID_NOT_FOUND, VALID_STUDENT_ID));
        assertCommandFailure(allocateStudentToTeamByEmailCommand, model,
                String.format(PersonMessages.MESSAGE_PERSON_EMAIL_NOT_FOUND, VALID_EMAIL));
    }

    @Test
    public void invalidAllocationToTeam_tutorialTeamNotExist_failure() {
        TutorialTeam team = new TutorialTeam(VALID_TEAM_NAME_NEW);
        Person validPerson = model.getAddressBook().getPersonList().get(0);
        TutorialClass tutorialClass = newModule.getTutorialClasses().get(0);

        AllocateStudentToTeamByStuIdCommand allocateStudentToTeamByStuIdCommand =
                new AllocateStudentToTeamByStuIdCommand(
                        validPerson.getStudentId(), newModule, tutorialClass, team);
        AllocateStudentToTeamByEmailCommand allocateStudentToTeamByEmailCommand =
                new AllocateStudentToTeamByEmailCommand(
                        validPerson.getEmail(), newModule, tutorialClass, team);
        assertCommandFailure(allocateStudentToTeamByStuIdCommand, model,
                String.format(TutorialTeamMessages.MESSAGE_TEAM_DOES_NOT_EXIST, team, tutorialClass));
        assertCommandFailure(allocateStudentToTeamByEmailCommand, model,
                String.format(TutorialTeamMessages.MESSAGE_TEAM_DOES_NOT_EXIST, team, tutorialClass));
    }

    @Test
    public void invalidAllocationToTeam_studentAlreadyInTeam_failure() {
        Person validPerson = model.getAddressBook().getPersonList().get(0);
        TutorialClass tutorialClass = newModule.getTutorialClasses().get(0);
        tutorialClass.addStudent(validPerson);
        newTeam.addStudent(validPerson);

        // try to allocate again but duplicate entry to team.
        AllocateStudentToTeamByStuIdCommand allocateStudentToTeamByStuIdCommand = new
                AllocateStudentToTeamByStuIdCommand(validPerson.getStudentId(), newModule, tutorialClass,
                tutTeam);
        AllocateStudentToTeamByEmailCommand allocateStudentToTeamByEmailCommand = new
                AllocateStudentToTeamByEmailCommand(validPerson.getEmail(), newModule, tutorialClass,
                tutTeam);
        assertCommandFailure(allocateStudentToTeamByStuIdCommand, model,
                String.format(TutorialTeamMessages.MESSAGE_DUPLICATE_PERSON_IN_TEAM,
                        Messages.format(validPerson), tutorialClass));
        assertCommandFailure(allocateStudentToTeamByEmailCommand, model,
                String.format(TutorialTeamMessages.MESSAGE_DUPLICATE_PERSON_IN_TEAM,
                        Messages.format(validPerson), tutorialClass));
    }

    @Test
    public void validAllocationToTeam_byEmail_success() {
        Person validPerson = model.getAddressBook().getPersonList().get(0);
        TutorialClass tutorialClass = newModule.getTutorialClasses().get(0);
        tutorialClass.addStudent(validPerson);
        AllocateStudentToTeamByStuIdCommand allocateStudentToTeamByStuIdCommand = new
                AllocateStudentToTeamByStuIdCommand(validPerson.getStudentId(),
                newModule, tutorialClass, tutTeam);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.addModule(newModule);

        assertCommandSuccess(allocateStudentToTeamByStuIdCommand, model,
                String.format(AllocateStudentToTeamByIndexCommand.MESSAGE_SUCCESS, tutTeam), expectedModel);
    }

    @Test
    public void validAllocationToTeam_byStudentId_success() {
        Person validPerson = model.getAddressBook().getPersonList().get(0);
        TutorialClass tutorialClass = newModule.getTutorialClasses().get(0);
        tutorialClass.addStudent(validPerson);
        AllocateStudentToTeamByEmailCommand allocateStudentToTeamByEmailCommand = new
                AllocateStudentToTeamByEmailCommand(validPerson.getEmail(),
                newModule, tutorialClass, tutTeam);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.addModule(newModule);
        assertCommandSuccess(allocateStudentToTeamByEmailCommand, model,
                String.format(AllocateStudentToTeamByEmailCommand.MESSAGE_SUCCESS, tutTeam), expectedModel);
    }

    @Test
    public void validAllocationToTeam_indexInSystem_success() {
        Person validPerson = model.getAddressBook().getPersonList().get(0);
        TutorialClass tutorialClass = newModule.getTutorialClasses().get(0);
        tutorialClass.addStudent(validPerson);
        Index index = Index.fromZeroBased(0);
        AllocateStudentToTeamByIndexCommand allocateStudentToTeamByIndexCommand = new
                AllocateStudentToTeamByIndexCommand(index,
                newModule, tutorialClass, newTeam);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.addModule(newModule);
        assertCommandSuccess(allocateStudentToTeamByIndexCommand, model,
                String.format(AllocateStudentToTeamByIndexCommand.MESSAGE_SUCCESS, newTeam), expectedModel);
    }

    @Test
    public void invalidAllocationToTeam_studentNotInTutorialClass_failure() {
        Person validPerson = model.getAddressBook().getPersonList().get(0);
        TutorialClass tutorialClass = newModule.getTutorialClasses().get(0);
        AllocateStudentToTeamByStuIdCommand allocateStudentToTeamByStuIdCommand = new
                AllocateStudentToTeamByStuIdCommand(validPerson.getStudentId(), newModule, tutorialClass, newTeam);
        AllocateStudentToTeamByEmailCommand allocateStudentToTeamByEmailCommand = new
                AllocateStudentToTeamByEmailCommand(validPerson.getEmail(), newModule, tutorialClass, newTeam);
        assertCommandFailure(allocateStudentToTeamByStuIdCommand, model,
                String.format(AllocateStudentToTeamCommand.MESSAGE_STUDENT_NOT_IN_TUTORIAL));
        assertCommandFailure(allocateStudentToTeamByEmailCommand, model,
                String.format(AllocateStudentToTeamCommand.MESSAGE_STUDENT_NOT_IN_TUTORIAL));
    }

    @Test
    public void toString_test() {
        Person validPerson = model.getAddressBook().getPersonList().get(0);
        TutorialClass tutorialClass = newModule.getTutorialClasses().get(0);
        tutorialClass.addStudent(validPerson);
        AllocateStudentToTeamByStuIdCommand allocateStudentToTeamByStuIdCommand =
                new AllocateStudentToTeamByStuIdCommand(
                        validPerson.getStudentId(), newModule, tutorialClass, newTeam);
        AllocateStudentToTeamByEmailCommand allocateOtherStudentToTeamByEmailCommand =
                new AllocateStudentToTeamByEmailCommand(validPerson.getEmail(),
                        newModule, tutorialClass, newTeam);
        AllocateStudentToTeamByIndexCommand allocateStudentToTeamByIndexCommand =
                new AllocateStudentToTeamByIndexCommand(
                        Index.fromZeroBased(0), newModule, tutorialClass, newTeam);
        assertEquals(allocateOtherStudentToTeamByEmailCommand.toString(),
                allocateOtherStudentToTeamByEmailCommand.toString());
        assertEquals(allocateStudentToTeamByStuIdCommand.toString(),
                allocateStudentToTeamByStuIdCommand.toString());
        assertEquals(allocateStudentToTeamByIndexCommand.toString(),
                allocateStudentToTeamByIndexCommand.toString());
        assertNotEquals(allocateStudentToTeamByIndexCommand.toString(), allocateStudentToTeamByStuIdCommand.toString());
        assertNotEquals(allocateStudentToTeamByIndexCommand.toString(),
                allocateOtherStudentToTeamByEmailCommand.toString());
    }

    @Test
    public void equals() {
        Person validPerson = model.getAddressBook().getPersonList().get(0);
        Person validOtherPerson = model.getAddressBook().getPersonList().get(1);
        TutorialClass tutorialClass = newModule.getTutorialClasses().get(0);
        tutorialClass.addStudent(validPerson);
        tutorialClass.addStudent(validOtherPerson);
        // creation of 2 allocation command based on 2 different student ID adding to
        // the same team under
        // the same module and tutorial class.
        AllocateStudentToTeamByStuIdCommand allocateStudentToTeamByStuIdCommand =
                new AllocateStudentToTeamByStuIdCommand(
                        validPerson.getStudentId(), newModule, tutorialClass, newTeam);
        AllocateStudentToTeamByStuIdCommand allocateOtherStudentToTeamByStuIdCommand =
                new AllocateStudentToTeamByStuIdCommand(
                        validOtherPerson.getStudentId(), newModule, tutorialClass, newTeam);

        // same object --> returns true
        assertTrue(allocateStudentToTeamByStuIdCommand.equals(allocateStudentToTeamByStuIdCommand));

        // different type --> returns false
        assertFalse(allocateStudentToTeamByStuIdCommand.equals("hello world"));

        // null --> returns false
        assertFalse(allocateStudentToTeamByStuIdCommand.equals(null));

        // allocation of a different person --> returns false
        assertFalse(allocateStudentToTeamByStuIdCommand.equals(allocateOtherStudentToTeamByStuIdCommand));

        // creation of 2 allocation command based on 2 different student emails adding
        // to the same team under
        // the same module and tutorial class.
        AllocateStudentToTeamByEmailCommand allocateStudentToTeamByEmailCommand =
                new AllocateStudentToTeamByEmailCommand(
                        validPerson.getEmail(), newModule, tutorialClass, newTeam);
        AllocateStudentToTeamByEmailCommand allocateOtherStudentToTeamByEmailCommand =
                new AllocateStudentToTeamByEmailCommand(
                        validOtherPerson.getEmail(), newModule, tutorialClass, newTeam);

        // same object --> returns true
        assertTrue(allocateStudentToTeamByEmailCommand.equals(allocateStudentToTeamByEmailCommand));

        // different type --> returns false
        assertFalse(allocateStudentToTeamByEmailCommand.equals("hello world"));

        // null --> returns false
        assertFalse(allocateStudentToTeamByEmailCommand.equals(null));

        // allocation of a different person --> returns false
        assertFalse(allocateStudentToTeamByEmailCommand.equals(allocateOtherStudentToTeamByEmailCommand));

        AllocateStudentToTeamByIndexCommand allocateOtherStudentToTeamByIndexCommand =
                new AllocateStudentToTeamByIndexCommand(
                        Index.fromZeroBased(1), newModule, tutorialClass, newTeam);

        // same object
        assertTrue(allocateOtherStudentToTeamByIndexCommand.equals(allocateOtherStudentToTeamByIndexCommand));

        // different type --> returns false
        assertFalse(allocateOtherStudentToTeamByIndexCommand.equals("hello world"));

        // null --> returns false
        assertFalse(allocateOtherStudentToTeamByIndexCommand.equals(null));
    }

}
