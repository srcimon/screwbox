package io.github.srcimon.screwbox.core.achievements;

import io.github.srcimon.screwbox.core.Engine;

import java.util.List;

/**
 * Add archivements to challange players with custom goals. Archivements will progress automatically when overwriting
 * {@link AchievementDefinition#progress(Engine)}. Archivements can also progress manually via {@link #progess(Class)}.
 *
 * @since 2.8.0
 */
public interface Achievements {

    /**
     * Adds a new {@link AchievementDefinition} to be completed by the player.
     *
     * @since 2.8.0
     */
    Achievements add(AchievementDefinition archivement);

    /**
     * Returns a list of all currently active and completed {@link Achievement archivements}.
     *
     * @since 2.8.0
     */
    List<Achievement> allAchivements();

    /**
     * Returns a list of all currently active {@link Achievement archivements}.
     *
     * @since 2.8.0
     */
    List<Achievement> activeArchivements();

    /**
     * Returns a list of all completed {@link Achievement archivements}.
     *
     * @since 2.8.0
     */
    List<Achievement> completedArchivements();

    /**
     * Updates the current {@link Achievement#score() score} of all archivements of the specified family with
     * the specified value.
     *
     * @since 2.8.0
     */
    Achievements progess(Class<? extends AchievementDefinition> archivementType, int progress);

    /**
     * Updates the current {@link Achievement#score() score} of all archivements of the specified family by one.
     *
     * @see #progess(Class, int)
     * @since 2.8.0
     */
    default Achievements progess(Class<? extends AchievementDefinition> archivement) {
        return progess(archivement, 1);
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
     * Resets status of all archivements.
     *
     * @since 2.8.0
     */
    void reset();
}
