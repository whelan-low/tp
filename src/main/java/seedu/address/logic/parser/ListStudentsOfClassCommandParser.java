package seedu.address.logic.parser;


import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODULECODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTORIALCLASS;

import java.util.stream.Stream;

import seedu.address.logic.commands.ListStudentsOfClassCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.module.ModuleCode;
import seedu.address.model.module.TutorialClass;


/**
 * Parses input arguments and creates a new ListStudentsCommand object.
 */
public class ListStudentsOfClassCommandParser implements Parser<ListStudentsOfClassCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ListStudentsCommand
     * and returns a ListStudentsCommand object for execution.
     *
     * @param args String containing the arguments.
     * @return ListStudentsCommand object representing the command.
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public ListStudentsOfClassCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
            ArgumentTokenizer.tokenize(args, PREFIX_MODULECODE, PREFIX_TUTORIALCLASS);

        if (!arePrefixesPresent(argMultimap, PREFIX_MODULECODE, PREFIX_TUTORIALCLASS)
            || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ListStudentsOfClassCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_MODULECODE, PREFIX_TUTORIALCLASS);
        ModuleCode module = ParserUtil.parseModuleCode(argMultimap.getValue(PREFIX_MODULECODE).get());
        TutorialClass tutorialClass = ParserUtil.parseTutorialClass(argMultimap.getValue(PREFIX_TUTORIALCLASS).get());

        return new ListStudentsOfClassCommand(module, tutorialClass);
    }

    /**
     * Returns true if all the prefixes contain non-empty values in the given {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}

