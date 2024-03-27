package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODULECODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TEAMNAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTORIALCLASS;

import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.module.ModuleCode;
import seedu.address.model.module.TutorialClass;
import seedu.address.model.module.TutorialTeam;
import seedu.address.model.person.StudentId;

/**
 * Allocates a student to a team in a tutorial Class in TAHelper.
 */
public class AllocateStudentToTeamCommand extends Command {

    public static final String COMMAND_WORD = "/allocate_team";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Allocates a student a team in the tutorial class.\n"
            + "Parameters: "
            + PREFIX_STUDENTID + "STUDENT ID "
            + PREFIX_MODULECODE + "MODULE CODE "
            + PREFIX_TUTORIALCLASS + "TUTORIAL CLASS "
            + PREFIX_TEAMNAME + "TEAM NAME"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_STUDENTID + "A1234567L "
            + PREFIX_MODULECODE + "CS2101 "
            + PREFIX_TUTORIALCLASS + "T01 "
            + PREFIX_TEAMNAME + "Team 1 ";

    public static final String MESSAGE_SUCCESS = "Allocate student to team: ";
    public static final String MESSAGE_TEAM_DOES_NOT_EXIST = "Team does not exist in tutorial class";
    public static final String MESSAGE_DUPLICATE_PERSON_IN_TEAM = "This person already exists in a team"
            + " in the tutorial class!";

    public static final String MESSAGE_CLASS_DOES_NOT_EXIST = "Tutorial class does not exist in module";
    public static final String MESSAGE_TEAM_SIZE_EXCEEDED = "Max team size reached";

    private final StudentId studentId;
    private final ModuleCode moduleCode;
    private final TutorialClass tutorialClass;
    private final TutorialTeam tutorialTeam;

    /**
     * Creates an AllocateStudentToTeam object.
     */
    public AllocateStudentToTeamCommand(StudentId studentId, ModuleCode moduleCode,
                                        TutorialClass tutorialClass, TutorialTeam tutorialTeam) {
        CollectionUtil.requireAllNonNull(studentId, moduleCode, tutorialClass, tutorialTeam);
        this.studentId = studentId;
        this.moduleCode = moduleCode;
        this.tutorialClass = tutorialClass;
        this.tutorialTeam = tutorialTeam;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.findTutorialClassFromList(tutorialClass, moduleCode) == null) {
            throw new CommandException(MESSAGE_CLASS_DOES_NOT_EXIST);
        }

        ModuleCode module = model.findModuleFromList(moduleCode);
        TutorialClass tutClass = model.findTutorialClassFromList(tutorialClass, module);

        if (!model.hasTeamInTutorial(tutClass, tutorialTeam)) {
            throw new CommandException(MESSAGE_TEAM_DOES_NOT_EXIST);
        }

        TutorialTeam tutTeam = model.getTutorialTeam(tutClass, tutorialTeam);
        if (tutTeam == null) {
            throw new CommandException(MESSAGE_TEAM_DOES_NOT_EXIST);
        }

        if (model.isStudentInAnyTeam(studentId, tutClass)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON_IN_TEAM);
        }

        if (model.hasTeamSizeExceeded(tutTeam)) {
            throw new CommandException(MESSAGE_TEAM_SIZE_EXCEEDED);
        }

        model.allocateStudentToTeam(studentId, tutTeam);

        return new CommandResult(String.format(MESSAGE_SUCCESS));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AllocateStudentToTeamCommand)) {
            return false;
        }

        AllocateStudentToTeamCommand otherAllocateCommand = (AllocateStudentToTeamCommand) other;
        return this.studentId.equals(otherAllocateCommand.studentId)
                && this.moduleCode.equals(otherAllocateCommand.moduleCode)
                && this.tutorialClass.equals(otherAllocateCommand.tutorialClass)
                && this.tutorialTeam.equals(otherAllocateCommand.tutorialTeam);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("studentId", studentId)
                .add("moduleCode", moduleCode)
                .add("tutorialClass", tutorialClass)
                .add("tutorialTeam", tutorialTeam)
                .toString();
    }
}
