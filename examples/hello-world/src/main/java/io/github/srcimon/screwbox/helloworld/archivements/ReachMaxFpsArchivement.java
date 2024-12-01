package io.github.srcimon.screwbox.helloworld.archivements;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.archivements.Archivement;
import io.github.srcimon.screwbox.core.archivements.ArchivementConfiguration;

public class ReachMaxFpsArchivement implements Archivement {

    @Override
    public ArchivementConfiguration configuration() {
        return ArchivementConfiguration
                .title("Reach max fps")
                .goal(150)
                .useFixedProgressMode()
                .useLazyRefresh();
    }

    @Override
    public int progress(Engine engine) {
        return engine.loop().fps();
    }
}
