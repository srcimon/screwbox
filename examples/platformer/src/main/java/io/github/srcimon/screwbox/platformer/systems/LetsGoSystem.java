package io.github.srcimon.screwbox.platformer.systems;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Rotation;
import dev.screwbox.core.Vector;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.assets.FontBundle;
import dev.screwbox.core.audio.Sound;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.tweening.TweenComponent;
import dev.screwbox.core.environment.tweening.TweenDestroyComponent;
import dev.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.platformer.components.LetsGoComponent;
import io.github.srcimon.screwbox.platformer.components.PlayerMarkerComponent;

import static dev.screwbox.core.Duration.ofMillis;
import static dev.screwbox.core.Duration.ofSeconds;
import static dev.screwbox.core.Duration.oneSecond;
import static dev.screwbox.core.graphics.options.CameraShakeOptions.lastingForDuration;
import static dev.screwbox.core.graphics.options.TextDrawOptions.font;

@Order(Order.SystemOrder.PRESENTATION_EFFECTS)
public class LetsGoSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.ofSpacial(PlayerMarkerComponent.class);
    private static final Archetype BUBBLES = Archetype.ofSpacial(LetsGoComponent.class);
    private static final Asset<Sound> LETS_GO_SOUND = Sound.assetFromFile("sounds/letsgo.wav");

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().isPressed(Key.Q)) {
            engine.graphics().camera().shake(lastingForDuration(oneSecond()).strength(10).interval(ofMillis(100)).swing(Rotation.degrees(5)));

            var playerCenter = engine.environment().fetchSingleton(PLAYER).position();
            engine.audio().playSound(LETS_GO_SOUND);

            Entity letsGoBubble = new Entity().add(
                    new TransformComponent(
                            Bounds.atPosition(playerCenter.addY(-5), 0, 0)),
                    new LetsGoComponent(),
                    new TweenDestroyComponent(),
                    new TweenComponent(ofSeconds(2)));

            engine.environment().addEntity(letsGoBubble);
        }

        var delta = engine.loop().delta();
        for (Entity bubble : engine.environment().fetchAll(BUBBLES)) {
            var letsGoComponent = bubble.get(LetsGoComponent.class);
            bubble.moveBy(Vector.of(Math.sin(letsGoComponent.modifier * 100 - 100) * delta * 100, -10 * delta));

            engine.graphics().world().drawText(bubble.position(), "LET'S GO", font(FontBundle.BOLDZILLA).opacity(letsGoComponent.visibility).scale(0.5));
            letsGoComponent.modifier += delta / 16;
            letsGoComponent.visibility = Percent.of(letsGoComponent.visibility.value() - delta / 2);
        }
    }
}
