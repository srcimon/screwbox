package dev.screwbox.core.environment.softphysics.support;

import dev.screwbox.core.environment.Entity;

import java.util.List;

@Deprecated
class TaggedRopeEntities extends TaggedEntitiesCollection implements RopeEntities {

    @Override
    public Entity end() {
        return last();
    }

    @Override
    public Entity center() {
        return all().get(all().size() / 2 - 1);
    }

    @Override
    public List<Entity> connectors() {
        return all().subList(1, all().size() - 1);
    }
}
