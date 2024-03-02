package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.SystemOrder;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroyComponent;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.examples.platformer.components.LetsGoComponent;
import io.github.srcimon.screwbox.examples.platformer.components.PlayerMarkerComponent;

import static io.github.srcimon.screwbox.core.Duration.ofSeconds;
import static io.github.srcimon.screwbox.core.graphics.Color.WHITE;
import static io.github.srcimon.screwbox.core.graphics.Pixelfont.defaultFont;

@Order(SystemOrder.PRESENTATION_EFFECTS)
public class LetsGoSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(PlayerMarkerComponent.class, TransformComponent.class);
    private static final Archetype BUBBLES = Archetype.of(LetsGoComponent.class, TransformComponent.class);
    private static final Asset<Sound> LETS_GO_SOUND = Sound.assetFromFile("sounds/letsgo.wav");

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().isPressed(Key.Q)) {
            var playerCenter = engine.environment().fetchSingleton(PLAYER).get(TransformComponent.class).bounds
                    .position();
            engine.audio().playSound(LETS_GO_SOUND);

            Entity letsGoBubble = new Entity().add(
                    new TransformComponent(
                            Bounds.atPosition(playerCenter.x(), playerCenter.y() - 5, 0, 0)),
                    new LetsGoComponent(),
                    new TweenDestroyComponent(),
                    new TweenComponent(ofSeconds(2)));

            engine.environment().addEntity(letsGoBubble);
        }

        var delta = engine.loop().delta();
        for (Entity bubble : engine.environment().fetchAll(BUBBLES)) {
            final var bubbleTranform = bubble.get(TransformComponent.class);
            var letsGoComponent = bubble.get(LetsGoComponent.class);
            bubbleTranform.bounds = bubbleTranform.bounds
                    .moveBy(Vector.of(Math.sin(letsGoComponent.modifier * 100 - 100) * delta * 100, -10 * delta));

            Vector postion = bubbleTranform.bounds.position();
            engine.graphics().world().drawTextCentered(postion, "LET'S GO", defaultFont(WHITE),
                    letsGoComponent.visibility, 0.5);
            letsGoComponent.modifier += delta / 16;
            letsGoComponent.visibility = Percent.of(letsGoComponent.visibility.value() - delta / 2);
        }
    }
}
