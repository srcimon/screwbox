package dev.screwbox.core.environment.softphysics.support;

import dev.screwbox.core.environment.Entity;

import java.util.ArrayList;
import java.util.List;

public class ClothEntities extends ArrayList<Entity> {

    public Entity root() {
        return getFirst();
    }

    List<Entity> outline() {
        return taggedBy("outline");
    }

    public List<Entity> outlineTop() {
        return taggedBy("outline-top");
    }

    public List<Entity> outlineBottom() {
        return taggedBy("outline-bottom");
    }

    private List<Entity> taggedBy(final String tag) {
        return stream().filter(entity -> entity.hasTag(tag)).toList();
    }
}
