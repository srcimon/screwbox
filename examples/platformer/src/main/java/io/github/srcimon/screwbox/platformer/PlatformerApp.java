package io.github.srcimon.screwbox.platformer;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.ui.WobblyUiLayouter;
import io.github.srcimon.screwbox.platformer.scenes.PauseScene;
import io.github.srcimon.screwbox.platformer.scenes.StartScene;

public class PlatformerApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Platformer");

        engine.audio().configuration().setSoundRange(200);

        //TODO FIX BUG WHEN SCREEN OFFSET WITH ROTATION IS ACTIVe
engine.graphics().screen().setRotation(Rotation.degrees(-10)).setCanvasBounds(new ScreenBounds(100, 50, 900, 500));
        engine.graphics().camera()
                .setZoomRestriction(2, 5)
                .setZoom(3.0);

        engine.ui().setLayouter(new WobblyUiLayouter());

        engine.assets()
                .enableLogging()
                .prepareEngineAssetsAsync()
                .prepareClassPackageAsync(PlatformerApp.class);

        engine.scenes()
                .add(new PauseScene())
                .add(new StartScene())
                .switchTo(StartScene.class);

        engine.start();
    }
}