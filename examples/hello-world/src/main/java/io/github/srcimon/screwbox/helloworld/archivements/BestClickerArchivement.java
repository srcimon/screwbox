package io.github.srcimon.screwbox.helloworld.archivements;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.archivements.Archivement;
import io.github.srcimon.screwbox.core.archivements.ArchivementConfiguration;
import io.github.srcimon.screwbox.core.mouse.MouseButton;

public class BestClickerArchivement implements Archivement {

    @Override
    public ArchivementConfiguration configuration() {
        return ArchivementConfiguration
                .title("best clicker")
                .description("click {goal} times like a boss")
                .goal(10);
    }

    @Override
    public int progress(final Engine engine) {
        return engine.mouse().isPressedLeft() ? 1 : 0;
    }
}
