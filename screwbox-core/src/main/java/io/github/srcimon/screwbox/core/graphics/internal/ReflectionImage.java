package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;
import io.github.srcimon.screwbox.core.graphics.Viewport;
import io.github.srcimon.screwbox.core.graphics.internal.renderer.DefaultRenderer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.UnaryOperator;

import static java.util.Objects.isNull;

public final class ReflectionImage {

    private final Viewport viewport;
    private final Size imageSize;
    private final SpriteBatch spriteBatch = new SpriteBatch();
    private final ScreenBounds screenArea;
    private final UnaryOperator<Bounds> entityMotion;
    private final int drawOrder;

    public ReflectionImage(final Viewport viewport, final int drawOrder, final Size imageSize, final ScreenBounds screenArea, final UnaryOperator<Bounds> entityMotion) {
        this.viewport = viewport;
        this.imageSize = imageSize;
        this.screenArea = screenArea;
        this.drawOrder = drawOrder;
        this.entityMotion = entityMotion;
    }

    public void addEntity(final Entity entity) {
        final var render = entity.get(RenderComponent.class);
        if (render.drawOrder > drawOrder) {
            return;
        }
        final Bounds entityBounds = isNull(entityMotion) ? entity.bounds() : entityMotion.apply(entity.bounds());
        final Bounds entityRenderArea = Bounds.atPosition(entityBounds.position(),
                entityBounds.width() * render.options.scale(),
                entityBounds.height() * render.options.scale());

        final ScreenBounds screenBounds = viewport.toCanvas(entityRenderArea, render.parallaxX, render.parallaxY);

        if (screenBounds.intersects(screenArea)) {
            var localDistance = screenBounds.center().substract(screenArea.offset());
            var localOffset = Offset.at(
                    localDistance.x() / viewport.camera().zoom() - render.sprite.width() * render.options.scale() / 2,
                    imageSize.height() - localDistance.y() / viewport.camera().zoom() - render.sprite.height() * render.options.scale() / 2
            );
            spriteBatch.add(render.sprite, localOffset, render.options.invertVerticalFlip(), render.drawOrder);
        }
    }

    public BufferedImage create() {
        final BufferedImage image = new BufferedImage(imageSize.width(), imageSize.height(), BufferedImage.TYPE_INT_ARGB);
        final var graphics2d = (Graphics2D) image.getGraphics();
        final var renderer = new DefaultRenderer();
        renderer.updateContext(() -> graphics2d);
        renderer.drawSpriteBatch(spriteBatch, new ScreenBounds(Offset.origin(), imageSize));
        graphics2d.dispose();
        return image;
    }
}