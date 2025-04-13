package dev.screwbox.vacuum.player.movement;

import dev.screwbox.core.Engine;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.logic.EntityState;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.vacuum.deathpit.DeathpitVictimComponent;
import dev.screwbox.vacuum.deathpit.FallenIntoDeathpitComponent;
import dev.screwbox.vacuum.enemies.EnemyComponent;
import dev.screwbox.vacuum.player.attack.PlayerAttackControlComponent;
import dev.screwbox.vacuum.player.death.FallToDeathState;
import dev.screwbox.vacuum.player.death.TouchedEnemyDeathState;

import static dev.screwbox.tiles.Tileset.spriteAssetFromJson;

public class PlayerWalkingState implements EntityState {

    private static final Archetype ENEMIES = Archetype.ofSpacial(EnemyComponent.class);
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
