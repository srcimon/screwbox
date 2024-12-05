package io.github.srcimon.screwbox.core.archivements.internal;

import io.github.srcimon.screwbox.core.archivements.ArchivementStatus;
import io.github.srcimon.screwbox.core.log.Log;

import java.util.function.Consumer;

public class DefaultCompletionReaction implements Consumer<ArchivementStatus> {

    private final Log log;

    public DefaultCompletionReaction(final Log log) {
        this.log = log;
    }

    @Override
    public void accept(final ArchivementStatus archivement) {
        log.info("completed archivement '%s' (%s)".formatted(
                archivement.title(),
                archivement.description().orElse("no description")));
    }
}
