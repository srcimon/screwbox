package io.github.srcimon.screwbox.core.archivements.internal;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.archivements.Archivement;
import io.github.srcimon.screwbox.core.archivements.ArchivementInfo;

import static java.util.Objects.isNull;

public class DefaultArchivementInfo implements ArchivementInfo {

    private Archivement archivement;//TODO get rid of
    private final String title;
    private final String description;
    private final int goal;
    private int score = 0;
    private boolean isFixedProgress;
    private boolean isLazy;
    private Time startTime;
    private Time completionTime;

    public DefaultArchivementInfo(final Archivement archivement) {
        final var configuration = archivement.configuration();
        this.archivement = archivement;
        this.goal = configuration.goal();
        this.title = resolvePlaceholders(configuration.title());
        this.description = resolvePlaceholders(configuration.description());
        this.isLazy = configuration.usesLazyRefresh();
        this.isFixedProgress = configuration.isFixedProgressMode();
        this.startTime = Time.now();
        this.completionTime = Time.unset();
    }

    private String resolvePlaceholders(final String value) {
        return isNull(value)
                ? null
                : value.replace("{goal}", String.valueOf(goal));
    }

    @Override
    public String title() {//TODO optional?
        return title;
    }

    @Override
    public String description() {//TODO optional?
        return description;
    }

    @Override
    public int goal() {
        return goal;
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

    public int score() {
        return score;
    }

    public void progress(final int progress) {
        setProgress(isFixedProgress ? progress : score + progress);
    }

    public void setProgress(final int progress) {
        score = Math.min(goal(), progress);
    }

    public boolean isOfFamily(Class<? extends Archivement> definition) {
        return this.archivement.getClass().equals(definition) || this.archivement.getClass().isAssignableFrom(definition.getClass());
    }

    public int autoProgress(Engine engine) {
        return archivement.progress(engine);
    }

    public boolean isLazy() {
        return isLazy;
    }
}
