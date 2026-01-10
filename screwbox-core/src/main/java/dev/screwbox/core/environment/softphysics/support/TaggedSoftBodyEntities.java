package dev.screwbox.core.environment.softphysics.support;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.softphysics.SoftStructureComponent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toSet;

public class TaggedSoftBodyEntities extends TaggedEntitiesCollection implements SoftBodyEntities {

    @Override
    public List<Entity> supportOrigins() {
        return all().stream()
            .filter(entity -> entity.hasComponent(SoftStructureComponent.class))
            .toList();
    }

    @Override
    public List<Entity> supportTargets() {
        final var targetIds = all().stream()
            .map(entity -> entity.get(SoftStructureComponent.class))
            .filter(Objects::nonNull)
            .flatMapToInt(structure -> Arrays.stream(structure.targetIds))
            .boxed()
            .collect(toSet());

        return all().stream()
            .filter(entity -> targetIds.contains(entity.forceId()))
            .toList();
    }
}