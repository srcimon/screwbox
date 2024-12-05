package io.github.srcimon.screwbox.core.archivements;

import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.Objects;

/**
 * Configures the details of an {@link Archivement}.
 *
 * @param title                 title of the archivement. Will replace '{goal}' with actual goal.
 * @param description           optional description. Will replace '{goal}' with actual goal.
 * @param goal                  target goal that hast to be reached
 * @param progressionIsAbsolute progression values won't accumulate and will be counted as absolute values.
 *                              Absolute progression automatically slows down updates to grain performance.
 * @since 2.8.0
 */
public record ArchivementDetails(String title, String description, int goal, boolean progressionIsAbsolute) {

    public ArchivementDetails {
        Objects.requireNonNull(title, "title must not be null");
        Validate.notEmpty(title, "title must not be empty");
        Validate.positive(goal, "goal must be positive");
    }

    /**
     * Sets the title of the {@link Archivement}. Will replace '{goal}' with actual goal.
     */
    public static ArchivementDetails title(final String title) {
        return new ArchivementDetails(title, null, 1, true);
    }

    /**
     * Sets the description of the {@link Archivement}. Will replace '{goal}' with actual goal.
     */
    public ArchivementDetails description(final String description) {
        return new ArchivementDetails(title, description, goal, progressionIsAbsolute);
    }

    /**
     * Sets the goal of the {@link Archivement}. (default 1)
     */
    public ArchivementDetails goal(final int goal) {
        return new ArchivementDetails(title, description, goal, progressionIsAbsolute);
    }

    /**
     * Changes progression to absolute value. Otherwise progrssion will be cumulative. Absolute progression automatically
     * slows down updates to grain performance.
     */
    public ArchivementDetails useAbsoluteProgression() {
        return new ArchivementDetails(title, description, goal, false);
    }
}
