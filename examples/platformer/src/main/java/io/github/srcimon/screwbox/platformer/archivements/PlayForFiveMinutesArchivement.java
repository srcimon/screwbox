package io.github.srcimon.screwbox.platformer.archivements;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.archivements.ArchivementDefinition;
import io.github.srcimon.screwbox.core.archivements.ArchivementDetails;

public class PlayForFiveMinutesArchivement implements ArchivementDefinition {

    @Override
    public ArchivementDetails details() {
        return ArchivementDetails.title("play the game for 5 minutes")
                .useAbsoluteProgression()
                .goal(300);
    }

    @Override
    public int progress(Engine engine) {
        return (int) engine.loop().runningTime().seconds();
    }
}
