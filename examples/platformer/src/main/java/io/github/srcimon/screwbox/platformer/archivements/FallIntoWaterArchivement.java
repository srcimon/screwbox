package io.github.srcimon.screwbox.platformer.archivements;

import io.github.srcimon.screwbox.core.archivements.ArchivementDefinition;
import io.github.srcimon.screwbox.core.archivements.ArchivementDetails;

public class FallIntoWaterArchivement implements ArchivementDefinition {

    @Override
    public ArchivementDetails details() {
        return ArchivementDetails.title("fall into water");
    }
}
