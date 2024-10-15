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
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;

import static io.github.srcimon.screwbox.core.assets.FontBundle.BOLDZILLA;
import static io.github.srcimon.screwbox.core.environment.Order.SystemOrder.PRESENTATION_BACKGROUND;
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

                        .addSystem(engine -> {
                            engine.graphics().screen().fillWith(Color.YELLOW);
                            SpriteBatch spriteBatch = new SpriteBatch();
                            spriteBatch.add(SpriteBundle.EXPLOSION.get(), Offset.at(90,90), SpriteDrawOptions.originalSize(),1);
                            engine.graphics().screen().drawSpriteBatch(spriteBatch);
                            engine.graphics().screen().drawSprite(SpriteBundle.MAN_STAND.get(), Offset.at(90,95), SpriteDrawOptions.originalSize());
                            engine.graphics().screen().drawSprite(SpriteBundle.BOX_STRIPED, engine.graphics().screen().center(), SpriteDrawOptions.originalSize());
                        });

        screwBox.start();
    }
}