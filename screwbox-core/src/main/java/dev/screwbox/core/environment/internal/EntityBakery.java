package dev.screwbox.core.environment.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.utils.GeometryUtil;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

public class EntityBakery {

    //TODO move into entity
    public static Optional<Entity> tryBake(final Entity entity, final Entity peer, Class<? extends Component> componentClass) {
        if (entity == peer) {
            return Optional.empty();
        }
        final var entityComponent = entity.get(componentClass);
        final var peerComponent = peer.get(componentClass);
        boolean areEqual = publicPropertiesAreEqual(entityComponent, peerComponent);

        //TODO test for non null components

        if (!areEqual) {
            return Optional.empty();
        }
        Optional<Bounds> result = GeometryUtil.tryToCombine(entity.bounds(), peer.bounds());//TODO move inside Bounds
        if (result.isPresent()) {
            var bakeResult = new Entity().bounds(result.get()).add(entity.get(componentClass));
            entity.remove(componentClass);
            peer.remove(componentClass);
            return Optional.of(bakeResult);
        }
        return Optional.empty();
    }

    //TODO move inside Reflections
    public static boolean publicPropertiesAreEqual(Object obj1, Object obj2) {
        if (obj1.getClass() != obj2.getClass()) {
            return false;
        }
        // Extract only public fields
        Field[] publicFields = obj1.getClass().getFields();

        try {
            for (Field field : publicFields) {
                Object value1 = field.get(obj1);
                Object value2 = field.get(obj2);

                // Check field-level equality safely
                if (!Objects.equals(value1, value2)) {
                    return false;
                }
            }
        } catch (IllegalAccessException e) {
            return false; // Fallback if reflection permissions fail
        }

        return true;
    }
}
