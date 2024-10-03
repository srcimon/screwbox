package io.github.srcimon.screwbox.helloworld;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Line;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.light.GlowComponent;
import io.github.srcimon.screwbox.core.environment.light.PointLightComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleInteractionComponent;
import io.github.srcimon.screwbox.core.environment.physics.CursorAttachmentComponent;
import io.github.srcimon.screwbox.core.environment.rendering.FixedRotationComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;

import static io.github.srcimon.screwbox.core.assets.FontBundle.BOLDZILLA;
import static io.github.srcimon.screwbox.core.environment.Order.SystemOrder.PRESENTATION_BACKGROUND;
import static io.github.srcimon.screwbox.core.graphics.TextDrawOptions.font;
import static io.github.srcimon.screwbox.core.particles.ParticleOptionsBundle.FALLING_LEAVES;

public class HelloWorldApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Hello World");
        // set ambient light to nearly full brightness
        screwBox.graphics().light().setAmbientLight(Percent.max());
        screwBox.graphics().camera().setZoom(4);
        // set good shadow quality
        screwBox.graphics().configuration().setLightmapScale(2);

        screwBox.environment()
                // enable all features that are used below...
                .enableAllFeatures()

                // draw Hello World

                // add light spot to create nice sunlight effect

                .addSystem(engine -> {
                    engine.environment().fetchAllHaving(TweenComponent.class).forEach(e -> {
                        final var render = e.get(RenderComponent.class);
                        Rotation degrees = Rotation.degrees(Time.now().milliseconds() / 8.0);
                        render.options = render.options.rotateX(degrees);
                        engine.graphics().world().drawLine(degrees.applyOn(Line.normal(e.position().addY(40), 20)), LineDrawOptions.color(Color.WHITE).strokeWidth(2));
                    });
                })
                .addEntity(
                        new RenderComponent(SpriteBundle.MAN_STAND, SpriteDrawOptions.originalSize().rotateX(Rotation.degrees(40))),
                        new TweenComponent(Duration.oneSecond(), Ease.LINEAR_IN, true),
                        new TransformComponent(0, 0, 16, 16))

                // let the mouse interact with the falling leaves
                .addEntity("cursor",
                        new TransformComponent(),
                        new CursorAttachmentComponent(),
                        new ParticleInteractionComponent(80, Percent.max()));

        screwBox.start();
    }
}