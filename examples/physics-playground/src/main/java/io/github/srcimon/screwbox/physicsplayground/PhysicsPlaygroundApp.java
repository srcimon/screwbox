package io.github.srcimon.screwbox.physicsplayground;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.scenes.SceneTransition;
import io.github.srcimon.screwbox.core.scenes.animations.ColorFadeAnimation;
import io.github.srcimon.screwbox.physicsplayground.scenes.PlaygroundScene;

public class PhysicsPlaygroundApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Physics Playground");

        screwBox.graphics().camera().setZoom(5);
        screwBox.scenes()
                .setDefaultTransition(SceneTransition.custom()
                        .introAnimation(new ColorFadeAnimation())
                        .introDurationMillis(500))
                .add(new PlaygroundScene())
                .switchTo(PlaygroundScene.class);

        screwBox.start();
        //TODO add to readme
    }
}