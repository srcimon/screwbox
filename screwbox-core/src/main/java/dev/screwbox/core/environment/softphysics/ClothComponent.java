package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.importing.IdPool;
import dev.screwbox.core.environment.softphysics.support.SoftPhysicsSupport;
import dev.screwbox.core.graphics.Size;

import java.io.Serial;

/**
 * Used to mark the root node of a soft body cloth. Will not be auto populated like {@link RopeComponent} or {@link SoftBodyComponent},
 * but can easily be created using {@link SoftPhysicsSupport#createCloth(Bounds, Size, IdPool)}.
 *
 * @since 3.20.0
 */
public class ClothComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Entity mesh used to create the cloth.
     */
    public Entity[][] mesh;

    /**
     * {@link Size} of a single mesh cell in idle state.
     */
    public Size meshCellSize;

    /**
     * Creates a new instance using pre filled mesh and cell size.
     */
    public ClothComponent(final Entity[][] mesh, final Size meshCellSize) {
        this.mesh = mesh;
        this.meshCellSize = meshCellSize;
    }
}
