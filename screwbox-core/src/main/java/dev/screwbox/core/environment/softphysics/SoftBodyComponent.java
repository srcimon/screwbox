package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to mark a chain of {@link Entity entities} as a soft body. Will create a fast accessible list of
 * {@link Entity entities} linked from this {@link Entity} with {@link SoftLinkComponent}.
 *
 * @since 3.16.0
 */
public class SoftBodyComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * List of {@link Entity entities} that define the outline of the soft body. Will be updated by {@link SoftBodySystem}.
     */
    public transient List<Entity> nodes = new ArrayList<>();

    @Serial
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        nodes = (List<Entity>) in.readObject();
    }

    @Serial
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(new ArrayList<>(nodes));
    }
}
