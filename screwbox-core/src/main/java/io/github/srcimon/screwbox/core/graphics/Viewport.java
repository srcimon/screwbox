package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Vector;

public interface Viewport {

    Offset toCanvas(Vector position);
}
