package dev.screwbox.core.achievements;

import dev.screwbox.core.Engine;

/**
 * An achievable goal to keep players engaged. Can be added to game via {@link Achievements#add(AchievementDefinition)}.
 */
@FunctionalInterface
public interface AchievementDefinition {

    /**
     * Configures the details of the {@link AchievementDefinition} e.g. {@link AchievementDetails#goal()}.
     */
    AchievementDetails details();

    /**
     * Can be overwritten to automatically progress towards the {@link AchievementDetails#goal()}. Automatically
     * progressing achievements cannot progress manually via {@link Achievements#progress(Class)}.
     */
    default int progress(final Engine engine) {
        return 0;
    }
}
