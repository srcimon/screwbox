package io.github.srcimon.screwbox.core.archivements;

import java.util.List;

public interface Archivements {

    Archivements add(ArchivementDefinition archivement);

    //TODO List<Archivement> activeArchivements();

    List<Archivement> allArchivements();

    default Archivements progess(Class<? extends ArchivementDefinition> archivement) {
        return progess(archivement, 1);
    }

    Archivements progess(Class<? extends ArchivementDefinition> definition, int progress);

   /*Archivements reset(Class<? extends ArchivementDefinition> definition);

    Archivements complete(Class<? extends ArchivementDefinition> definition);

    Archivements unlock(Class<? extends ArchivementDefinition> definition);

    Archivement get(Class<? extends ArchivementDefinition> definition);*/
}
