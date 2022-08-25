package de.suzufa.screwbox.examples.helloworld;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.ScrewBox;
import de.suzufa.screwbox.core.keyboard.Key;
import de.suzufa.screwbox.examples.helloworld.systems.PrintHelloWorldSystem;
import de.suzufa.screwbox.examples.helloworld.systems.QuitOnKeystrokeSystem;

public class HelloWorldExample {

    public static void main(String[] args) {
        // create the engine
        Engine engine = ScrewBox.createEngine();

        engine.graphics().window().setTitle("Hello World Example");// TODO: engine Name ScrewBox.createEngine("name")

        // add systems to the game loop
        engine.entityEngine().add(
                new PrintHelloWorldSystem(), // print hello world next to mouse position
                new QuitOnKeystrokeSystem(Key.ESCAPE)); // quits the engine on pressing ESC

        engine.start(); // start the previously configured engine
    }
}
