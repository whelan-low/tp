package seedu.address.logic.parser;


import seedu.address.logic.commands.*;
import seedu.address.logic.commands.addstudenttoclasscommands.*;
import seedu.address.logic.parser.exceptions.*;
import seedu.address.model.module.*;
import seedu.address.model.person.*;
import seedu.address.model.tag.*;

import java.util.*;
import java.util.stream.*;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODULECODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TEAMNAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTORIALCLASS;

/**
 * Parses input arguments and creates a new AllocateStudentToTeamCommand object
 */
public class AllocateStudentToTeamCommandParser implements Parser<AllocateStudentToTeamCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * AllocateStudentToTeam and returns an AllocateStudentToTeamCommand object for
     * execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AllocateStudentToTeamCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_STUDENTID, PREFIX_MODULECODE,
                PREFIX_TUTORIALCLASS, PREFIX_TEAMNAME);

        if (!arePrefixesPresent(argMultimap, PREFIX_STUDENTID, PREFIX_MODULECODE,
                PREFIX_TUTORIALCLASS, PREFIX_TEAMNAME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AllocateStudentToTeamCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_STUDENTID, PREFIX_MODULECODE,
                PREFIX_TUTORIALCLASS, PREFIX_TEAMNAME);

        StudentId studentId = ParserUtil.parseStudentId(argMultimap.getValue(PREFIX_STUDENTID).get());
        ModuleCode moduleCode = ParserUtil.parseModuleCode(argMultimap.getValue(PREFIX_MODULECODE).get());
        TutorialClass tutorialClass = ParserUtil.parseTutorialClass(argMultimap.getValue(PREFIX_TUTORIALCLASS).get());
        TutorialTeam tutorialTeam = ParserUtil.parseTutorialTeam(argMultimap.getValue(PREFIX_TEAMNAME).get());

        return new AllocateStudentToTeamCommand(studentId, moduleCode, tutorialClass, tutorialTeam);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
