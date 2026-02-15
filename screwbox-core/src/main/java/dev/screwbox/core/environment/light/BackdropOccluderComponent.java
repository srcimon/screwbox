package dev.screwbox.core.environment.light;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.options.OccluderOptions;

import java.io.Serial;

//TODO document, test
public class BackdropOccluderComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public OccluderOptions options;

    public BackdropOccluderComponent(final OccluderOptions options) {
        this.options = options;
    }
}
