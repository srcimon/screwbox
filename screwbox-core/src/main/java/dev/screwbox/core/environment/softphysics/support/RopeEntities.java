package dev.screwbox.core.environment.softphysics.support;

import dev.screwbox.core.environment.Entity;

import java.util.ArrayList;
import java.util.List;

public class RopeEntities extends ArrayList<Entity> {

    public Entity root() {
        return getFirst();
    }

    public Entity center() {
        return get(size() / 2 - 1);
    }

    public List<Entity> connectors() {
        return subList(1, size() - 1);
    }

    public Entity end() {
        return getLast();
    }
}
