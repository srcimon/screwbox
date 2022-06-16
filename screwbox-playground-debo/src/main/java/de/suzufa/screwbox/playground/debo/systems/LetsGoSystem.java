package de.suzufa.screwbox.playground.debo.systems;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.audio.SoundPool;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.UpdatePriority;
import de.suzufa.screwbox.core.entityengine.components.TimeoutComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.Font.Style;
import de.suzufa.screwbox.core.keyboard.Key;
import de.suzufa.screwbox.playground.debo.components.LetsGoComponent;
import de.suzufa.screwbox.playground.debo.components.PlayerMarkerComponent;

public class LetsGoSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(PlayerMarkerComponent.class, TransformComponent.class);
    private static final Archetype BUBBLES = Archetype.of(LetsGoComponent.class, TransformComponent.class);
    private static final Font FONT = new Font("FUTURA", 12, Style.BOLD);
    private static final SoundPool LETS_GO_SOUND = SoundPool.fromFile("sounds/letsgo.wav");

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().justPressed(Key.Q)) {
            var playerCenter = engine.entityEngine().forcedFetch(PLAYER).get(TransformComponent.class).bounds
                    .position();
            engine.audio().playEffect(LETS_GO_SOUND);

            Entity letsGoBubble = new Entity().add(
                    new TransformComponent(
                            Bounds.atPosition(playerCenter.x(), playerCenter.y() - 5, 0, 0)),
                    new LetsGoComponent(),
                    new TimeoutComponent(Time.now().plusSeconds(2)));

            engine.entityEngine().add(letsGoBubble);
        }

        var factor = engine.loop().metrics().updateFactor();
        for (Entity bubble : engine.entityEngine().fetchAll(BUBBLES)) {
            final var bubbleTranform = bubble.get(TransformComponent.class);
            var letsGoComponent = bubble.get(LetsGoComponent.class);
            Bounds updatedBounds = bubbleTranform.bounds
                    .moveBy(Vector.of(Math.sin(letsGoComponent.modifier * 100 - 100) * factor * 100, -10 * factor));
            bubbleTranform.bounds = updatedBounds;

            Vector postion = bubbleTranform.bounds.position();
            Color whity = Color.WHITE.withOpacity(letsGoComponent.visibility);
            engine.graphics().world().drawTextCentered(postion, "LET'S GO", FONT, whity);
            letsGoComponent.modifier += factor / 16;
            letsGoComponent.visibility = Percentage.of(letsGoComponent.visibility.value() - factor / 2);
        }
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_EFFECTS;
    }
}
