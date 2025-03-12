package io.github.srcimon.screwbox.core.achievements.internal;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.achievements.Achievement;
import io.github.srcimon.screwbox.core.achievements.AchievementDefinition;
import io.github.srcimon.screwbox.core.achievements.Achievements;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.utils.Reflections;
import io.github.srcimon.screwbox.core.utils.Scheduler;
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

    private final Scheduler lazyUpdateScheduler = Scheduler.withInterval(Duration.ofMillis(500));
    private final Engine engine;
    private final Map<Class<? extends AchievementDefinition>, List<DefaultAchievement>> achievementsByClass = new HashMap<>();
    private final List<DefaultAchievement> activeAchievements = new ArrayList<>();
    private final List<DefaultAchievement> completedAchievements = new ArrayList<>();
    private Consumer<Achievement> onCompletion;

    public DefaultAchievements(final Engine engine, Consumer<Achievement> onCompletion) {
        this.engine = engine;
        this.onCompletion = onCompletion;
    }

    @Override
    public Achievements add(final AchievementDefinition achievement) {
        requireNonNull(achievement, "achievement must not be null");
        final var defaultAchievement = new DefaultAchievement(achievement);
        final var achievementClazz = achievement.getClass();
        activeAchievements.add(defaultAchievement);
        final var achievementsOfClazz = achievementsByClass.get(achievementClazz);

        if (isNull(achievementsOfClazz)) {
            achievementsByClass.put(achievementClazz, new ArrayList<>(List.of(defaultAchievement)));
        } else {
            achievementsOfClazz.add(defaultAchievement);
        }
        return this;
    }

    @Override
    public List<Achievement> allAchievements() {
        return unmodifiableList(combine(activeAchievements, completedAchievements));
    }

    @Override
    public List<Achievement> activeAchievements() {
        return unmodifiableList(activeAchievements);
    }

    @Override
    public List<Achievement> completedAchievements() {
        return unmodifiableList(completedAchievements);
    }

    @Override
    public Achievements progress(final Class<? extends AchievementDefinition> achievementType, final int progress) {
        requireNonNull(achievementType, "achievementType must not be null");
        Validate.zeroOrPositive(progress, "progress must be positive");
        if (progress == 0) {
            return this;
        }
        final var achievementsOfType = achievementsByClass.get(achievementType);
        if (isNull(achievementsOfType)) {
            throw new IllegalArgumentException("achievement not present: " + achievementType.getSimpleName());
        }
        for (final var achievement : achievementsOfType) {
            if (achievement.usesAutoProgression()) {
                throw new IllegalArgumentException("achievement %s uses automatic progression and cannot be updated manually".formatted(achievementType.getSimpleName()));
            }
            achievement.progress(progress);
        }
        return this;
    }

    @Override
    public Achievements addAllFromPackage(final String packageName) {
        Reflections.createInstancesFromPackage(packageName, AchievementDefinition.class).forEach(this::add);
        return this;
    }

    @Override
    public Achievements setCompletionReaction(Consumer<Achievement> onCompletion) {
        this.onCompletion = requireNonNull(onCompletion, "reaction must not be null");
        return this;
    }

    @Override
    public void reset() {
        activeAchievements.addAll(completedAchievements);
        completedAchievements.clear();
        for (final var achievement : activeAchievements) {
            achievement.reset();
        }
    }

    @Override
    public void update() {

        final boolean mustRefreshAbsoluteAchievements = lazyUpdateScheduler.isTick();

        for (final var activeAchievement : new ArrayList<>(activeAchievements)) {
            if (mustRefreshAbsoluteAchievements || !activeAchievement.progressionIsAbsolute()) {
                activeAchievement.autoProgress(engine);
            }
            if (activeAchievement.isCompleted()) {
                activeAchievements.remove(activeAchievement);
                completedAchievements.add(activeAchievement);
                activeAchievement.setCompleted(Time.now());
                onCompletion.accept(activeAchievement);
            }
        }
    }

}
