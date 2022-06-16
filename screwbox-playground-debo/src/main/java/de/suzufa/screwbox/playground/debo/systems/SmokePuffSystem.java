package de.suzufa.screwbox.playground.debo.systems;

import java.util.Random;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.UpdatePriority;
import de.suzufa.screwbox.core.entityengine.components.FadeOutComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.playground.debo.components.SmokeEmitterComponent;
import de.suzufa.screwbox.tiled.SpriteDictionary;
import de.suzufa.screwbox.tiled.TiledSupport;

public class SmokePuffSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(SpriteComponent.class, TransformComponent.class,
            SmokeEmitterComponent.class);

    private static final SpriteDictionary SPRITES = TiledSupport.loadTileset("tilesets/effects/smokes.json");
    private static final Random RANDOM = new Random();

    @Override
    public void update(Engine engine) {
        var playerEntity = engine.entityEngine().fetch(PLAYER);
        if (playerEntity.isEmpty()) {
            return;
        }
        var player = playerEntity.get();
        var order = player.get(SpriteComponent.class).drawOrder;
        var smokeEmitter = player.get(SmokeEmitterComponent.class);
        var playerCenter = player.get(TransformComponent.class).bounds.position();
        if (Duration.since(smokeEmitter.lastEmitted).isAtLeast(Duration.ofMillis(120))) {
            int tileId = RANDOM.nextInt(3);
            Bounds bounds = Bounds.atPosition(playerCenter, 16, 16);
            Entity smokePuff = new Entity().add(
                    new FadeOutComponent(2),
                    new TransformComponent(bounds),
                    new SpriteComponent(SPRITES.findById(tileId), order)

            );
            smokeEmitter.lastEmitted = Time.now();
            engine.entityEngine().add(smokePuff);
        }

    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_EFFECTS;
    }
}
