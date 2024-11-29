package io.github.srcimon.screwbox.core.archivements;

import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.Objects;

public record ArchivementOptions(String title, String description, int goal, Class<? extends ArchivementDefinition> predecessor) {

    public ArchivementOptions {
        Objects.requireNonNull(title, "title must not be null");
        Validate.notEmpty(title, "title must not be empty");
        Validate.positive(goal, "goal must be positive");
    }

    public static ArchivementOptions title(final String title) {
        return new ArchivementOptions(title, null, 1, null);
    }

    public ArchivementOptions predecessor(Class<? extends ArchivementDefinition> predecessor) {
        return new ArchivementOptions(title, description, goal, predecessor);
    }

    public ArchivementOptions description(final String description) {
        return new ArchivementOptions(title, description, goal, predecessor);
    }

    public ArchivementOptions goal(final int goal) {
        return new ArchivementOptions(title, description, goal, predecessor);
    }
}
