package io.github.srcimon.screwbox.core.environment.core;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.core.graphics.Viewport;
import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;

/**
 * Renders a crt monitor like effect on top of the primary {@link Viewport} or on all
 * split screen {@link Viewport viewports}.
 */
@Order(Order.SystemOrder.PRESENTATION_UI_FOREGROUND)
public class CrtMonitorOverlaySystem implements EntitySystem {

    private static final Asset<Sprite> EDGE = SpriteBundle.CRT_MONITOR_EDGE.asset();

    private final boolean decorateSplitScreens;

    public CrtMonitorOverlaySystem() {
        this(false);
    }

    public CrtMonitorOverlaySystem(final boolean decorateSplitScreens) {
        this.decorateSplitScreens = decorateSplitScreens;
    }

    @Override
    public void update(Engine engine) {
        final long seed = engine.loop().lastUpdate().milliseconds();

        if (decorateSplitScreens) {
            for (var viewport : engine.graphics().viewports()) {
                Canvas canvas = viewport.canvas();
                decorateCanvas(canvas, seed);
            }
        } else {
            decorateCanvas(engine.graphics().canvas(), seed);
        }
    }

    private void decorateCanvas(final Canvas canvas, final long seed) {
        for (int y = 0; y < canvas.height(); y += 3) {
            var opacity = Math.sin((seed + y * 20) / 600.0) / 10.0 + 0.25;
            canvas.drawLine(Offset.at(0, y), Offset.at(canvas.width(), y), LineDrawOptions.color(Color.BLACK.opacity(opacity)).strokeWidth(2));
        }
        var size = EDGE.get().size();
        canvas.drawSprite(EDGE, Offset.origin(), SpriteDrawOptions.scaled(1));
        canvas.drawSprite(EDGE, Offset.at(canvas.width() - size.width(), 0), SpriteDrawOptions.scaled(1).flipHorizontal(true));
        canvas.drawSprite(EDGE, Offset.at(0, canvas.height() - size.height()), SpriteDrawOptions.scaled(1).flipVertical(true));
        canvas.drawSprite(EDGE, Offset.at(canvas.width() - size.width(), canvas.height() - size.height()), SpriteDrawOptions.scaled(1).flipHorizontal(true).flipVertical(true));
    }
}
