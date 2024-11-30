package io.github.srcimon.screwbox.helloworld.archivements;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.archivements.Archivement;
import io.github.srcimon.screwbox.core.archivements.ArchivementDetails;

public class ReachMaxFpsArchivement implements Archivement {

    @Override
    public ArchivementDetails details() {
        return ArchivementDetails.title("reach max fps")
                .goal(150)
                .useFixedProgressMode();
    }

    @Override
    public int progress(Engine engine) {
        return engine.loop().fps();
    }
}
