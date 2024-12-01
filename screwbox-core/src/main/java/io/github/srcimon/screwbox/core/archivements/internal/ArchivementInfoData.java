package io.github.srcimon.screwbox.core.archivements.internal;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.archivements.Archivement;
import io.github.srcimon.screwbox.core.archivements.ArchivementConfiguration;
import io.github.srcimon.screwbox.core.archivements.ArchivementInfo;

public class ArchivementInfoData implements ArchivementInfo {

    private ArchivementConfiguration options;
    private Archivement archivement;
    private int score = 0;

    public ArchivementInfoData(final Archivement archivement) {
        this.archivement = archivement;
        this.options = archivement.configuration();
    }

    @Override
    public String title() {
        return options.title();
    }

    @Override
    public String description() {
        return options.description();
    }

    @Override
    public int goal() {
        return options.goal();
    }

    @Override
    public boolean isCompleted() {
        return score() >= goal();
    }

    @Override
    public Percent progress() {
        return Percent.of((double) score / goal());
    }

    public int score() {
        return score;
    }

    public void progress(final int progress) {
        if (options.isFixedProgressMode()) {
            setProgress(progress);
        } else {
            setProgress(score + progress);
        }
    }

    public void setProgress(final int progress) {
        score = Math.min(goal(), progress);
    }

    public boolean isOfFamily(Class<? extends Archivement> definition) {
        return  this.archivement.getClass().equals(definition) || this.archivement.getClass().isAssignableFrom(definition.getClass());
    }

    public int autoProgress(Engine engine) {
        return archivement.progress(engine);
    }

    public boolean isLazy() {
        return options.usesLazyRefresh();
    }
}
