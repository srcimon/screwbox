package io.github.srcimon.screwbox.core.archivements.internal;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.archivements.ArchivementDefinition;
import io.github.srcimon.screwbox.core.archivements.Archivement;
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
    private final List<DefaultArchivement> activeArchivements = new ArrayList<>();
    private final List<DefaultArchivement> completedArchivements = new ArrayList<>();
    private Consumer<Archivement> completionReaction;

    public DefaultArchivements(final Engine engine) {
        this.engine = engine;
        this.completionReaction = new LoggingCompletionReaction(engine.log());
    }

    @Override
    public Archivements add(final ArchivementDefinition archivement) {
        requireNonNull(archivement, "archivement must not be null");
        activeArchivements.add(new DefaultArchivement(archivement));
        return this;
    }

    @Override
    public Archivements setCompletionReaction(Consumer<Archivement> completionReaction) {
        this.completionReaction = completionReaction;
        return this;
    }

    @Override
    public List<Archivement> allArchivements() {
        return Stream.concat(activeArchivements.stream(), completedArchivements.stream())
                .map(Archivement.class::cast)
                .toList();
    }

    @Override
    public List<Archivement> activeArchivements() {
        return unmodifiableList(activeArchivements);
    }

    @Override
    public List<Archivement> completedArchivements() {
        return unmodifiableList(completedArchivements);
    }

    @Override
    public Archivements progess(final Class<? extends ArchivementDefinition> archivementFamily, int progress) {
        requireNonNull(archivementFamily, "archivement family must not be null");
        if (progress > 0) {
            for (final var activeArchivement : activeArchivements) {
                if (activeArchivement.isOfFamily(archivementFamily)) {
                    activeArchivement.progress(progress);
                }
            }
        }
        //TODO fail if no archivement found (also in completed archivements)
        return this;
    }

    @Override
    public Archivements addAllFromPackage(String packageName) {
        Reflections.findClassesInPackage(packageName).stream()
                .filter(ArchivementDefinition.class::isAssignableFrom)
                .map(Reflections::createInstance)
                .map(ArchivementDefinition.class::cast)
                .forEach(this::add);
        return this;
    }

    @Override
    public void update() {
        final boolean refreshLazyArchivements = lazyUpdateSheduler.isTick();
        final var transferItems = new ArrayList<DefaultArchivement>();
//TODO fail on lazy update archivement without progression method
        for (final var activeArchivement : activeArchivements) {
            if (refreshLazyArchivements || !activeArchivement.canBeUpdatedLazy()) {//TODO own list for all using auto upgrade (has method...)
                final var progress = activeArchivement.autoProgress(engine);
                activeArchivement.progress(progress);
            }
            if (activeArchivement.isCompleted()) {
                transferItems.add(activeArchivement);
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
