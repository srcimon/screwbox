package io.github.srcimon.screwbox.helloworld;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent;
import io.github.srcimon.screwbox.core.particles.ParticleOptionsBundle;

import static io.github.srcimon.screwbox.core.assets.FontBundle.BOLDZILLA;
import static io.github.srcimon.screwbox.core.environment.Order.SystemOrder.PRESENTATION_BACKGROUND;
import static io.github.srcimon.screwbox.core.graphics.TextDrawOptions.font;

public class HelloWorldApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Hello World");

        screwBox.environment()

                // enable needed features
                .enableRendering()
                .enablePhysics()
                .enableParticles()
                .enableTweening()

                // draw Hello World
                .addSystem(PRESENTATION_BACKGROUND, engine -> {
                    var screen = engine.graphics().screen();
                    var drawOptions = font(BOLDZILLA).scale(4).alignCenter();
                    screen.drawText(screen.center(), "Hello world!", drawOptions);
                })

                // add falling leaves
                .addEntity("falling leaves",
                        new TransformComponent(screwBox.graphics().world().visibleArea().expand(400)),
                        new ParticleEmitterComponent(Duration.ofMillis(60), ParticleOptionsBundle.FALLING_LEAVES));

        screwBox.start();
    }
}