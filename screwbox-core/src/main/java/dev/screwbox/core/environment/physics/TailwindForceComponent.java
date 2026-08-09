package dev.screwbox.core.environment.physics;

import dev.screwbox.core.environment.Component;

import java.io.Serial;

/**
 * Will receive acceleration by nearby entities containing {@link TailwindComponent}.
 *
 * @since 3.15.0
 */
public class TailwindForceComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;
}
