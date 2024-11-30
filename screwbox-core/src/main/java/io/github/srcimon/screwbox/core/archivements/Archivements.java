package io.github.srcimon.screwbox.core.archivements;

import java.util.List;

public interface Archivements {

    Archivements add(Archivement archivement);

    //TODO List<Archivement> activeArchivements();

    List<ArchivementInfo> allArchivements();

    default Archivements progess(Class<? extends Archivement> archivement) {
        return progess(archivement, 1);
    }


    Archivements progess(Class<? extends Archivement> definition, int progress);

    Archivements addAllFromPackage(String packageName);

   /*Archivements reset(Class<? extends ArchivementDefinition> definition);

Archivements reset();

Archivements clear();
    Archivements complete(Class<? extends ArchivementDefinition> definition);

    Archivements unlock(Class<? extends ArchivementDefinition> definition);

    Archivement get(Class<? extends ArchivementDefinition> definition);*/
}
