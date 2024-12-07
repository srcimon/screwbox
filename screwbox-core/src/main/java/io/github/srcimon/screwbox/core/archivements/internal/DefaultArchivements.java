package io.github.srcimon.screwbox.core.archivements.internal;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.archivements.Archivement;
import io.github.srcimon.screwbox.core.archivements.ArchivementDefinition;
import io.github.srcimon.screwbox.core.archivements.Archivements;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.utils.Reflections;
import io.github.srcimon.screwbox.core.utils.Sheduler;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class DefaultArchivements implements Archivements, Updatable {

    private final Sheduler lazyUpdateSheduler = Sheduler.withInterval(Duration.ofMillis(500));
    private final Engine engine;
    private final Map<Class<? extends ArchivementDefinition>, List<DefaultArchivement>> archivementsByClass = new HashMap<>();
    private final List<DefaultArchivement> activeArchivements = new ArrayList<>();
    private final List<DefaultArchivement> completedArchivements = new ArrayList<>();
    private final Consumer<Archivement> onCompletion;

    public DefaultArchivements(final Engine engine, Consumer<Archivement> onCompletion) {
        this.engine = engine;
        this.onCompletion = onCompletion;
    }

    @Override
    public Archivements add(final ArchivementDefinition archivement) {
        requireNonNull(archivement, "archivement must not be null");
        final var defaultArchivement = new DefaultArchivement(archivement);
        final var archivementClazz = archivement.getClass();
        activeArchivements.add(defaultArchivement);
        final var archivementsOfClazz = archivementsByClass.get(archivementClazz);

        if (isNull(archivementsOfClazz)) {
            archivementsByClass.put(archivementClazz, new ArrayList<>(List.of(defaultArchivement)));
        } else {
            archivementsOfClazz.add(defaultArchivement);
        }
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

    //TODO prevent archivement from progression when archivement has automatic progression
    @Override
    public Archivements progess(final Class<? extends ArchivementDefinition> archivementType, int progress) {
        requireNonNull(archivementType, "archivement family must not be null");
        Validate.zeroOrPositive(progress, "progress must be positive");
        if (progress == 0) {
            return this;
        }
        final var archivmentsOfType = archivementsByClass.get(archivementType);
        if (nonNull(archivmentsOfType)) {
            for (final var archivement : archivmentsOfType) {
                archivement.progress(progress);
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
//TODO fail on lazy update archivement without progression method
        for (final var activeArchivement : new ArrayList<>(activeArchivements)) {
            if (refreshLazyArchivements || !activeArchivement.progressionIsAbsolute()) {//TODO own list for all using auto upgrade (has method...)
                final var progress = activeArchivement.autoProgress(engine);
                activeArchivement.progress(progress);
            }
            if (activeArchivement.isCompleted()) {
                activeArchivements.remove(activeArchivement);
                completedArchivements.add(activeArchivement);
                activeArchivement.setCompleted(Time.now());
                onCompletion.accept(activeArchivement);
            }
        }
    }

}
