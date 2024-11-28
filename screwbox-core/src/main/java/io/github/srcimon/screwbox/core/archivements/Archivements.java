package io.github.srcimon.screwbox.core.archivements;

import java.util.List;

public interface Archivements {

    class MyArchivement implements ArchivementDefinition {

        //TODO buildForGoals(10,20,40)

        @Override
        public ArchivementOptions define() { //TODO .buildFrom(xxx) would be nice to rechtferig the interface
            return ArchivementOptions
                    .title("best clicker")
                    .description("click {goal} times like a boss")
                    .goal(10);
        }
    }

    Archivements define(ArchivementDefinition archivement);

    Archivements reset(Clazz<? extends ArchivementDefinition> definition);

    Archivements complete(Clazz<? extends ArchivementDefinition> definition);

    Archivements unlock(Clazz<? extends ArchivementDefinition> definition);

    List<Archivement> get(Clazz<? extends ArchivementDefinition> definition);
}
