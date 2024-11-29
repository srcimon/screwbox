package io.github.srcimon.screwbox.helloworld;

import io.github.srcimon.screwbox.core.archivements.ArchivementDefinition;
import io.github.srcimon.screwbox.core.archivements.ArchivementOptions;

public class EvenBetterClickerArchivement implements ArchivementDefinition {

    @Override
    public ArchivementOptions define() {
        return ArchivementOptions
                .title("even better clicker")
                .description("click {goal} times like a boss")
                .goal(20)
                .predecessor(BestClickerArchivement.class);
    }
}
