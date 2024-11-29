package io.github.srcimon.screwbox.core.archivements;

import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;

import java.util.List;

public interface Archivements {

    class MyArchivement implements ArchivementDefinition {

        //TODO buildForGoals(10,20,40)

        private static final Asset<Sprite> ICON = SpriteBundle.ICON_LARGE.asset();

        @Override
        public ArchivementOptions define() {
            return ArchivementOptions
                    .title("best clicker")
                    .description("click {goal} times like a boss")
                    .icon(ICON)
                    .goal(10);
        }
    }

    Archivements define(ArchivementDefinition archivement);

    Archivements reset(Clazz<? extends ArchivementDefinition> definition);

    Archivements complete(Clazz<? extends ArchivementDefinition> definition);

    Archivements unlock(Clazz<? extends ArchivementDefinition> definition);

    List<Archivement> get(Clazz<? extends ArchivementDefinition> definition);
}
