package io.github.srcimon.screwbox.physicsplayground.player;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.physicsplayground.tiles.FloorTypeComponent;

public class PlayerStatusSystem implements EntitySystem {
    @Override
    public void update(Engine engine) {
        Entity player = engine.environment().fetchSingleton(Archetype.ofSpacial(PlayerStatusComponent.class));

        engine.physics().raycastFrom(player.position())
                .checkingFor(Archetype.ofSpacial(FloorTypeComponent.class))
                .castingVertical(20)
                .nearestEntity()
                .map(floor -> floor.get(FloorTypeComponent.class).type)
                .ifPresent(floorType -> player.get(PlayerStatusComponent.class).floorType = floorType);


    }
}
