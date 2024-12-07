package io.github.srcimon.screwbox.platformer.archivements;

import io.github.srcimon.screwbox.core.archivements.ArchivementDefinition;
import io.github.srcimon.screwbox.core.archivements.ArchivementDetails;

public class Collect10ItemsArchivement implements ArchivementDefinition {

    @Override
    public ArchivementDetails details() {
        return ArchivementDetails.title("collect {goal} items")
                .description("collect cherries and D.E.B.O.")
                .goal(10);
    }
}
