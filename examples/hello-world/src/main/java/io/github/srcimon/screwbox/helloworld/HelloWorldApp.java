package io.github.srcimon.screwbox.helloworld;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.assets.FontBundle;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.light.GlowComponent;
import io.github.srcimon.screwbox.core.environment.light.PointLightComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleInteractionComponent;
import io.github.srcimon.screwbox.core.environment.physics.CursorAttachmentComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Pixelfont;
import io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions;
import io.github.srcimon.screwbox.core.mouse.MouseButton;

import static io.github.srcimon.screwbox.core.assets.FontBundle.BOLDZILLA;
import static io.github.srcimon.screwbox.core.environment.Order.SystemOrder.PRESENTATION_BACKGROUND;
import static io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions.font;
import static io.github.srcimon.screwbox.core.particles.ParticleOptionsBundle.FALLING_LEAVES;

public class HelloWorldApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Hello World");

        screwBox.archivements().add(new BestClickerArchivement());

        screwBox.environment().addSystem(engine -> {
            for(var archivement : engine.archivements().allArchivements()) {
                engine.graphics().canvas().drawText(engine.graphics().canvas().center(),  archivement.title() + " : " + archivement.score() + " of " + archivement.goal(), TextDrawOptions.font(BOLDZILLA).alignCenter());
            }
            if(engine.mouse().isPressed(MouseButton.LEFT)) {
                engine.archivements().progess(BestClickerArchivement.class);
            }
        });

        screwBox.start();
    }
}