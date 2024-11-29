package io.github.srcimon.screwbox.core.archivements.internal;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.archivements.Archivement;
import io.github.srcimon.screwbox.core.archivements.ArchivementDefinition;
import io.github.srcimon.screwbox.core.archivements.ArchivementOptions;

public class ArchivementData implements Archivement {

    private ArchivementOptions options;
    private ArchivementDefinition definition;
    private int score = 0;

    public ArchivementData(final ArchivementDefinition definition, final ArchivementOptions options) {
        this.definition = definition;
        this.options = options;
    }

    @Override
    public String title() {
        return options.title();
    }

    @Override
    public int goal() {
        return options.goal();
    }

    @Override
    public boolean isArchived() {
        return score() >= goal();
    }

    public int score() {
        return score;
    }

    public void progress(final int progress) {
        setProgress(score + progress);
    }

    public void setProgress(final int progress) {
        score = Math.min(goal(),  progress);
    }

    public boolean isOfFamily(Class<? extends ArchivementDefinition> definition) {
        return definition.equals(options.family()) || this.definition.getClass().equals(definition);
    }

    public void autoProgress(Engine engine) {
        if(options.progressionMode().equals(ArchivementOptions.ProgressionMode.ADD)) {
            progress(definition.progress(engine));
        } else {
            setProgress(definition.progress(engine));
        }

    }
}
