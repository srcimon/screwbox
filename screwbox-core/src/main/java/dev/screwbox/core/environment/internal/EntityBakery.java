package dev.screwbox.core.environment.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.utils.Reflections;

import java.util.Optional;

public class EntityBakery {

    //TODO move into entity
    public static Optional<Entity> tryBake(final Entity entity, final Entity peer, Class<? extends Component> componentClass) {
        if (entity == peer) {
            return Optional.empty();
        }
        final var entityComponent = entity.get(componentClass);
        final var peerComponent = peer.get(componentClass);
        boolean areEqual = Reflections.areEqualComparingFieldValues(entityComponent, peerComponent);

        //TODO test for non null components

        if (!areEqual) {
            return Optional.empty();
        }
        final Optional<Bounds> result = entity.bounds().tryMerge(peer.bounds());
        if (result.isPresent()) {
            var bakeResult = new Entity().bounds(result.get()).add(entity.get(componentClass));
            entity.remove(componentClass);
            peer.remove(componentClass);
            return Optional.of(bakeResult);
        }
        return Optional.empty();
    }
}
