package io.github.srcimon.screwbox.core.archivements;

import java.util.List;
import java.util.function.Consumer;

/**
 * Add archivements to challange players with custom goals.
 *
 * @since 2.8.0
 */
public interface Archivements {

    /**
     * Adds a new {@link ArchivementDefinition} to be completed by the player.
     *
     * @since 2.8.0
     */
    Archivements add(ArchivementDefinition archivement);

    /**
     * Returns a list of all currently active and completed {@link Archivement archivements}.
     *
     * @since 2.8.0
     */
    List<Archivement> allArchivements();

    /**
     * Returns a list of all currently active {@link Archivement archivements}.
     *
     * @since 2.8.0
     */
    List<Archivement> activeArchivements();

    /**
     * Returns a list of all completed {@link Archivement archivements}.
     *
     * @since 2.8.0
     */
    List<Archivement> completedArchivements();

   //TODO ArchivementStatus get(Class<? extends Archivement> archivementType);

    //TODO List<ArchivementInfo> upcommingArchivements(10);

    /**
     * Updates the current {@link Archivement#score() score} of all archivements of the specified family with
     * the specified value.
     *
     * @since 2.8.0
     */
    Archivements progess(Class<? extends ArchivementDefinition> archivementFamily, int progress);

    /**
     * Updates the current {@link Archivement#score() score} of all archivements of the specified family by one.
     *
     * @see #progess(Class, int)
     * @since 2.8.0
     */
    default Archivements progess(Class<? extends ArchivementDefinition> archivement) {
        return progess(archivement, 1);
    }

    //TODO javadoc
    Archivements addAllFromPackage(String packageName);

    //TODO javadoc
    default Archivements addAllFromClassPackage(Class<?> clazz) {
        return addAllFromPackage(clazz.getPackageName());
    }
    //TODO javadoc
    Archivements setCompletionReaction(Consumer<Archivement> completionReaction);

    //TODO Archivements reset();
}
