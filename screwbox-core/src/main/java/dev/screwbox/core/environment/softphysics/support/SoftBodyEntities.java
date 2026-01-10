package dev.screwbox.core.environment.softphysics.support;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.softphysics.SoftStructureComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toSet;

public class SoftBodyEntities extends ArrayList<Entity> {

    public List<Entity> supportOrigins() {
        return stream()
            .filter(entity -> entity.hasComponent(SoftStructureComponent.class))
            .toList();
    }

    public List<Entity> supportTargets() {
        final var targetIds = stream()
            .map(entity -> entity.get(SoftStructureComponent.class))
            .filter(Objects::nonNull)
            .flatMapToInt(structure -> Arrays.stream(structure.targetIds))
            .boxed()
            .collect(toSet());

        return stream()
            .filter(entity -> targetIds.contains(entity.forceId()))
            .toList();
    }
}
