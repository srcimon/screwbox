package io.github.srcimon.screwbox.helloworld;

import io.github.srcimon.screwbox.core.archivements.ArchivementDefinition;
import io.github.srcimon.screwbox.core.archivements.ArchivementOptions;

import java.util.Optional;

public class BestClickerArchivement implements ArchivementDefinition {

    @Override
    public ArchivementOptions defineArchivement() {
        return ArchivementOptions
                .title("best clicker")
                .description("click {goal} times like a boss")
                .goal(10);
    }
}
