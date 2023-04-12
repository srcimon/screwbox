package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.*;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.entities.*;
import io.github.srcimon.screwbox.core.entities.components.TimeoutComponent;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.examples.platformer.components.LetsGoComponent;
import io.github.srcimon.screwbox.examples.platformer.components.PlayerMarkerComponent;

import static io.github.srcimon.screwbox.core.graphics.Color.WHITE;
import static io.github.srcimon.screwbox.core.graphics.Pixelfont.defaultFont;

@Order(SystemOrder.PRESENTATION_EFFECTS)
public class LetsGoSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(PlayerMarkerComponent.class, TransformComponent.class);
    private static final Archetype BUBBLES = Archetype.of(LetsGoComponent.class, TransformComponent.class);
    private static final Asset<Sound> LETS_GO_SOUND = Sound.assetFromFile("sounds/letsgo.wav");

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().justPressed(Key.Q)) {
            var playerCenter = engine.entities().forcedFetch(PLAYER).get(TransformComponent.class).bounds
                    .position();
            engine.audio().playEffect(LETS_GO_SOUND);

            Entity letsGoBubble = new Entity().add(
                    new TransformComponent(
                            Bounds.atPosition(playerCenter.x(), playerCenter.y() - 5, 0, 0)),
                    new LetsGoComponent(),
                    new TimeoutComponent(Time.now().plusSeconds(2)));

            engine.entities().add(letsGoBubble);
        }

        var delta = engine.loop().delta();
        for (Entity bubble : engine.entities().fetchAll(BUBBLES)) {
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
