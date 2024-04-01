package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MODULE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_STUDENT_ID_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TEAM_NAME;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TEAM_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TEAM_NAME_NEW;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TUTORIAL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.deletestudentfromteamcommands.DeleteStudentFromTeamByEmailCommand;
import seedu.address.logic.commands.deletestudentfromteamcommands.DeleteStudentFromTeamByIdCommand;
import seedu.address.logic.commands.deletestudentfromteamcommands.DeleteStudentFromTeamByIndexCommand;
import seedu.address.logic.messages.TeamMessages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.module.ModuleCode;
import seedu.address.model.module.TutorialClass;
import seedu.address.model.module.TutorialTeam;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class DeleteStudentFromTeamCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private TutorialClass tutorialClass;
    private Person validPerson;
    private Person validOtherPerson;
    private ModuleCode newModule;
    private TutorialTeam newTeam;
    private TutorialTeam tutTeam;

    @BeforeEach
    public void setUp() {
        validPerson = new PersonBuilder(AMY).build();
        validOtherPerson = new PersonBuilder(ALICE)
                .withStudentId(VALID_STUDENT_ID_BOB).withEmail(VALID_EMAIL_BOB).build();
        newModule = new ModuleCode(VALID_MODULE_AMY);
        model.addModule(newModule);
        model.addPerson(validPerson);
        model.addPerson(validOtherPerson);
        TutorialClass newTutorialClass = new TutorialClass(VALID_TUTORIAL_AMY);
        newModule.addTutorialClass(newTutorialClass);
        tutorialClass = newTutorialClass;
        newTeam = new TutorialTeam(VALID_TEAM_NAME, 1);
        tutTeam = new TutorialTeam(VALID_TEAM_NAME_BOB, 3);
        tutorialClass.addTeam(tutTeam);
        tutorialClass.addTeam(newTeam);
    }

    @Test
    public void invalidDeletionFromTeam_tutorialTeamNotExist_failure() {
        TutorialTeam team = new TutorialTeam(VALID_TEAM_NAME_NEW);

        DeleteStudentFromTeamByIdCommand deleteStudentFromTeamByIdCommand = new
                DeleteStudentFromTeamByIdCommand(validPerson.getStudentId(), newModule, tutorialClass,
                team);
        DeleteStudentFromTeamByEmailCommand allocateStudentToTeamByEmailCommand = new
                DeleteStudentFromTeamByEmailCommand(validPerson.getEmail(), newModule, tutorialClass,
                team);
        assertCommandFailure(deleteStudentFromTeamByIdCommand, model,
                String.format(TeamMessages.MESSAGE_TEAM_DOES_NOT_EXIST, team, tutorialClass));
        assertCommandFailure(allocateStudentToTeamByEmailCommand, model,
                String.format(TeamMessages.MESSAGE_TEAM_DOES_NOT_EXIST, team, tutorialClass));
    }

    @Test
    public void toString_test() {
        tutorialClass.addStudent(validPerson);
        DeleteStudentFromTeamByIdCommand deleteStudentFromTeamByIdCommand =
                new DeleteStudentFromTeamByIdCommand(validPerson.getStudentId(), newModule, tutorialClass, newTeam);
        DeleteStudentFromTeamByEmailCommand deleteOtherStudentFromTeamByEmailCommand =
                new DeleteStudentFromTeamByEmailCommand(validOtherPerson.getEmail(),
                        newModule, tutorialClass, newTeam);
        DeleteStudentFromTeamByIndexCommand allocateStudentToTeamByIndexCommand =
                new DeleteStudentFromTeamByIndexCommand(Index.fromZeroBased(0),
                        newModule, tutorialClass, newTeam);
        assertEquals(deleteOtherStudentFromTeamByEmailCommand.toString(),
                deleteOtherStudentFromTeamByEmailCommand.toString());
        assertEquals(deleteStudentFromTeamByIdCommand.toString(),
                deleteStudentFromTeamByIdCommand.toString());
        assertEquals(allocateStudentToTeamByIndexCommand.toString(),
                allocateStudentToTeamByIndexCommand.toString());

    }
}
