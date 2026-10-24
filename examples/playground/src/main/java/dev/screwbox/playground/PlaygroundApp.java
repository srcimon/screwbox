package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;

public class PlaygroundApp {


    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");
        screwBox.scenes().add(new HeavyLoadScene()).switchTo(HeavyLoadScene.class);
        screwBox.start();
    }
}