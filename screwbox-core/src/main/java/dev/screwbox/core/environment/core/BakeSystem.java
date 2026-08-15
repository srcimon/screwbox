package dev.screwbox.core.environment.core;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;

import static dev.screwbox.core.environment.Order.OPTIMIZATION;

@ExecutionOrder(OPTIMIZATION)
public class BakeSystem implements EntitySystem {

    private static final Archetype BAKE_CANDIDATES = Archetype.ofSpacial(StaticBoundsComponent.class);


    @Override
    public void update(Engine engine) {

    }

//    @Override
//    public void update(final Engine engine) {
//        final List<Entity> combinables = engine.environment().fetchAll(COMBINABLES);
//        for (final var entity : combinables) {
//            for (final var peer : combinables) {
//                final var baked = EntityBakery.tryBake(entity, peer, ColliderComponent.class);
//                if (baked.isPresent()) {
//                    entity.remove(StaticColliderComponent.class);
//                    peer.remove(StaticColliderComponent.class);
//                    baked.get().add(new StaticColliderComponent());
//                    engine.environment().addEntity(baked.get());
//                    return; // only one combination per frame
//                }
//            }
//        }
//        // at this point all colliders have been combined
//        for (final var entity : combinables) {
//            entity.remove(StaticColliderComponent.class);
//        }
//        engine.environment().remove(OptimizePhysicsPerformanceSystem.class);
//    }
}
