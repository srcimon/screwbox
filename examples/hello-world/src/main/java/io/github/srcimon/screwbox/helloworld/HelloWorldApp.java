package io.github.srcimon.screwbox.helloworld;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.environment.core.LogFpsSystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.light.GlowComponent;
import io.github.srcimon.screwbox.core.environment.light.PointLightComponent;
import io.github.srcimon.screwbox.core.environment.light.ShadowCasterComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.particles.ParticleOptionsBundle;

import static io.github.srcimon.screwbox.core.assets.FontBundle.BOLDZILLA;
import static io.github.srcimon.screwbox.core.environment.Order.SystemOrder.PRESENTATION_BACKGROUND;
import static io.github.srcimon.screwbox.core.graphics.TextDrawOptions.font;

public class HelloWorldApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Hello World");

        // set ambient light to nearly full brightness
        screwBox.graphics().light().setAmbientLight(Percent.of(0.96));

        // set good shadow quality
        screwBox.graphics().configuration().setLightmapScale(2);

        screwBox.environment()

                // enable all features
                .enableAllFeatures()

                // add light spot to create shadow effect
                .addEntity("sun", new PointLightComponent(800, Color.BLACK),
                        new GlowComponent(800, Color.YELLOW.opacity(0.1)),
                        new TransformComponent())

                // draw Hello World
                .addSystem(PRESENTATION_BACKGROUND, engine -> {
                    var screen = engine.graphics().screen();
                    var drawOptions = font(BOLDZILLA).scale(6).alignCenter();
                    screen.fillWith(Color.hex("#125d7e"));
                    screen.drawText(screen.center(), "Hello world!", drawOptions);
                })

                // add falling leaves
                .addEntity("falling leaves",
                        new TransformComponent(screwBox.graphics().world().visibleArea().expand(400)),
                        new ParticleEmitterComponent(Duration.ofMillis(60), ParticleOptionsBundle.FALLING_LEAVES.get()
                                .customize("x", entity -> entity.add(new ShadowCasterComponent(false)))));

        screwBox.start();
    }
}