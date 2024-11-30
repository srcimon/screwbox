package io.github.srcimon.screwbox.core.archivements.internal;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.archivements.ArchivementInfo;
import io.github.srcimon.screwbox.core.archivements.Archivement;
import io.github.srcimon.screwbox.core.archivements.Archivements;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.utils.Reflections;
import io.github.srcimon.screwbox.core.utils.Sheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class DefaultArchivements implements Archivements, Updatable {

    private final Map<Class<? extends Archivement>, ArchivementInfoData> unarchived = new HashMap<>();
    private final Map<Class<? extends Archivement>, ArchivementInfoData> archived = new HashMap<>();
    private final Engine engine;

    public DefaultArchivements(final Engine engine) {
        this.engine = engine;
    }

    @Override
    public Archivements add(Archivement archivement) {
        unarchived.put(archivement.getClass(), new ArchivementInfoData(archivement, archivement.configuration()));
        return this;
    }

    @Override
    public List<ArchivementInfo> allArchivements() {
        return Stream.concat(unarchived.values().stream(), archived.values().stream())
                .map(ArchivementInfo.class::cast)
                .toList();
    }

    @Override
    public Archivements progess(Class<? extends Archivement> definition, int progress) {
        if (progress <= 0) {
            return this;
        }
        for (var archivementData : new ArrayList<>(unarchived.values())) {
            if (archivementData.isOfFamily(definition)) {
                progress(definition, progress, archivementData);
            }
        }
        return this;
    }

    @Override
    public Archivements addAllFromPackage(String packageName) {
        Reflections.findClassesInPackage(packageName).stream().filter(f -> Archivement.class.isAssignableFrom(f)).forEach(f -> {
            try {
                add((Archivement) f.newInstance());
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        });
        return this;
    }

    private void progress(Class<? extends Archivement> definition, int progress, ArchivementInfoData archivementData) {
        archivementData.progress(progress);
        if (archivementData.isArchived()) {
            unarchived.remove(definition);
            archived.put(definition, archivementData);
        }
    }

    Sheduler sheduler = Sheduler.withInterval(Duration.ofMillis(500));
    @Override
    public void update() {
        final boolean refreshLazyArchivements = sheduler.isTick();

        for (var x : unarchived.values()) {
            if(refreshLazyArchivements || !x.isLazy()) {
                x.autoProgress(engine);
                //TODO same behaviour as explicitly progressing
            }
        }
    }
}
