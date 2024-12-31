package io.github.srcimon.screwbox.physicsplayground.player;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.physicsplayground.tiles.Material;

public class PlayerInfoSystem implements EntitySystem {

    private static final Archetype PLAYERS = Archetype.of(PlayerInfoComponent.class, PhysicsComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var player : engine.environment().fetchAll(PLAYERS)) {
            player.get(PlayerInfoComponent.class).currentGroundMaterial = engine.physics().raycastFrom(player.position())
                    .checkingFor(Archetype.ofSpacial(MaterialComponent.class))
                    .castingVertical(10)
                    .nearestEntity().map(hit -> hit.get(MaterialComponent.class).material)
                    .orElse(Material.UNKNOWN);
        }
    }
}
