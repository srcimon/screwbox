package io.github.srcimon.screwbox.core.achievements;

import io.github.srcimon.screwbox.core.Engine;

import java.util.List;

/**
 * Add achievement to challenge players with custom goals. Achievement will progress automatically when overwriting
 * {@link AchievementDefinition#progress(Engine)}. Achievement can also progress manually via {@link #progess(Class)}.
 *
 * @since 2.8.0
 */
public interface Achievements {

    /**
     * Adds a new {@link AchievementDefinition} to be completed by the player.
     *
     * @since 2.8.0
     */
    Achievements add(AchievementDefinition achievement);

    /**
     * Returns a list of all currently active and completed {@link Achievement archivements}.
     *
     * @since 2.8.0
     */
    List<Achievement> allAchievements();

    /**
     * Returns a list of all currently active {@link Achievement archivements}.
     *
     * @since 2.8.0
     */
    List<Achievement> activeAchievements();

    /**
     * Returns a list of all completed {@link Achievement archivements}.
     *
     * @since 2.8.0
     */
    List<Achievement> completedAchievements();

    /**
     * Updates the current {@link Achievement#score() score} of all achievements of the specified family with
     * the specified value.
     *
     * @since 2.8.0
     */
    Achievements progess(Class<? extends AchievementDefinition> achievementType, int progress);

    /**
     * Updates the current {@link Achievement#score() score} of all achievement of the specified family by one.
     *
     * @see #progess(Class, int)
     * @since 2.8.0
     */
    default Achievements progess(Class<? extends AchievementDefinition> achievement) {
        return progess(achievement, 1);
    }

    /**
     * Automatically adds all {@link AchievementDefinition archivements} from specified package.
     *
     * @see #add(AchievementDefinition)
     * @see #addAllFromPackage(String)
     * @since 2.8.0
     */
    Achievements addAllFromPackage(String packageName);

    /**
     * Automatically adds all {@link AchievementDefinition archivements} from the package the specified class lives in.
     *
     * @see #add(AchievementDefinition)
     * @see #addAllFromPackage(String)
     * @since 2.8.0
     */
    default Achievements addAllFromClassPackage(final Class<?> clazz) {
        return addAllFromPackage(clazz.getPackageName());
    }

    /**
     * Resets status of all achievement.
     *
     * @since 2.8.0
     */
    void reset();
}
