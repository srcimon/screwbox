package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.ecosphere.*;
import io.github.srcimon.screwbox.core.ecosphere.components.FadeOutComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.RenderComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.TransformComponent;
import io.github.srcimon.screwbox.examples.platformer.components.SmokeEmitterComponent;
import io.github.srcimon.screwbox.tiled.Tileset;

import static io.github.srcimon.screwbox.core.assets.Asset.asset;
import static io.github.srcimon.screwbox.core.utils.ListUtil.randomFrom;

@Order(SystemOrder.PRESENTATION_EFFECTS)
public class SmokePuffSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(RenderComponent.class, TransformComponent.class,
            SmokeEmitterComponent.class);

    private static final Asset<Tileset> SPRITES = asset(() -> Tileset.fromJson("tilesets/effects/smokes.json"));

    @Override
    public void update(Engine engine) {
        var playerEntity = engine.ecosphere().fetch(PLAYER);
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
            engine.ecosphere().addEntity(smokePuff);
        }
    }
}
