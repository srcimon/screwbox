package dev.screwbox.core.environment.softphysics.support;

import dev.screwbox.core.environment.Entity;

import java.util.List;

class TaggedRopeEntities extends EntityStructure implements RopeEntities {

    @Override
    public Entity end() {
        return last();
    }

    @Override
    public List<Entity> connectors() {
        return all().subList(1, all().size() - 1);
    }
}
