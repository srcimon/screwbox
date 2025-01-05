package io.github.srcimon.screwbox.playgrounds.playercontrolls;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;

//TODO add playgrounds to readme.md
public class PlayerControlsApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Player Controls");
        screwBox.graphics().camera().setZoom(4);

        screwBox.scenes()
                .add(new GameScene())
                .switchTo(GameScene.class);

        screwBox.start();
    }
}