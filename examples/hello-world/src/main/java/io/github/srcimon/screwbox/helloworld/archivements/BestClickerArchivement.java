package io.github.srcimon.screwbox.helloworld.archivements;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.archivements.ArchivementDefinition;
import io.github.srcimon.screwbox.core.archivements.ArchivementOptions;
import io.github.srcimon.screwbox.core.mouse.MouseButton;

public class BestClickerArchivement implements ArchivementDefinition {

    @Override
    public ArchivementOptions defineArchivement() {
        return ArchivementOptions
                .title("best clicker")
                .description("click {goal} times like a boss")
                .goal(10);
    }

    @Override
    public int progress(final Engine engine) {
        return engine.mouse().isPressed(MouseButton.LEFT) ? 1 : 0;
    }
}
