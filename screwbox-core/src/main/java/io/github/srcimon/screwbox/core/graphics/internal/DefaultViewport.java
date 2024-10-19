package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.Camera;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Viewport;

public class DefaultViewport implements Viewport {

    private final Canvas canvas;
    private  Camera camera;

    public DefaultViewport(final Canvas canvas) {
        this.canvas = canvas;
        this.camera = null;//FIXME
    }

    public void setCameraWorkaround(Camera camera) {
this.camera = camera;
    }

    @Override
    public Offset toCanvas(final Vector position) {
        final double x = (position.x() - camera.position().x()) * camera.zoom() + (canvas.width() / 2.0) + canvas.offset().x();
        final double y = (position.y() - camera.position().y()) * camera.zoom() + (canvas.height() / 2.0) + canvas.offset().y();
        return Offset.at(x, y);
    }

    @Override
    public Bounds visibleArea() {
        return Bounds.atPosition(camera.position(),
                canvas.width() / camera.zoom(),
                canvas.height() / camera.zoom());
    }
}
