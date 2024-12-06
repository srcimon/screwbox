package io.github.srcimon.screwbox.core.archivements.internal;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.archivements.Archivement;
import io.github.srcimon.screwbox.core.archivements.ArchivementDefinition;
import io.github.srcimon.screwbox.core.archivements.ArchivementDetails;

import java.util.Objects;
import java.util.Optional;

class DefaultArchivement implements Archivement {

    private final ArchivementDefinition archivementDefinition;
    private final ArchivementDetails details;
    private int score = 0;
    private final Time startTime;
    private Time completionTime;

    public DefaultArchivement(final ArchivementDefinition archivementDefinition) {
        this.details = archivementDefinition.details();
        this.archivementDefinition = archivementDefinition;
        this.startTime = Time.now();
        this.completionTime = Time.unset();
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
    public int score() {
        return score;
    }

    public void progress(final int progress) {
        score = Math.min(goal(), details.progressionIsAbsolute() ? score + progress : progress);
        if (score == goal()) {
            completionTime = Time.now();
        }
    }

    public int autoProgress(Engine engine) {
        return archivementDefinition.progress(engine);
    }

    public boolean canBeUpdatedLazy() {
        return details.progressionIsAbsolute();
    }

    private String resolvePlaceholders(final String value) {
        return value.replace("{goal}", String.valueOf(details.goal()));
    }
}
