package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.module.*;
import seedu.address.model.person.*;

/**
 * Jackson-friendly version of {@link TutorialTeam}.
 */
public class JsonAdaptedTutorialTeam {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "team name is missing!";
    private final String teamName;
    private final int teamSize;
    private final List<JsonAdaptedPerson> students = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedTutorialTeam} with the given
     * {@code teamName}.
     */
    @JsonCreator
    public JsonAdaptedTutorialTeam(@JsonProperty("teamName") String teamName,
            @JsonProperty("teamSize") int teamSize,
            @JsonProperty("students") List<JsonAdaptedPerson> students) {
        this.teamName = teamName;
        if (students != null) {
            this.students.addAll(students);
        }
        this.teamSize = teamSize;
    }

    /**
     * Converts a given {@code TutorialTeam} into this class for Jackson use.
     */
    public JsonAdaptedTutorialTeam(TutorialTeam source) {
        this.teamName = source.getTeamName();
        this.teamSize = source.getTeamSize();
        students.addAll(source.getStudents().stream().map(JsonAdaptedPerson::new).collect(Collectors.toList()));
    }

    public String getTeamName() {
        return teamName;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public List<JsonAdaptedPerson> getStudents() {
        return this.students;
    }

    /**
     * Converts this Jackson-friendly adapted tutorial team object into the model's
     * {@code TutorialTeam} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in
     *                               the adapted tutorial team.
     */
    public TutorialTeam toModelType() throws IllegalValueException {
        final ArrayList<Person> listOfStudents = new ArrayList<>();
        if (teamName == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, teamName));
        }
        if (!TutorialTeam.isValidTeamName(teamName)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }

        if (!TutorialTeam.isValidSize(teamSize)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        for (JsonAdaptedPerson student : students) {
            listOfStudents.add(student.toModelType());
        }
        return new TutorialTeam(teamName, listOfStudents, teamSize);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof JsonAdaptedTutorialTeam)) {
            return false;
        }

        JsonAdaptedTutorialTeam otherTutorialTeam = (JsonAdaptedTutorialTeam) other;
        return teamName.equals(otherTutorialTeam.teamName) && students.equals(otherTutorialTeam.students)
                && teamSize == otherTutorialTeam.teamSize;
    }
}
