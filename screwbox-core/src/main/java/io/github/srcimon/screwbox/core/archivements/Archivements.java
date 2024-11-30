package io.github.srcimon.screwbox.core.archivements;

import java.util.List;

public interface Archivements {

    Archivements add(Archivement archivement);

    List<ArchivementInfo> allArchivements();

    List<ArchivementInfo> activeArchivements();

    List<ArchivementInfo> completedArchivements();

    default Archivements progess(Class<? extends Archivement> archivement) {
        return progess(archivement, 1);
    }

    Archivements progess(Class<? extends Archivement> archivement, int progress);

    Archivements addAllFromPackage(String packageName);

    default Archivements addAllFromClassPackage(Class<?> clazz)  {
        return addAllFromPackage(clazz.getPackageName());
    }

    //TODO Archivements setLazyUpdateInterval(Duration)
    //TODO List<Archivement> activeArchivements();
    //TODO Archivements reset();
}
