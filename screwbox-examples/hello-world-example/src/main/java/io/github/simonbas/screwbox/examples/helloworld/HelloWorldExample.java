package io.github.simonbas.screwbox.examples.helloworld;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.ScrewBox;
import io.github.simonbas.screwbox.core.entities.systems.QuitOnKeyPressSystem;
import io.github.simonbas.screwbox.core.graphics.MouseCursor;
import io.github.simonbas.screwbox.core.keyboard.Key;
import io.github.simonbas.screwbox.examples.helloworld.systems.PrintHelloWorldSystem;

public class HelloWorldExample {

    public static void main(String[] args) {
        // create the engine
        Engine engine = ScrewBox.createEngine("Hello World Example");

        // hide cursor in window mode
        engine.window().setWindowCursor(MouseCursor.HIDDEN);

        // add systems to the game loop
        engine.entities()
                .add(new PrintHelloWorldSystem()) // print hello world next to mouse position
                .add(new QuitOnKeyPressSystem(Key.ESCAPE)); // quits the engine on pressing ESC

        engine.start(); // start the previously configured engine
    }
}
