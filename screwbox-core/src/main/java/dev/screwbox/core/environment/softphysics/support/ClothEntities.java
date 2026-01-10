package dev.screwbox.core.environment.softphysics.support;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.softphysics.ClothComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Easy access to {@link Entity entities} of a soft body cloth created by {@link SoftPhysicsSupport}.
 *
 * @since 3.20.0
 */
public class ClothEntities extends ArrayList<Entity> {

    /**
     * The root {@link Entity} containing the {@link ClothComponent}.
     */
    public Entity root() {
        return getFirst();
    }

    /**
     * All {@link Entity entities} that belong to the cloth outline.
     */
    List<Entity> outline() {
        return taggedBy("outline");
    }

    /**
     * All {@link Entity entities} that belong to the cloth top outline.
     */
    public List<Entity> outlineTop() {
        return taggedBy("outline-top");
    }

    /**
     * All {@link Entity entities} that belong to the cloth bottom outline.
     */
    public List<Entity> outlineBottom() {
        return taggedBy("outline-bottom");
    }

    private List<Entity> taggedBy(final String tag) {
        return stream().filter(entity -> entity.hasTag(tag)).toList();
    }

    //TODO add all methods
}
