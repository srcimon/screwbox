package io.github.srcimon.screwbox.helloworld;

import io.github.srcimon.screwbox.core.archivements.ArchivementDefinition;
import io.github.srcimon.screwbox.core.archivements.ArchivementOptions;

public class BestClickerArchivement implements ArchivementDefinition {

    @Override
    public ArchivementOptions define() {
        return ArchivementOptions
                .title("best clicker")
                .description("click {goal} times like a boss")
                .onComplete()
                .goal(10);
    }
}
