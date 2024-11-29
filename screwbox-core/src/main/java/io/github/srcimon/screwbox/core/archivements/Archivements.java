package io.github.srcimon.screwbox.core.archivements;

import java.util.List;

public interface Archivements {

    Archivements define(ArchivementDefinition archivement);

    Archivements reset(Class<? extends ArchivementDefinition> definition);

    Archivements complete(Class<? extends ArchivementDefinition> definition);

    Archivements unlock(Class<? extends ArchivementDefinition> definition);

    List<Archivement> get(Class<? extends ArchivementDefinition> definition);
}
