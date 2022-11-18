package de.suzufa.screwbox.playground.debo.systems;

import static de.suzufa.screwbox.core.graphics.Color.WHITE;
import static de.suzufa.screwbox.core.graphics.Pixelfont.defaultFont;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.audio.Sound;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.UpdatePriority;
import de.suzufa.screwbox.core.entities.components.TimeoutComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.keyboard.Key;
import de.suzufa.screwbox.playground.debo.components.LetsGoComponent;
import de.suzufa.screwbox.playground.debo.components.PlayerMarkerComponent;

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
            Bounds updatedBounds = bubbleTranform.bounds
                    .moveBy(Vector.of(Math.sin(letsGoComponent.modifier * 100 - 100) * delta * 100, -10 * delta));
            bubbleTranform.bounds = updatedBounds;

            Vector postion = bubbleTranform.bounds.position();
            engine.graphics().world().drawTextCentered(postion, "LET'S GO", defaultFont(WHITE),
                    letsGoComponent.visibility, 0.5);
            letsGoComponent.modifier += delta / 16;
            letsGoComponent.visibility = Percent.of(letsGoComponent.visibility.value() - delta / 2);
        }
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_EFFECTS;
    }
}
