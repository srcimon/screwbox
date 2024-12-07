package io.github.srcimon.screwbox.core.archivements;

import io.github.srcimon.screwbox.core.Engine;

import java.util.List;

/**
 * Add archivements to challange players with custom goals. Archivements will progress automatically when overwriting
 * {@link ArchivementDefinition#progress(Engine)}. Archivements can also progress manually via {@link #progess(Class)}.
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

    /**
     * Updates the current {@link Archivement#score() score} of all archivements of the specified family with
     * the specified value.
     *
     * @since 2.8.0
     */
    Archivements progess(Class<? extends ArchivementDefinition> archivementType, int progress);

    /**
     * Updates the current {@link Archivement#score() score} of all archivements of the specified family by one.
     *
     * @see #progess(Class, int)
     * @since 2.8.0
     */
    default Archivements progess(Class<? extends ArchivementDefinition> archivement) {
        return progess(archivement, 1);
    }

    /**
     * Automatically adds all {@link ArchivementDefinition archivements} from specified package.
     *
     * @see #add(ArchivementDefinition)
     * @see #addAllFromPackage(String)
     * @since 2.8.0
     */
    Archivements addAllFromPackage(String packageName);

    /**
     * Automatically adds all {@link ArchivementDefinition archivements} from the package the specified class lives in.
     *
     * @see #add(ArchivementDefinition)
     * @see #addAllFromPackage(String)
     * @since 2.8.0
     */
    default Archivements addAllFromClassPackage(final Class<?> clazz) {
        return addAllFromPackage(clazz.getPackageName());
    }

    /**
     * Resets status of all archivements.
     *
     * @since 2.8.0
     */
    void reset();

    //TODO ArchivementStatus get(Class<? extends Archivement> archivementType);
    //TODO List<ArchivementInfo> upcommingArchivements(10);
}
