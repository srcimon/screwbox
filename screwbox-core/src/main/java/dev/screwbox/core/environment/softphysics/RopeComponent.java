package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to mark the start of a rope. Will create a fast accessible list of {@link Entity entities} linked from this
 * {@link Entity} with {@link SoftLinkComponent}.
 *
 * @since 3.16.0
 */
public class RopeComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * List of {@link Entity entities} that are part of the rope. Will be updated by {@link RopeSystem}.
     */
    public transient List<Entity> nodes = new ArrayList<>();
//TODO Add serializationtest
    @Serial
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        nodes = new ArrayList<>();
    }
}
