package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODULECODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTORIALCLASS;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ListStudentsOfClassCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.module.ModuleCode;
import seedu.address.model.module.TutorialClass;

public class ListStudentsOfClassCommandParserTest {

    private final ListStudentsOfClassCommandParser parser = new ListStudentsOfClassCommandParser();

    @Test
    public void parse_validArgs_returnsListStudentsOfClassCommand() throws ParseException {
        // Example valid input
        String validArgs = " " + PREFIX_MODULECODE + "CS2103T" + " " + PREFIX_TUTORIALCLASS + "T01";
        ListStudentsOfClassCommand expectedCommand = new ListStudentsOfClassCommand(
            new ModuleCode("CS2103T"), new TutorialClass("T01"));
        assertEquals(expectedCommand, parser.parse(validArgs));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // No module code provided
        assertThrows(ParseException.class, () -> parser.parse(PREFIX_TUTORIALCLASS + "T01"));

        // No tutorial class provided
        assertThrows(ParseException.class, () -> parser.parse(PREFIX_MODULECODE + "CS2103T"));

        // Invalid prefix provided
        assertThrows(ParseException.class, () -> parser.parse(" " + PREFIX_MODULECODE + "CS2103T" + " T01"));
    }
}
