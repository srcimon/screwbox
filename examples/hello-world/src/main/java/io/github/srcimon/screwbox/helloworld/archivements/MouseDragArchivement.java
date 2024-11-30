package io.github.srcimon.screwbox.helloworld.archivements;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.archivements.Archivement;
import io.github.srcimon.screwbox.core.archivements.ArchivementConfiguration;

public class MouseDragArchivement implements Archivement {

    @Override
    public ArchivementConfiguration configuration() {
        return ArchivementConfiguration
                .title("Mouse travels a lot")
                .goal(20000);
    }

    @Override
    public int progress(Engine engine) {
       return  (int) engine.mouse().drag().length();
    }

}
