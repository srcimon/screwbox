package dev.screwbox.core.achievements.internal;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Time;
import dev.screwbox.core.achievements.Achievement;
import dev.screwbox.core.achievements.AchievementDefinition;
import dev.screwbox.core.achievements.AchievementDetails;
import dev.screwbox.core.graphics.Sprite;

import java.util.Objects;
import java.util.Optional;

class DefaultAchievement implements Achievement {

    private final AchievementDefinition achievementDefinition;
    private final AchievementDetails details;
    private final boolean usesAutoProgression;
    private final Time startTime;
    private int score = 0;
    private Time completionTime;

    DefaultAchievement(final AchievementDefinition achievementDefinition) {
        this.details = achievementDefinition.details();
        this.achievementDefinition = achievementDefinition;
        this.startTime = Time.now();
        this.completionTime = Time.unset();
        usesAutoProgression = hasProgressMethod(achievementDefinition.getClass());
    }

    @Override
    public String title() {
        return resolvePlaceholders(details.title());
    }

    @Override
    public Optional<String> description() {
        return Objects.isNull(details.description())
                ? Optional.empty()
                : Optional.of(resolvePlaceholders(details.description()));
    }

    @Override
    public int goal() {
        return details.goal();
    }

    @Override
    public boolean isCompleted() {
        return score() >= goal();
    }

    @Override
    public Percent progress() {
        return Percent.of((double) score / goal());
    }

    @Override
    public Time startTime() {
        return startTime;
    }

    @Override
    public Time completionTime() {
        return completionTime;
    }

    @Override
    public Optional<Sprite> icon() {
        return details.icon();
    }

    @Override
    public int score() {
        return score;
    }

    public boolean usesAutoProgression() {
        return usesAutoProgression;
    }

    public void progress(final int progress) {
        score = Math.min(goal(), details.progressionIsAbsolute() ? progress : score + progress);
    }

    public void autoProgress(Engine engine) {
        progress(achievementDefinition.progress(engine));
    }

    public boolean progressionIsAbsolute() {
        return details.progressionIsAbsolute();
    }

    private String resolvePlaceholders(final String value) {
        return value.replace("{goal}", String.valueOf(details.goal()));
    }

    public void setCompleted(final Time time) {
        completionTime = time;
    }

    private static boolean hasProgressMethod(final Class<? extends AchievementDefinition> clazz) {
        try {
            clazz.getDeclaredMethod("progress", Engine.class);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public void reset() {
        score = 0;
        completionTime = Time.unset();
    }
}
