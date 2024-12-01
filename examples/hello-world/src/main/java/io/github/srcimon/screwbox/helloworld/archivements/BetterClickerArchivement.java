package io.github.srcimon.screwbox.helloworld.archivements;

import io.github.srcimon.screwbox.core.archivements.ArchivementConfiguration;

public class BetterClickerArchivement extends BestClickerArchivement {

    @Override
    public ArchivementConfiguration configuration() {
        return super.configuration()
                .goal(40);
    }

}
