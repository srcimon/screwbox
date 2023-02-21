package io.github.simonbas.screwbox.examples.platformer.systems;

import io.github.simonbas.screwbox.core.Bounds;
import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.assets.Asset;
import io.github.simonbas.screwbox.core.entities.*;
import io.github.simonbas.screwbox.core.entities.components.FadeOutComponent;
import io.github.simonbas.screwbox.core.entities.components.RenderComponent;
import io.github.simonbas.screwbox.core.entities.components.TransformComponent;
import io.github.simonbas.screwbox.examples.platformer.components.SmokeEmitterComponent;
import io.github.simonbas.screwbox.tiled.Tileset;

import static io.github.simonbas.screwbox.core.assets.Asset.asset;
import static io.github.simonbas.screwbox.core.utils.ListUtil.randomFrom;

@Order(SystemOrder.PRESENTATION_EFFECTS)
public class SmokePuffSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(RenderComponent.class, TransformComponent.class,
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
            var order = player.get(RenderComponent.class).drawOrder;
            Bounds bounds = Bounds.atPosition(playerCenter, 16, 16);
            Entity smokePuff = new Entity().add(
                    new FadeOutComponent(2),
                    new TransformComponent(bounds),
                    new RenderComponent(randomFrom(SPRITES.get().all()), order)

            );
            engine.entities().add(smokePuff);
        }
    }
}
