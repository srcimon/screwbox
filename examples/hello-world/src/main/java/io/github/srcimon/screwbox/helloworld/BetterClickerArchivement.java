package io.github.srcimon.screwbox.helloworld;

import io.github.srcimon.screwbox.core.archivements.ArchivementOptions;

public class BetterClickerArchivement extends BestClickerArchivement {

    @Override
    public ArchivementOptions defineArchivement() {
        return ArchivementOptions
                .title("better clicker")
                .description("click {goal} times like a boss")
                .family(BestClickerArchivement.class)
                .goal(40);
    }

}
