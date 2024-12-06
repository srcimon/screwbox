package io.github.srcimon.screwbox.core.archivements.internal;

import io.github.srcimon.screwbox.core.archivements.Archivement;
import io.github.srcimon.screwbox.core.log.Log;

import java.util.function.Consumer;

public class LoggingArchivementOnCompletion implements Consumer<Archivement> {

    private final Log log;

    public LoggingArchivementOnCompletion(final Log log) {
        this.log = log;
    }

    @Override
    public void accept(final Archivement archivement) {
        log.info("completed archivement '%s' (%s)".formatted(
                archivement.title(),
                archivement.description().orElse("no description")));
    }
}
