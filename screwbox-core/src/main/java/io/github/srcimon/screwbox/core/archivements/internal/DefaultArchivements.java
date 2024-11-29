package io.github.srcimon.screwbox.core.archivements.internal;

import io.github.srcimon.screwbox.core.archivements.Archivement;
import io.github.srcimon.screwbox.core.archivements.ArchivementDefinition;
import io.github.srcimon.screwbox.core.archivements.Archivements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class DefaultArchivements implements Archivements {

    private final Map<Class<? extends ArchivementDefinition>, ArchivementData> unarchived = new HashMap<>();
    private final Map<Class<? extends ArchivementDefinition>, ArchivementData> archived = new HashMap<>();

    @Override
    public Archivements add(ArchivementDefinition archivement) {
        unarchived.put(archivement.getClass(), new ArchivementData(archivement, archivement.defineArchivement()));
        return this;
    }

    @Override
    public List<Archivement> allArchivements() {
        return Stream.concat(unarchived.values().stream(), archived.values().stream())
                .map(Archivement.class::cast)
                .toList();
    }

    @Override
    public Archivements progess(Class<? extends ArchivementDefinition> definition, int progress) {
        if(progress <= 0) {
            return this;
        }
        for(var archivementData : unarchived.values()) {
            if(archivementData.isOfFamily(definition)) {
                progress(definition, progress, archivementData);
            }
        }
        return this;
    }

    private void progress(Class<? extends ArchivementDefinition> definition, int progress, ArchivementData archivementData) {
        archivementData.progress(progress);
        if (archivementData.isArchived()) {
            unarchived.remove(definition);
            archived.put(definition, archivementData);
            System.out.println("Archivement unlocked " + archivementData.title());
        }
    }
}
