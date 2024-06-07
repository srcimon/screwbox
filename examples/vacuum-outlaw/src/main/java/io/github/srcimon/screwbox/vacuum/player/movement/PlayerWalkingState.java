package io.github.srcimon.screwbox.vacuum.player.movement;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.vacuum.deathpit.DeathpitVictimComponent;
import io.github.srcimon.screwbox.vacuum.deathpit.FallenIntoDeathpitComponent;
import io.github.srcimon.screwbox.vacuum.enemies.EnemyComponent;
import io.github.srcimon.screwbox.vacuum.player.attack.PlayerAttackControlComponent;
import io.github.srcimon.screwbox.vacuum.player.death.FallToDeathState;
import io.github.srcimon.screwbox.vacuum.player.death.TouchedEnemyDeathState;

import static io.github.srcimon.screwbox.tiled.Tileset.spriteAssetFromJson;

public class PlayerWalkingState implements EntityState {

    private static final Archetype ENEMIES = Archetype.of(TransformComponent.class, EnemyComponent.class);
    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/objects/player.json", "idle");

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(RenderComponent.class).sprite = SPRITE.get();
        entity.addIfNotPresent(new DeathpitVictimComponent());
        entity.addIfNotPresent(new MovementControlComponent());
        entity.addIfNotPresent(new PlayerAttackControlComponent());
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (entity.hasComponent(FallenIntoDeathpitComponent.class)) {
            return new FallToDeathState();
        }
        for (final var enemy : engine.environment().fetchAll(ENEMIES)) {
            if (entity.bounds().touches(enemy.bounds())) {
                return new TouchedEnemyDeathState();
            }
        }
        return entity.hasComponent(DashComponent.class)
                ? new PlayerDashingState()
                : this;
    }

    @Override
    public void exit(Entity entity, Engine engine) {
        entity.remove(DeathpitVictimComponent.class);
    }
}
