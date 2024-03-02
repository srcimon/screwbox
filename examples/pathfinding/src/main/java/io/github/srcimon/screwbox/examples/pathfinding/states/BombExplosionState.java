package io.github.srcimon.screwbox.examples.pathfinding.states;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.graphics.CameraShake;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.examples.pathfinding.components.PlayerMovementComponent;
import io.github.srcimon.screwbox.tiled.Tileset;

import java.io.Serial;
import java.util.List;

public class BombExplosionState implements EntityState {

    private static final Asset<Sprite> SPRITE = Tileset.spriteAssetFromJson("bomb.json", "explosion");
    private static final Asset<Sound> EXPLOSION = Sound.assetFromFile("explosion.wav");

    @Serial
    private static final long serialVersionUID = 1L;

    private Time endOfAnimation;

    @Override
    public void enter(Entity entity, Engine engine) {
        engine.graphics().camera().addShake(CameraShake.strength(10).interval(Duration.ofMillis(10)).duration(Duration.ofMillis(500)));
        Sprite sprite = SPRITE.get().freshInstance();
        entity.get(RenderComponent.class).sprite = sprite;
        endOfAnimation = engine.loop().lastUpdate().plus(sprite.duration());
        engine.audio().playSound(EXPLOSION, entity.get(TransformComponent.class).bounds.position());
        Bounds bounds = entity.get(TransformComponent.class).bounds.inflated(8);
        List<Entity> entitiesInExplosionRange = engine.physics()
                .searchInRange(bounds)
                .ignoringEntitiesHaving(PlayerMovementComponent.class)
                .selectAll();

        engine.environment().remove(entitiesInExplosionRange);
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (Time.now().isAfter(endOfAnimation)) {
            engine.environment().remove(entity);
        }
        return this;
    }

}
