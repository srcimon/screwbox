package io.github.srcimon.screwbox.core.archivements.internal;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.archivements.Archivement;
import io.github.srcimon.screwbox.core.archivements.ArchivementStatus;
import io.github.srcimon.screwbox.core.archivements.Archivements;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.utils.Reflections;
import io.github.srcimon.screwbox.core.utils.Sheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;

public class DefaultArchivements implements Archivements, Updatable {

    private final Sheduler lazyUpdateSheduler = Sheduler.withInterval(Duration.ofMillis(500));
    private final Engine engine;
    private final List<DefaultArchivementStatus> activeArchivements = new ArrayList<>();
    private final List<DefaultArchivementStatus> completedArchivements = new ArrayList<>();
    private Consumer<ArchivementStatus> completionReaction;

    public DefaultArchivements(final Engine engine) {
        this.engine = engine;
    }

    @Override
    public Archivements add(final Archivement archivement) {
        requireNonNull(archivement, "archivement must not be null");
        activeArchivements.add(new DefaultArchivementStatus(archivement));
        return this;
    }

    @Override
    public Archivements setCompletionReaction(Consumer<ArchivementStatus> completionReaction) {
        this.completionReaction = completionReaction;
        return this;
    }

    @Override
    public List<ArchivementStatus> allArchivements() {
        return Stream.concat(activeArchivements.stream(), completedArchivements.stream())
                .map(ArchivementStatus.class::cast)
                .toList();
    }

    @Override
    public List<ArchivementStatus> activeArchivements() {
        return unmodifiableList(activeArchivements);
    }

    @Override
    public List<ArchivementStatus> completedArchivements() {
        return unmodifiableList(completedArchivements);
    }

    @Override
    public Archivements progess(final Class<? extends Archivement> archivementFamily, int progress) {
        requireNonNull(archivementFamily, "archivement family must not be null");
        if (progress > 0) {
            for (final var activeArchivement : activeArchivements) {
                if (activeArchivement.isOfFamily(archivementFamily)) {
                    activeArchivement.progress(progress);
                }
            }
        }
        return this;
    }

    @Override
    public Archivements addAllFromPackage(String packageName) {
        Reflections.findClassesInPackage(packageName).stream()
                .filter(Archivement.class::isAssignableFrom)
                .map(Reflections::createInstance)
                .map(Archivement.class::cast)
                .forEach(this::add);
        return this;
    }

    @Override
    public void update() {
        final boolean refreshLazyArchivements = lazyUpdateSheduler.isTick();
        final var transferItems = new ArrayList<DefaultArchivementStatus>();

        for (final var activeArchivement : activeArchivements) {
            if (refreshLazyArchivements || !activeArchivement.isLazy()) {
                var progress = activeArchivement.autoProgress(engine);
                activeArchivement.progress(progress);
            }
            if (activeArchivement.isCompleted()) {
                transferItems.add(activeArchivement);
                System.out.println(activeArchivement.title());
                completionReaction.accept(activeArchivement);
            }
        }

        for (final var transferItem : transferItems) {
            activeArchivements.remove(transferItem);
            completedArchivements.add(transferItem);
        }
        transferItems.clear();
    }

}
