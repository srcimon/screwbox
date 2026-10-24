package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Time;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@ExecutionOrder(Order.SIMULATION_PREPARE)
public class ChunkSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        Time t = Time.now();
        var chunks = engine.environment().fetchAll(Archetype.ofSpacial(ChunkComponent.class));
        var stashes = engine.environment().fetchAll(Archetype.ofSpacial(StashComponent.class));
        int killDist = 1024;
        int reviveDist = 512;
        int chunkSize = 256;
        Map<Vector, StashComponent> stashMap = new HashMap<>();
        stashes.forEach(stash -> {
            var snapped = stash.position().snap(chunkSize);
            stashMap.put(snapped, stash.get(StashComponent.class));
        });

        int stashCount = 0;
        for (var chunk : chunks) {
            boolean isNear = engine.graphics().isWithinDistanceToVisibleArea(chunk.position(), killDist);
            if (!isNear) {
                var snapped = chunk.position().snap(chunkSize);
                StashComponent matchingStash = stashMap.computeIfAbsent(snapped, new Function<Vector, StashComponent>() {
                    @Override
                    public StashComponent apply(Vector vector) {
                        Entity ee = new Entity().bounds(Bounds.atPosition(vector, 0, 0));
                        var component = new StashComponent();
                        ee.add(component);
                        engine.environment().addEntity(ee);
                        return component;
                    }
                });

                matchingStash.stashedEntities.add(chunk);
                engine.environment().remove(chunk);
                stashCount++;
            }
        }
        int rewivecount = 0;
        for(final var stash : stashes) {
            boolean isNear = engine.graphics().isWithinDistanceToVisibleArea(stash.position(), reviveDist);
            if(isNear) {
                List<Entity> stashedEntities = stash.get(StashComponent.class).stashedEntities;
                engine.environment().addEntities(stashedEntities);
                engine.environment().remove(stash);
                rewivecount+=stashedEntities.size();
            }
        }
//        System.out.println("!!" + Duration.since(t).milliseconds());
    }
}
