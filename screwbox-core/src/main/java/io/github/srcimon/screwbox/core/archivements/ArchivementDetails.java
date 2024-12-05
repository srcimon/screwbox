package io.github.srcimon.screwbox.core.archivements;

import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.Objects;

public record ArchivementDetails(String title, String description, int goal,
                                 boolean isCumulativeProgression, boolean isUpdateLazy) {

    public ArchivementDetails {
        Objects.requireNonNull(title, "title must not be null");
        Validate.notEmpty(title, "title must not be empty");
        Validate.positive(goal, "goal must be positive");
        Validate.isFalse(isUpdateLazy && isCumulativeProgression, "lazy refresh can only be used when not using cumulative progression");
    }

    public static ArchivementDetails title(final String title) {
        return new ArchivementDetails(title, null, 1, true, false);
    }

    public ArchivementDetails description(final String description) {
        return new ArchivementDetails(title, description, goal, isCumulativeProgression, isUpdateLazy);
    }

    public ArchivementDetails goal(final int goal) {
        return new ArchivementDetails(title, description, goal, isCumulativeProgression, isUpdateLazy);
    }

    public ArchivementDetails nonCumulativeProgression() {
        return new ArchivementDetails(title, description, goal, false, isUpdateLazy);
    }

    public ArchivementDetails updateLazy() {
        return new ArchivementDetails(title, description, goal, isCumulativeProgression, true);
    }

}
