package io.github.srcimon.screwbox.helloworld;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.light.GlowComponent;
import dev.screwbox.core.environment.light.PointLightComponent;
import dev.screwbox.core.environment.particles.ParticleEmitterComponent;
import dev.screwbox.core.environment.particles.ParticleInteractionComponent;
import dev.screwbox.core.environment.physics.CursorAttachmentComponent;
import dev.screwbox.core.graphics.Color;

import static dev.screwbox.core.assets.FontBundle.BOLDZILLA;
import static dev.screwbox.core.environment.Order.SystemOrder.PRESENTATION_BACKGROUND;
import static dev.screwbox.core.graphics.options.TextDrawOptions.font;
import static dev.screwbox.core.particles.ParticlesBundle.FALLING_LEAVES;

public class HelloWorldApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Hello World");

        // set ambient light to nearly full brightness
        screwBox.graphics().light().setAmbientLight(Percent.of(0.98));

        // set good shadow quality
        screwBox.graphics().configuration().setLightmapScale(2);

        screwBox.environment()
                // enable all features that are used below...
                .enableAllFeatures()

                // draw Hello World
                .addSystem(PRESENTATION_BACKGROUND, engine -> {
                    var canvas = engine.graphics().canvas();
                    var drawOptions = font(BOLDZILLA).scale(6).alignCenter();
                    canvas.fillWith(Color.hex("#125d7e"));
                    canvas.drawText(canvas.center(), "Hello World!", drawOptions);
                })

                // add light spot to create nice sunlight effect
                .addEntity("sun", new PointLightComponent(800, Color.BLACK),
                        new GlowComponent(800, Color.YELLOW.opacity(0.1)),
                        new TransformComponent())

                // add falling leaves
                .addEntity("falling leaves",
                        new TransformComponent(screwBox.graphics().visibleArea()),
                        new ParticleEmitterComponent(Duration.ofMillis(250), FALLING_LEAVES))

                // let the mouse interact with the falling leaves
                .addEntity("cursor",
                        new TransformComponent(),
                        new CursorAttachmentComponent(),
                        new ParticleInteractionComponent(80, Percent.max()));

        screwBox.start();
    }
}