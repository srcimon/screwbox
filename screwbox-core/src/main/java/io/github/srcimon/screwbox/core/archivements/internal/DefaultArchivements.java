package io.github.srcimon.screwbox.core.archivements.internal;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.archivements.Archivement;
import io.github.srcimon.screwbox.core.archivements.ArchivementInfo;
import io.github.srcimon.screwbox.core.archivements.Archivements;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.utils.Reflections;
import io.github.srcimon.screwbox.core.utils.Sheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class DefaultArchivements implements Archivements, Updatable {

    private final Sheduler lazyUpdateSheduler = Sheduler.withInterval(Duration.ofMillis(500));
    private final List<ArchivementInfoData> active = new ArrayList<>();
    private final List<ArchivementInfoData> stale = new ArrayList<>();
    private final Engine engine;

    private List<ArchivementInfoData> transferList = new ArrayList<>();
    public DefaultArchivements(final Engine engine) {
        this.engine = engine;
    }

    @Override
    public Archivements add(final Archivement archivement) {
        ArchivementInfoData archivementInfo = new ArchivementInfoData(archivement);
        active.add(archivementInfo);
        return this;
    }

    @Override
    public List<ArchivementInfo> allArchivements() {
        return Stream.concat(active.stream(), stale.stream())
                .map(ArchivementInfo.class::cast)
                .toList();
    }

    @Override
    public List<ArchivementInfo> activeArchivements() {
        return Collections.unmodifiableList(active);
    }

    @Override
    public Archivements progess(Class<? extends Archivement> archivement, int progress) {
        if (progress <= 0) {
            return this;
        }
        for (var archivementData : active) {
            if (archivementData.isOfFamily(archivement)) {
                progress(progress, archivementData);
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

    @Override
    public void update() {
        final boolean refreshLazyArchivements = lazyUpdateSheduler.isTick();

        for (var x : active) {
            if (refreshLazyArchivements || !x.isLazy()) {
                var progress = x.autoProgress(engine);
                progress(progress, x);
            }
        }

        for(var y : transferList) {
            active.remove(y);
            stale.add(y);
        }
        transferList.clear();
    }

    private void progress(int progress, ArchivementInfoData archivementData) {
        archivementData.progress(progress);
        if (archivementData.isArchived()) {
            transferList.add(archivementData);
        }
    }
}
