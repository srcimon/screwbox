package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.environment.*;
import io.github.srcimon.screwbox.core.environment.components.RenderComponent;
import io.github.srcimon.screwbox.core.environment.components.TransformComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroyComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenOpacityComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenStateComponent;
import io.github.srcimon.screwbox.examples.platformer.components.SmokeEmitterComponent;
import io.github.srcimon.screwbox.tiled.Tileset;

import static io.github.srcimon.screwbox.core.Duration.ofMillis;
import static io.github.srcimon.screwbox.core.assets.Asset.asset;
import static io.github.srcimon.screwbox.core.utils.ListUtil.randomFrom;

@Order(SystemOrder.PRESENTATION_EFFECTS)
public class SmokePuffSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(RenderComponent.class, TransformComponent.class,
            SmokeEmitterComponent.class);

    private static final Asset<Tileset> SPRITES = asset(() -> Tileset.fromJson("tilesets/effects/smokes.json"));

    @Override
    public void update(Engine engine) {
        var playerEntity = engine.environment().fetch(PLAYER);
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
                    new TweenOpacityComponent(),
                    new TweenDestroyComponent(),
                    new TweenStateComponent(ofMillis(300), false, true),
                    new TransformComponent(bounds),
                    new RenderComponent(randomFrom(SPRITES.get().all()), order)

            );
            engine.environment().addEntity(smokePuff);
        }
    }
}
