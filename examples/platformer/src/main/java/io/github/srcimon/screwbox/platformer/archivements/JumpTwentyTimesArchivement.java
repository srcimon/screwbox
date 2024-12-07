package io.github.srcimon.screwbox.platformer.archivements;

import io.github.srcimon.screwbox.core.archivements.ArchivementDefinition;
import io.github.srcimon.screwbox.core.archivements.ArchivementDetails;

public class JumpTwentyTimesArchivement implements ArchivementDefinition {

    @Override
    public ArchivementDetails details() {
        return ArchivementDetails.title("jump {goal} times")
                .description("press SPACE to jump {goal} times")
                .goal(20);
    }
}
