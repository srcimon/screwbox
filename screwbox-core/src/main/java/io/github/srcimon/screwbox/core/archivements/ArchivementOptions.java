package io.github.srcimon.screwbox.core.archivements;

import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.Objects;

public record ArchivementOptions(String title, String description, int goal, Class<? extends ArchivementDefinition> family) {

    public ArchivementOptions {
        Objects.requireNonNull(title, "title must not be null");
        Validate.notEmpty(title, "title must not be empty");
        Validate.positive(goal, "goal must be positive");
        //TODO validate family is not self
    }

    public static ArchivementOptions title(final String title) {
        return new ArchivementOptions(title, null, 1, null);
    }


    public ArchivementOptions description(final String description) {
        return new ArchivementOptions(title, description, goal, family);
    }

    public ArchivementOptions goal(final int goal) {
        return new ArchivementOptions(title, description, goal, family);
    }

    public ArchivementOptions family(Class<? extends ArchivementDefinition> family) {
        return new ArchivementOptions(title, description, goal, family);
    }
}
