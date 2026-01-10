package dev.screwbox.core.environment.softphysics.support;

import dev.screwbox.core.environment.Entity;

import java.util.Collections;
import java.util.List;

public record ClothEntities(List<Entity> entities) {

    public Entity root() {
        return entities.getFirst();
    }

    public List<Entity> all() {
        return Collections.unmodifiableList(entities);
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
        return entities.stream().filter(entity -> entity.hasTag(tag)).toList();
    }
}
