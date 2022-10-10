package de.suzufa.screwbox.examples.helloworld;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.ScrewBox;
import de.suzufa.screwbox.core.entities.systems.QuitOnKeyPressSystem;
import de.suzufa.screwbox.core.graphics.PredefinedCursor;
import de.suzufa.screwbox.core.keyboard.Key;
import de.suzufa.screwbox.examples.helloworld.systems.PrintHelloWorldSystem;

public class HelloWorldExample {

    public static void main(String[] args) {
        // create the engine
        Engine engine = ScrewBox.createEngine("Hello World Example");

        // hide cursor in window mode
        engine.graphics().window().setWindowCursor(PredefinedCursor.HIDDEN);

        // add systems to the game loop
        engine.entities()
                .add(new PrintHelloWorldSystem()) // print hello world next to mouse position
                .add(new QuitOnKeyPressSystem(Key.ESCAPE)); // quits the engine on pressing ESC

        engine.start(); // start the previously configured engine
    }
}
