package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODULECODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTORIALCLASS;

import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.Model;
import seedu.address.model.module.ModuleCode;
import seedu.address.model.module.TutorialClass;
import seedu.address.model.module.TutorialTeam;
import seedu.address.model.module.TutorialTeamName;
import seedu.address.model.person.Person;

/**
 * Views all teams in a specified tutorial class of a module to the user.
 */
public class ViewTeamCommand extends Command {

    public static final String COMMAND_WORD = "/view_teams";
    public static final String MESSAGE_EMPTY = "No teams available in %1$s %2$s";
    public static final String MESSAGE_NO_TUTORIAL_CLASSES = "No tutorial classes available in %1$s";
    private static final String MESSAGE_NO_MODULE = "Module not available";
    public static final String MESSAGE_TEAM_NOT_FOUND = "Team with %1$s %2$s not found!";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": View teams of the tutorial class "
        + "based on their name or index. \n"
        + "Parameters: [name/NAME | index/TEAM_INDEX]"
        + PREFIX_MODULECODE + "MODULE CODE "
        + PREFIX_TUTORIALCLASS + "TUTORIAL CLASS "
        + "Example: " + COMMAND_WORD + " "
        + PREFIX_MODULECODE + "CS2103T "
        + PREFIX_TUTORIALCLASS + "T09 ";

    private final Prefix predicateType;
    private final String predicateValue;
    private final ModuleCode moduleCode;
    private final TutorialClass tutorialClass;

    public ViewTeamCommand(Prefix predicateType, String predicateValue, ModuleCode moduleCode,
                           TutorialClass tutorialClass) {
        requireAllNonNull(predicateType, predicateValue, moduleCode, tutorialClass);
        this.predicateType = predicateType;
        this.predicateValue = predicateValue;
        this.moduleCode = moduleCode;
        this.tutorialClass = tutorialClass;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        TutorialTeam teamToView;

        ModuleCode moduleInList = model.findModuleFromList(moduleCode);
        if (moduleInList == null) {
            return new CommandResult(MESSAGE_NO_MODULE);
        }
        if (moduleInList.getTutorialClasses().isEmpty()) {
            return new CommandResult(String.format(MESSAGE_NO_TUTORIAL_CLASSES, moduleInList));
        }
        TutorialClass tutorialInList = model.findTutorialClassFromList(tutorialClass, moduleInList);
        if (tutorialInList.getTeams().isEmpty()) {
            return new CommandResult(String.format(MESSAGE_EMPTY, moduleInList, tutorialInList));
        }
        if (predicateType.equals(PREFIX_NAME)) {
            teamToView = findTeamByName(model, new TutorialTeamName(predicateValue), tutorialInList, moduleInList);
            if (teamToView == null || !tutorialInList.hasTeam(teamToView)) {
                throw new CommandException(String.format(MESSAGE_TEAM_NOT_FOUND, predicateType, predicateValue));
            }
        } else if (predicateType.equals(PREFIX_INDEX)) {
            teamToView = findTeamByIndex(model, Index.fromZeroBased(Integer.parseInt(predicateValue)), tutorialClass,
                moduleInList);
            if (teamToView == null || !tutorialInList.hasTeam(teamToView)) {
                throw new CommandException(String.format(MESSAGE_TEAM_NOT_FOUND, predicateType, predicateValue));
            }
        } else {
            throw new CommandException("Invalid predicate type specified!");
        }

        StringBuilder studentsStringBuilder = new StringBuilder();
        for (Person student : teamToView.getStudents()) {
            studentsStringBuilder.append(student.toStringTwo()).append(", ");
        }
        String studentsString = studentsStringBuilder.toString();
        // Remove the trailing comma and space
        if (studentsString.length() > 2) {
            studentsString = studentsString.substring(0, studentsString.length() - 2);
        }

        return new CommandResult("Team Name: " + teamToView.getTeamName() + ", Team Size: "
            + teamToView.getTeamSize() + ", Students: " + studentsString);
    }

    private TutorialTeam findTeamByName(Model model, TutorialTeamName teamName, TutorialClass tutorialClass,
                                        ModuleCode moduleCode) {
        Predicate<TutorialTeam> namePredicate = tutorialTeam -> tutorialTeam.getTeamName()
            .trim().equalsIgnoreCase(teamName.toString());
        return model.searchTeamByPredicate(namePredicate, tutorialClass, moduleCode);
    }

    private TutorialTeam findTeamByIndex(Model model, Index index, TutorialClass tutorialClass,
                                         ModuleCode moduleCode) throws CommandException {
        TutorialClass tutorialInList = model.findTutorialClassFromList(tutorialClass, moduleCode);
        if (tutorialInList == null) {
            throw new CommandException("No such tutorial class.");
        }
        Predicate<TutorialTeam> indexPredicate = tutorialTeam -> {
            ObservableList<TutorialTeam> filteredList = FXCollections.observableList(tutorialInList.getTeams());
            int teamIndex = filteredList.indexOf(tutorialTeam);
            return teamIndex == index.getZeroBased();
        };
        TutorialTeam team = model.searchTeamByPredicate(indexPredicate, tutorialClass, moduleCode);

        if (team == null) {
            throw new CommandException("Invalid team index. Please provide a valid index within the range.");
        }

        return team;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ViewTeamCommand)) {
            return false;
        }
        ViewTeamCommand command = (ViewTeamCommand) other;
        return moduleCode.equals(command.moduleCode)
            && tutorialClass.equals(command.tutorialClass);
    }
}
