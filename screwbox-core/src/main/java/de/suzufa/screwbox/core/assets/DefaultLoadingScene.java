package de.suzufa.screwbox.core.assets;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Entities;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Pixelfont;
import de.suzufa.screwbox.core.scenes.Scene;

public class DefaultLoadingScene implements Scene {

    private Class<? extends Scene> nextScene;

    public DefaultLoadingScene(Class<? extends Scene> nextScene) {
        this.nextScene = nextScene;
    }

    @Override
    public void initialize(Entities entities) {
        entities.add(new EntitySystem() {

            @Override
            public void update(Engine engine) {
                if (engine.assets().isLoadingInProgress()) {
                    engine.graphics().window().drawTextCentered(engine.graphics().window().center(),
                            "loading game assets",
                            Pixelfont.defaultFont(Color.WHITE), 2);
                    engine.graphics().window().drawTextCentered(engine.graphics().window().center().addY(40),
                            engine.assets().loadingProgress().currentTask(),
                            Pixelfont.defaultFont(Color.WHITE), 1.5);
                } else {
                    engine.scenes().switchTo(nextScene);
                }
            }
        });
    }
}
