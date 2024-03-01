package io.github.srcimon.screwbox.core.environment.camera;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.utils.Lurk;

import java.io.Serial;

import static io.github.srcimon.screwbox.core.Duration.ofMillis;

public final class CameraComponent implements Component {


    //TODO remove camera from Tiled Maps
    @Serial
    private static final long serialVersionUID = 1L;

    public Bounds visibleArea;

    //TODO shake may be relative to screen and be set in graphics().setcamerashake(interval, strength)

    public CameraComponent(final Bounds visibleArea) {
        this.visibleArea = visibleArea;
    }

}
