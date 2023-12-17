package io.github.srcimon.screwbox.examples.helloworld;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Sprite;

import static io.github.srcimon.screwbox.core.graphics.Pixelfont.defaultFont;

public class HelloWorldExample {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Hello World Example");

        screwBox.environment().setup().enableRendering().enablePhysics();
        screwBox.environment().addEntity(
                new TransformComponent(Vector.zero()),
                new RenderComponent(Sprite.fromFile("default_font.png").replaceColor(Color.BLACK, Color.RED)));


        screwBox.start();
    }
}
