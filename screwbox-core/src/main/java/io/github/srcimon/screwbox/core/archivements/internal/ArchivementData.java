package io.github.srcimon.screwbox.core.archivements.internal;

import io.github.srcimon.screwbox.core.archivements.Archivement;
import io.github.srcimon.screwbox.core.archivements.ArchivementDefinition;
import io.github.srcimon.screwbox.core.archivements.ArchivementOptions;

public class ArchivementData implements Archivement {

    private ArchivementOptions options;
    private int score = 0;
    private boolean isLocked;

    public ArchivementData(final ArchivementOptions options) {
        this.options = options;
        this.isLocked = options.predecessor() != null;
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
        score = Math.min(goal(), score + progress);
    }

    public boolean isLocked() {
        return isLocked;
    }

    public Class<? extends ArchivementDefinition> predecessor() {
        return options.predecessor();
    }

    public void unlock(final int score) {
        this.isLocked = false;
        this.score = score;
    }
}
