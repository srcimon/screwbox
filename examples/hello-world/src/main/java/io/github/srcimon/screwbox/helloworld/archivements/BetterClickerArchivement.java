package io.github.srcimon.screwbox.helloworld.archivements;

import io.github.srcimon.screwbox.core.archivements.ArchivementDetails;

public class BetterClickerArchivement extends BestClickerArchivement {

    @Override
    public ArchivementDetails details() {
        return ArchivementDetails
                .title("better clicker")
                .description("click {goal} times like a boss")
                .family(BestClickerArchivement.class)
                .goal(40);
    }

}
