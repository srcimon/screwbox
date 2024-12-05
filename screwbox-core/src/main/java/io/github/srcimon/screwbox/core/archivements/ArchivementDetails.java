package io.github.srcimon.screwbox.core.archivements;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.Objects;

/**
 * Configures the details of an {@link Archivement}.
 *
 * @param title                   title of the archivement
 * @param description             optional description
 * @param goal                    target goal that hast to be reached
 * @param isCumulativeProgression progression will be cumulative, otherwise every call of
 *                                {@link Archivement#progress(Engine)} will be counted as absolute value
 * @param isLazyRefresh           update only every other frame to save performance. can only be used with cumulative progression
 */
public record ArchivementDetails(String title, String description, int goal,
                                 boolean isCumulativeProgression, boolean isLazyRefresh) {

    public ArchivementDetails {
        Objects.requireNonNull(title, "title must not be null");
        Validate.notEmpty(title, "title must not be empty");
        Validate.positive(goal, "goal must be positive");
        Validate.isFalse(isLazyRefresh && isCumulativeProgression, "lazy refresh can only be used when not using cumulative progression");
    }

    /**
     * Sets the title of the {@link Archivement}.
     */
    public static ArchivementDetails title(final String title) {
        return new ArchivementDetails(title, null, 1, true, false);
    }

    /**
     * Sets the description of the {@link Archivement}.
     */
    public ArchivementDetails description(final String description) {
        return new ArchivementDetails(title, description, goal, isCumulativeProgression, isLazyRefresh);
    }

    /**
     * Sets the goal of the {@link Archivement}. (default 1)
     */
    public ArchivementDetails goal(final int goal) {
        return new ArchivementDetails(title, description, goal, isCumulativeProgression, isLazyRefresh);
    }

    /**
     * Changes progression to absolute value.
     */
    public ArchivementDetails nonCumulativeProgression() {
        return new ArchivementDetails(title, description, goal, false, isLazyRefresh);
    }

    /**
     * Marks {@link Archivement} for lazy updates to gain performance. Can only be used with cumulative progression.
     */
    public ArchivementDetails updateLazy() {
        return new ArchivementDetails(title, description, goal, isCumulativeProgression, true);
    }

}
