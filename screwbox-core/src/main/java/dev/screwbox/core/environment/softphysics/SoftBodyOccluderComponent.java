package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.io.Serial;

//TODO test
//TODO document within soft body guide
/**
 * Adds a backdrop shadow to a soft body. Also requires {@link Entity} to have a {@link SoftBodyComponent}.
 *
 * @since 3.23.0
 */
public class SoftBodyOccluderComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;
}
