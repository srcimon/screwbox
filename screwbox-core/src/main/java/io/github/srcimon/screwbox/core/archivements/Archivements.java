package io.github.srcimon.screwbox.core.archivements;

import java.util.Collection;

public interface Archivements {

    Archivements define(ArchivementDefinition archivement);

    Collection<Archivement> allArchivements();

    /* Archivements progess(Class<? extends ArchivementDefinition> definition);

   Archivements reset(Class<? extends ArchivementDefinition> definition);

    Archivements complete(Class<? extends ArchivementDefinition> definition);

    Archivements unlock(Class<? extends ArchivementDefinition> definition);

    Archivement get(Class<? extends ArchivementDefinition> definition);*/
}
