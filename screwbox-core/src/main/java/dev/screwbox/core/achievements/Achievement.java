package dev.screwbox.core.achievements;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Time;
import dev.screwbox.core.graphics.Sprite;

import java.util.Optional;

/**
 * Achievement that can be completed by the player.
 *
 * @since 2.8.0
 */
public interface Achievement {

    /**
     * Title of the achievement.
     */
    String title();

    /**
     * (Optional) description of the achievement to further describe how to reach the {@link #goal()}.
     */
    Optional<String> description();

    /**
     * Current score towards the {@link #goal()}. Can never be higher than {@link #goal()}.
     */
    int score();

    /**
     * The goal that has to be reached.
     *
     * @see #score()
     */
    int goal();

    /**
     * Returns {@code true} once the {@link #goal()} is reached.
     */
    boolean isCompleted();

    /**
     * Current progress towards the {@link #goal()}.
     */
    Percent progress();

    /**
     * Start time of the achievement.
     */
    Time startTime();

    /**
     * Completion time of the achievement. Will be {@link Time#unset()} when not {@link #isCompleted() completed}.
     */
    Time completionTime();

    /**
     * Icon associated with this achievement.
     */
    Optional<Sprite> icon();
}
