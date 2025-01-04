package io.github.srcimon.screwbox.playgrounds.playercontrolls;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.scene.GameScene;

//TODO add playgrounds to readme.md
public class PlayerControlsApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Player Controls");

        screwBox.graphics().camera().setZoom(3);

        screwBox.scenes()
                .add(new GameScene())
                .switchTo(GameScene.class);

        screwBox.start();
    }
}