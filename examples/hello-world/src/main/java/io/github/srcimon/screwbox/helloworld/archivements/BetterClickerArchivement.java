package io.github.srcimon.screwbox.helloworld.archivements;

import io.github.srcimon.screwbox.core.archivements.ArchivementConfiguration;

public class BetterClickerArchivement extends BestClickerArchivement {

    @Override
    public ArchivementConfiguration configuration() {
        return ArchivementConfiguration
                .title("Be an even better clicker")
                .description("click {goal} times like a boss")
                .family(BestClickerArchivement.class)
                .goal(40);
    }

}
