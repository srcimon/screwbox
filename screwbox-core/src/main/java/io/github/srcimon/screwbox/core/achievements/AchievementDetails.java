package io.github.srcimon.screwbox.core.achievements;

import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Configures the details of an {@link AchievementDefinition}.
 *
 * @param title                 title of the achievement. Will replace '{goal}' with actual goal.
 * @param description           optional description. Will replace '{goal}' with actual goal.
 * @param goal                  target goal that hast to be reached
 * @param icon                  optional icon associated with this achievement
 * @param progressionIsAbsolute progression values won't accumulate and will be counted as absolute values.
 *                              Absolute progression automatically slows down updates to grain performance.
 * @since 2.8.0
 */
public record AchievementDetails(String title, String description, int goal,
                                 boolean progressionIsAbsolute, Optional<Sprite> icon) {

    public AchievementDetails {
        Objects.requireNonNull(title, "title must not be null");
        Validate.notEmpty(title, "title must not be empty");
        Validate.positive(goal, "goal must be positive");
    }

    /**
     * Sets the title of the {@link AchievementDefinition}. Will replace '{goal}' with actual goal.
     */
    public static AchievementDetails title(final String title) {
        return new AchievementDetails(title, null, 1, false, Optional.empty());
    }

    /**
     * Sets the description of the {@link AchievementDefinition}. Will replace '{goal}' with actual goal.
     */
    public AchievementDetails description(final String description) {
        return new AchievementDetails(title, description, goal, progressionIsAbsolute, icon);
    }

    /**
     * Sets the goal of the {@link AchievementDefinition}. (default 1)
     */
    public AchievementDetails goal(final int goal) {
        return new AchievementDetails(title, description, goal, progressionIsAbsolute, icon);
    }

    /**
     * Changes progression to absolute value. Otherwise progression will be cumulative. Absolute progression automatically
     * slows down updates to grain performance.
     */
    public AchievementDetails useAbsoluteProgression() {
        return new AchievementDetails(title, description, goal, true, icon);
    }

    /**
     * Sets custom icon associated with the achievement.
     */
    public AchievementDetails icon(final Supplier<Sprite> icon) {
        return icon(icon.get());
    }

    /**
     * Sets custom icon associated with the achievement.
     */
    public AchievementDetails icon(final Sprite icon) {
        return new AchievementDetails(title, description, goal, progressionIsAbsolute, Optional.of(icon));
    }
}
