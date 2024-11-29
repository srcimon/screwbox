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
    public Archivements define(ArchivementDefinition archivement) {
        return archivements.put(archivement.getClass(), archivement.define());
    }

    @Override
    public Collection<Archivement> allArchivements() {
        return archivements.values();
    }
}
