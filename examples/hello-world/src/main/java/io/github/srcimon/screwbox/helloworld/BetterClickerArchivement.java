package io.github.srcimon.screwbox.helloworld;

import io.github.srcimon.screwbox.core.archivements.ArchivementDefinition;
import io.github.srcimon.screwbox.core.archivements.ArchivementOptions;

public class BetterClickerArchivement implements ArchivementDefinition {

    @Override
    public ArchivementOptions define() {
        return ArchivementOptions
                .title("better clicker")
                .description("click {goal} times like a boss")
                .family(BestClickerArchivement.class)
                .goal(40);
    }
}
