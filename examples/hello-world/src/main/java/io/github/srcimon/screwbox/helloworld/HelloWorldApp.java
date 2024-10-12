package io.github.srcimon.screwbox.helloworld;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.light.GlowComponent;
import io.github.srcimon.screwbox.core.environment.light.PointLightComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleInteractionComponent;
import io.github.srcimon.screwbox.core.environment.physics.CursorAttachmentComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.drawoptions.CircleDrawOptions;
import io.github.srcimon.screwbox.core.keyboard.Key;

import static io.github.srcimon.screwbox.core.assets.FontBundle.BOLDZILLA;
import static io.github.srcimon.screwbox.core.environment.Order.SystemOrder.PRESENTATION_BACKGROUND;
import static io.github.srcimon.screwbox.core.environment.Order.SystemOrder.PRESENTATION_UI_FOREGROUND;
import static io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions.font;
import static io.github.srcimon.screwbox.core.particles.ParticleOptionsBundle.FALLING_LEAVES;

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
                    var screen = engine.graphics().screen();
                    var drawOptions = font(BOLDZILLA).scale(6).alignCenter();
                    screen.fillWith(Color.hex("#125d7e"));
                    screen.drawText(screen.center(), "Hello World!", drawOptions);
                })

                .addSystem(PRESENTATION_UI_FOREGROUND, engine -> {
                    if (engine.keyboard().isPressed(Key.SPACE)) {
                        engine.graphics().enableSplitscreen();
                        engine.log().info("splitscreen enabled");
                    }
                    for (var view : engine.graphics().activeViewports()) {
                        view.drawCircle(Offset.at(20, 20), 14, CircleDrawOptions.filled(Color.DARK_BLUE));
                    }
                })
                // add light spot to create nice sunlight effect
                .addEntity("sun", new PointLightComponent(800, Color.BLACK),
                        new GlowComponent(800, Color.YELLOW.opacity(0.1)),
                        new TransformComponent())

                // add falling leaves
                .addEntity("falling leaves",
                        new TransformComponent(screwBox.graphics().world().visibleArea()),
                        new ParticleEmitterComponent(Duration.ofMillis(250), FALLING_LEAVES))

                // let the mouse interact with the falling leaves
                .addEntity("cursor",
                        new TransformComponent(),
                        new CursorAttachmentComponent(),
                        new ParticleInteractionComponent(80, Percent.max()));

        screwBox.start();
    }
}