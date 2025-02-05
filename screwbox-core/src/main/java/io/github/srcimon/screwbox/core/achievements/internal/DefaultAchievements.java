package io.github.srcimon.screwbox.core.achievements.internal;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.achievements.Achievement;
import io.github.srcimon.screwbox.core.achievements.AchievementDefinition;
import io.github.srcimon.screwbox.core.achievements.Achievements;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.utils.Reflections;
import io.github.srcimon.screwbox.core.utils.Sheduler;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static io.github.srcimon.screwbox.core.utils.ListUtil.combine;
import static java.util.Collections.unmodifiableList;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

public class DefaultAchievements implements Achievements, Updatable {

    private final Sheduler lazyUpdateSheduler = Sheduler.withInterval(Duration.ofMillis(500));
    private final Engine engine;
    private final Map<Class<? extends AchievementDefinition>, List<DefaultAchievement>> archivementsByClass = new HashMap<>();
    private final List<DefaultAchievement> activeAchievements = new ArrayList<>();
    private final List<DefaultAchievement> completedAchievements = new ArrayList<>();
    private final Consumer<Achievement> onCompletion;

    public DefaultAchievements(final Engine engine, Consumer<Achievement> onCompletion) {
        this.engine = engine;
        this.onCompletion = onCompletion;
    }

    @Override
    public Achievements add(final AchievementDefinition achievement) {
        requireNonNull(achievement, "achievement must not be null");
        final var defaultArchivement = new DefaultAchievement(achievement);
        final var archivementClazz = achievement.getClass();
        activeAchievements.add(defaultArchivement);
        final var archivementsOfClazz = archivementsByClass.get(archivementClazz);

        if (isNull(archivementsOfClazz)) {
            archivementsByClass.put(archivementClazz, new ArrayList<>(List.of(defaultArchivement)));
        } else {
            archivementsOfClazz.add(defaultArchivement);
        }
        return this;
    }

    @Override
    public List<Achievement> allAchivements() {
        return unmodifiableList(combine(activeAchievements, completedAchievements));
    }

    @Override
    public List<Achievement> activeArchivements() {
        return unmodifiableList(activeAchievements);
    }

    @Override
    public List<Achievement> completedArchivements() {
        return unmodifiableList(completedAchievements);
    }

    @Override
    public Achievements progess(final Class<? extends AchievementDefinition> achievementType, final int progress) {
        requireNonNull(achievementType, "achievementType must not be null");
        Validate.zeroOrPositive(progress, "progress must be positive");
        if (progress == 0) {
            return this;
        }
        final var archivmentsOfType = archivementsByClass.get(achievementType);
        if (isNull(archivmentsOfType)) {
            throw new IllegalArgumentException("achievement not present: " + achievementType.getSimpleName());
        }
        for (final var archivement : archivmentsOfType) {
            if (archivement.usesAutoProgression()) {
                throw new IllegalArgumentException("achievement %s uses automatic progression and cannot be updated manually".formatted(achievementType.getSimpleName()));
            }
            archivement.progress(progress);
        }
        return this;
    }

    @Override
    public Achievements addAllFromPackage(final String packageName) {
        Reflections.createInstancesFromPackage(packageName, AchievementDefinition.class).forEach(this::add);
        return this;
    }

    @Override
    public void reset() {
        activeAchievements.addAll(completedAchievements);
        completedAchievements.clear();
        for (final var archivement : activeAchievements) {
            archivement.reset();
        }
    }

    @Override
    public void update() {
        final boolean mustRefreshAbsoluteArchivements = lazyUpdateSheduler.isTick();

        for (final var activeArchivement : new ArrayList<>(activeAchievements)) {
            if (mustRefreshAbsoluteArchivements || !activeArchivement.progressionIsAbsolute()) {
                activeArchivement.autoProgress(engine);
            }
            if (activeArchivement.isCompleted()) {
                activeAchievements.remove(activeArchivement);
                completedAchievements.add(activeArchivement);
                activeArchivement.setCompleted(Time.now());
                onCompletion.accept(activeArchivement);
            }
        }
    }

}
