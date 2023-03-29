package io.github.simonbas.screwbox.examples.pathfinding.scenes;

import io.github.simonbas.screwbox.core.Duration;
import io.github.simonbas.screwbox.core.entities.Entities;
import io.github.simonbas.screwbox.core.graphics.Color;
import io.github.simonbas.screwbox.core.graphics.Pixelfont;
import io.github.simonbas.screwbox.core.scenes.Scene;

//TODO: Start any Engine with Loading Scene?
public class LoadingScene implements Scene {

    @Override
    public void initialize(Entities entities) {
        entities.add(engine -> {
            engine.graphics().screen().drawTextCentered(engine.graphics().screen().center(), "loading...", Pixelfont.defaultFont(Color.WHITE));
            if (engine.loop().updateDuration().isLessThan(Duration.ofMicros(5))) {
                engine.scenes().switchTo(DemoScene.class);
            }
        });
    }
}
