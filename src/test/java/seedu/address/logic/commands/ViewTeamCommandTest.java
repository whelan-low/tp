package seedu.address.logic.commands;

import org.junit.jupiter.api.Test;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.module.ModuleCode;
import seedu.address.model.module.ModuleCode;
import seedu.address.model.module.TutorialClass;
import seedu.address.model.module.TutorialTeam;
import seedu.address.model.module.TutorialTeamName;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import java.util.HashSet;

public class ViewTeamCommandTest {

    @Test
    public void execute_validInput_teamFound() throws Exception {
        Model model = new ModelManager(); // Create a basic model
        ModuleCode moduleCode = new ModuleCode("CS2104");
        TutorialClass tutorialClass = new TutorialClass("T09");
        moduleCode.addTutorialClass(tutorialClass);
        model.addModule(moduleCode);

        TutorialTeamName teamName = new TutorialTeamName("Team A");
        TutorialTeam team = new TutorialTeam(teamName.fullName, 2);
        Person john = new Person(new Name("John"), new Email("john@example.com"), new StudentId("A1234561Z"), new HashSet<>());
        Person alice = new Person(new Name("Alice"), new Email("alice@example.com"), new StudentId("A1234562Z"), new HashSet<>());
        team.addStudent(john);
        team.addStudent(alice);
        tutorialClass.addTeam(team);

        ViewTeamCommand command = new ViewTeamCommand(PREFIX_NAME, "Team A", moduleCode, tutorialClass);
        CommandResult result = command.execute(model);

        assertEquals("Team Name: Team A, Team Size: 2, Students: name=John, email=john@example.com, "
            + "stuId=A1234561Z, name=Alice, email=alice@example.com, stuId=A1234562Z", result.getFeedbackToUser());
    }

    @Test
    public void execute_invalidTeamName_exceptionThrown() {
        Model model = new ModelManager(); // Create a basic model
        ModuleCode moduleCode = new ModuleCode("CS2105");
        TutorialClass tutorialClass = new TutorialClass("T06");
        moduleCode.addTutorialClass(tutorialClass);
        model.addModule(moduleCode);

        TutorialTeamName teamName = new TutorialTeamName("Team A");
        TutorialTeam team = new TutorialTeam(teamName.fullName, 2);
        Person john = new Person(new Name("John"), new Email("john@example.com"), new StudentId("A1234561Z"), new HashSet<>());
        Person alice = new Person(new Name("Alice"), new Email("alice@example.com"), new StudentId("A1234562Z"), new HashSet<>());
        tutorialClass.addTeam(team);

        ViewTeamCommand command = new ViewTeamCommand(PREFIX_NAME, "Team B", moduleCode, tutorialClass);

        assertThrows(CommandException.class, () -> command.execute(model));
    }
}
