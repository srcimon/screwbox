package de.suzufa.screwbox.playground.debo.systems;

import static de.suzufa.screwbox.core.assets.Asset.asset;
import static de.suzufa.screwbox.core.utils.ListUtil.randomFrom;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.UpdatePriority;
import de.suzufa.screwbox.core.entities.components.FadeOutComponent;
import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.playground.debo.components.SmokeEmitterComponent;
import de.suzufa.screwbox.tiled.Tileset;

public class SmokePuffSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(SpriteComponent.class, TransformComponent.class,
            SmokeEmitterComponent.class);

    private static final Asset<Tileset> SPRITES = asset(() -> Tileset.fromJson("tilesets/effects/smokes.json"));

    @Override
    public void update(Engine engine) {
        var playerEntity = engine.entities().fetch(PLAYER);
        if (playerEntity.isEmpty()) {
            return;
        }
        var player = playerEntity.get();
        var smokeEmitter = player.get(SmokeEmitterComponent.class);
        if (smokeEmitter.ticker.isTick(engine.loop().lastUpdate())) {
            var playerCenter = player.get(TransformComponent.class).bounds.position();
            var order = player.get(SpriteComponent.class).drawOrder;
            Bounds bounds = Bounds.atPosition(playerCenter, 16, 16);
            Entity smokePuff = new Entity().add(
                    new FadeOutComponent(2),
                    new TransformComponent(bounds),
                    new SpriteComponent(randomFrom(SPRITES.get().all()), order)

            );
            engine.entities().add(smokePuff);
        }
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_EFFECTS;
    }
}
