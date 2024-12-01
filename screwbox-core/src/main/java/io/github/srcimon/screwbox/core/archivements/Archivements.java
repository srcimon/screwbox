package io.github.srcimon.screwbox.core.archivements;

import java.util.List;
import java.util.function.Consumer;

/**
 * Add archivements to challange players with custom goals.
 */
public interface Archivements {

    /**
     * Adds a new {@link Archivement} to be completed by the player.
     */
    Archivements add(Archivement archivement);

    List<ArchivementInfo> allArchivements();

    List<ArchivementInfo> activeArchivements();

    List<ArchivementInfo> completedArchivements();

    default Archivements progess(Class<? extends Archivement> archivement) {
        return progess(archivement, 1);
    }

    Archivements progess(Class<? extends Archivement> archivementFamily, int progress);

    Archivements addAllFromPackage(String packageName);

    default Archivements addAllFromClassPackage(Class<?> clazz)  {
        return addAllFromPackage(clazz.getPackageName());
    }

    Archivements setCompletionReaction(Consumer<ArchivementInfo> completionReaction);


    //TODO Archivements setLazyUpdateInterval(Duration)
    //TODO Archivements reset();
}
