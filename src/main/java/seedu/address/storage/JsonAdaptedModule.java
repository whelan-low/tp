package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.module.ModuleCode;
import seedu.address.model.module.TutorialClass;
import seedu.address.model.person.StudentId;

/**
 * Jackson-friendly version of {@link ModuleCode}.
 */
class JsonAdaptedModule {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Module name is missing!";

    private final String name;
    private final List<JsonAdaptedTutorialClass> tutorialClasses = new ArrayList<>();
    private final String description;

    /**
     * Constructs a {@code JsonAdaptedModule} with the given module details.
     */
    @JsonCreator
    public JsonAdaptedModule(@JsonProperty("name") String name,
                             @JsonProperty("tutorialClasses") List<JsonAdaptedTutorialClass> tutorialClasses,
                             @JsonProperty("description") String description) {
        this.name = name;
        this.description = description;
        if (tutorialClasses != null) {
            this.tutorialClasses.addAll(tutorialClasses);
        }
    }

    /**
     * Converts a given {@code Module} into this class for Jackson use.
     */
    public JsonAdaptedModule(ModuleCode source) {
        name = source.toString();
        tutorialClasses.addAll(source.getTutorialClasses().stream().map(JsonAdaptedTutorialClass::new)
            .collect(Collectors.toCollection(ArrayList::new)));
        this.description = source.getDescription();
    }

    /**
     * Retrieves module name from the module.
     */
    public String getModuleName() {
        return name;
    }

    /**
     * Retrieves tutorial classes from the module.
     */
    public List<JsonAdaptedTutorialClass> getTutorialClasses() {
        return tutorialClasses;
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public ModuleCode toModelType() throws IllegalValueException {
        if (!ModuleCode.isValidModuleCode(name)) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, name));
        }
        final List<TutorialClass> listOfClass = new ArrayList<>();
        for (JsonAdaptedTutorialClass tutorialClass : this.tutorialClasses) {
            listOfClass.add(tutorialClass.toModelType());
        }

        ModuleCode moduleCode;
        if (!ModuleCode.isValidModuleCode(name)) {
            throw new IllegalValueException(StudentId.MESSAGE_CONSTRAINTS);
        }

        if (description != null && !description.isEmpty()) {
            moduleCode = new ModuleCode(name, listOfClass,
                description);
        } else {
            moduleCode = new ModuleCode(name, listOfClass);
        }

        return moduleCode;
    }
}
