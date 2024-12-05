package io.github.srcimon.screwbox.core.archivements.internal;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.archivements.Archivement;
import io.github.srcimon.screwbox.core.archivements.ArchivementDetails;
import io.github.srcimon.screwbox.core.archivements.ArchivementStatus;

import java.util.Objects;
import java.util.Optional;

public class DefaultArchivementStatus implements ArchivementStatus {

    private final Archivement archivement;
    private final ArchivementDetails configuration;
    private int score = 0;
    private final Time startTime;
    private Time completionTime;

    public DefaultArchivementStatus(final Archivement archivement) {
        this.configuration = archivement.details();
        this.archivement = archivement;
        this.startTime = Time.now();
        this.completionTime = Time.unset();
    }

    @Override
    public String title() {
        return resolvePlaceholders(configuration.title());
    }

    @Override
    public Optional<String> description() {
        return Objects.isNull(configuration.description())
                ? Optional.empty()
                : Optional.of(resolvePlaceholders(configuration.description()));
    }

    @Override
    public int goal() {
        return configuration.goal();
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
    public int score() {
        return score;
    }

    //TODO reduce interaction between this and calling class
    public void progress(final int progress) {
        setProgress(configuration.isCumulativeProgression() ? score +progress : progress);
    }

    public void setProgress(final int progress) {//TODO return boolean of status
        score = Math.min(goal(), progress);
        if(progress == goal()) {
            completionTime = Time.now();
        }
    }

    public boolean isOfFamily(Class<? extends Archivement> definition) {
        return this.archivement.getClass().equals(definition) || //TODO needed?
                this.archivement.getClass().isAssignableFrom(definition);
    }

    public int autoProgress(Engine engine) {
        return archivement.progress(engine);
    }

    public boolean isLazy() {
        return configuration.isLazyRefresh();
    }

    private String resolvePlaceholders(final String value) {
        return value.replace("{goal}", String.valueOf(configuration.goal()));
    }
}
