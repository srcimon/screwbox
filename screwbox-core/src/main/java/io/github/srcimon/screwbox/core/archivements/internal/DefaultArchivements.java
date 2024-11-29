package io.github.srcimon.screwbox.core.archivements.internal;

import io.github.srcimon.screwbox.core.archivements.Archivement;
import io.github.srcimon.screwbox.core.archivements.ArchivementDefinition;
import io.github.srcimon.screwbox.core.archivements.Archivements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultArchivements implements Archivements {

    private final Map<Class<? extends ArchivementDefinition>, ArchivementData> archivements = new HashMap<>();

    @Override
    public Archivements add(ArchivementDefinition archivement) {
        archivements.put(archivement.getClass(), new ArchivementData(archivement.define()));
        return this;
    }

    @Override
    public List<Archivement> allArchivements() {
        return archivements.values()
                .stream()
                .map(Archivement.class::cast)
                .toList();
    }

    @Override
    public Archivements progess(Class<? extends ArchivementDefinition> definition, int progress) {
        ArchivementData archivementData = archivements.get(definition);
        if(!archivementData.isArchived() && !archivementData.isLocked()) {
            archivementData.progress(progress);
            if(archivementData.isArchived()) {
                System.out.println("Archivement unlocked " + archivementData.title());
                 for(final var ar : archivements.values()) {//TODO unlock in next frame to prevent double counting

                     if(definition.equals(ar.predecessor())) {
                         ar.unlock(archivementData.score());
                     }
                 }
            }
        }
        return this;
    }
}
