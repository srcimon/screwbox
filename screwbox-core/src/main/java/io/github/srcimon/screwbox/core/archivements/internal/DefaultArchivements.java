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
        archivements.get(definition).progress(progress);
        return this;
    }
}
