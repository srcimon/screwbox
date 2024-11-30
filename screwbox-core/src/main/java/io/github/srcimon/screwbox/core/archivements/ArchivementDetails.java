package io.github.srcimon.screwbox.core.archivements;

import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.Objects;

public record ArchivementDetails(String title, String description, int goal, Class<? extends Archivement> family, boolean isFixedProgressMode) {

    public ArchivementDetails {
        Objects.requireNonNull(title, "title must not be null");
        Validate.notEmpty(title, "title must not be empty");
        Validate.positive(goal, "goal must be positive");
        //TODO validate family is not self
    }

    public static ArchivementDetails title(final String title) {
        return new ArchivementDetails(title, null, 1, null, false);
    }


    public ArchivementDetails description(final String description) {
        return new ArchivementDetails(title, description, goal, family, isFixedProgressMode);
    }

    public ArchivementDetails goal(final int goal) {
        return new ArchivementDetails(title, description, goal, family, isFixedProgressMode);
    }

    public ArchivementDetails family(Class<? extends Archivement> family) {
        return new ArchivementDetails(title, description, goal, family, isFixedProgressMode);
    }

    public ArchivementDetails useFixedProgressMode() {
        return new ArchivementDetails(title, description, goal, family, true);
    }

}
