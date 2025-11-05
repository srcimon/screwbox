package dev.screwbox.core.environment.core;

import dev.screwbox.core.Engine;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.Viewport;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.utils.MathUtil;

/**
 * Renders a crt monitor like effect on top of the primary {@link Viewport} or on all
 * split screen {@link Viewport viewports}.
 */
@ExecutionOrder(Order.PRESENTATION_UI_FOREGROUND)
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
        final long seed = engine.loop().time().milliseconds();

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
            var opacity = MathUtil.fastSin((seed + y * 20) / 600.0) / 10.0 + 0.25;
            canvas.drawLine(Offset.at(0, y), Offset.at(canvas.width(), y), LineDrawOptions.color(Color.BLACK.opacity(opacity)).strokeWidth(2));
        }
        var size = EDGE.get().size();
        SpriteDrawOptions baseOptions = SpriteDrawOptions.originalSize().ignoreOverlayShader();
        canvas.drawSprite(EDGE, Offset.origin(), baseOptions);
        canvas.drawSprite(EDGE, Offset.at(canvas.width() - size.width(), 0), baseOptions.flipHorizontal(true));
        canvas.drawSprite(EDGE, Offset.at(0, canvas.height() - size.height()), baseOptions.flipVertical(true));
        canvas.drawSprite(EDGE, Offset.at(canvas.width() - size.width(), canvas.height() - size.height()), baseOptions.flipHorizontal(true).flipVertical(true));
    }
}
