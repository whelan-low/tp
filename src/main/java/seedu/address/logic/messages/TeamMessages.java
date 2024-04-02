package seedu.address.logic.messages;

/**
 * Class that stores messages regarding tutorial teams
 */
public class TeamMessages {
    public static final String MESSAGE_DUPLICATE_PERSON_IN_TEAM = "This person already exists in a team"
            + " in the tutorial class %s!";
    public static final String MESSAGE_TEAM_SIZE_EXCEEDED = "Max team size of %d reached";
    public static final String MESSAGE_TEAM_DOES_NOT_EXIST = "Team %s does not exist in tutorial class %s";
    public static final String MESSAGE_STUDENT_NOT_FOUND_IN_TEAM = "%s is not in team %s";

    public static final String MESSAGE_DELETE_STUDENT_FROM_TEAM_SUCCESS = "Deleted %s from %s %s, Team %s";
}