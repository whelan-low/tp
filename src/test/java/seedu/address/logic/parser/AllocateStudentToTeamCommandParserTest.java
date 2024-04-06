package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_MODULE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TEAM_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TUTORIAL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.MODULE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.TEAM_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TUTORIAL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MODULE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TEAM_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TUTORIAL_AMY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODULECODE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.allocatestudenttoteamcommands.AllocateStudentToTeamByEmailCommand;
import seedu.address.logic.commands.allocatestudenttoteamcommands.AllocateStudentToTeamCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.module.ModuleCode;
import seedu.address.model.module.TutorialClass;
import seedu.address.model.module.TutorialTeam;
import seedu.address.model.person.Email;

public class AllocateStudentToTeamCommandParserTest {
    private AllocateStudentToTeamCommandParser parser = new AllocateStudentToTeamCommandParser();

    @Test
    public void parse_allFieldPresent_success() {
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + EMAIL_DESC_AMY + MODULE_DESC_AMY + TUTORIAL_DESC_AMY
                + TEAM_DESC_AMY,
                new AllocateStudentToTeamByEmailCommand(new Email(VALID_EMAIL_AMY), new ModuleCode(VALID_MODULE_AMY),
                        new TutorialClass(VALID_TUTORIAL_AMY), new TutorialTeam(VALID_TEAM_NAME_AMY)));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AllocateStudentToTeamCommand.MESSAGE_USAGE);
        String invalidUserInput = "a";
        assertParseFailure(parser, invalidUserInput, expectedMessage);
    }

    @Test
    public void parse_emailMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AllocateStudentToTeamCommand.MESSAGE_USAGE);
        assertParseFailure(parser, PREAMBLE_WHITESPACE + MODULE_DESC_AMY + TUTORIAL_DESC_AMY
                        + TEAM_DESC_AMY, expectedMessage);
    }

    @Test
    public void parse_moduleMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AllocateStudentToTeamCommand.MESSAGE_USAGE);
        assertParseFailure(parser, PREAMBLE_WHITESPACE + EMAIL_DESC_AMY + TUTORIAL_DESC_AMY
                        + TEAM_DESC_AMY, expectedMessage);
    }

    @Test
    public void parse_tutorialMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AllocateStudentToTeamCommand.MESSAGE_USAGE);
        assertParseFailure(parser, PREAMBLE_WHITESPACE + EMAIL_DESC_AMY + MODULE_DESC_AMY
                        + TEAM_DESC_AMY, expectedMessage);
    }

    @Test
    public void parse_invalidInput_failure() {
        // invalid email
        String invalidEmailInput = PREAMBLE_WHITESPACE + INVALID_EMAIL_DESC + MODULE_DESC_AMY + TUTORIAL_DESC_AMY
                + TEAM_DESC_AMY;
        assertThrows(ParseException.class, () -> parser.parse(invalidEmailInput));

        // invalid module
        String invalidModuleInput = PREAMBLE_WHITESPACE + EMAIL_DESC_AMY + INVALID_MODULE_DESC + TUTORIAL_DESC_AMY
                + TEAM_DESC_AMY;
        assertThrows(ParseException.class, () -> parser.parse(invalidModuleInput));

        // invalid tutorial
        String invalidTutorialInput = PREAMBLE_WHITESPACE + EMAIL_DESC_AMY + MODULE_DESC_AMY
                + INVALID_TUTORIAL_DESC + TEAM_DESC_AMY;
        assertThrows(ParseException.class, () -> parser.parse(invalidTutorialInput));

        // invalid team
        String invalidTeamInput = PREAMBLE_WHITESPACE + EMAIL_DESC_AMY + MODULE_DESC_AMY
                + TUTORIAL_DESC_AMY + INVALID_TEAM_DESC_AMY;
        assertThrows(ParseException.class, () -> parser.parse(invalidTeamInput));
    }

    @Test
    public void parse_prefixRepeat_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AllocateStudentToTeamCommand.MESSAGE_USAGE);
        assertParseFailure(parser, PREAMBLE_WHITESPACE + PREFIX_MODULECODE
                        + EMAIL_DESC_AMY + MODULE_DESC_AMY + TUTORIAL_DESC_AMY + TEAM_DESC_AMY,
                expectedMessage);
    }
}
