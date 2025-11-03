package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.ShaderSetup;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.Viewport;
import dev.screwbox.core.graphics.internal.renderer.DefaultRenderer;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.UnaryOperator;

import static java.util.Objects.isNull;

public final class ReflectionImage {

    private static final Comparator<Rendering> DRAW_ORDER_COMPARATOR = Comparator.comparing(e -> e.drawOrder);

    private record Rendering(Sprite sprite, Offset localOffset, SpriteDrawOptions options, int drawOrder) {
    }

    private final Viewport viewport;
    private final Size imageSize;
    private final ScreenBounds screenArea;
    private final UnaryOperator<Bounds> entityMotion;
    private final int drawOrder;

    private final List<Rendering> entries = new ArrayList<>();

    public ReflectionImage(final Viewport viewport, final int drawOrder, final Size imageSize, final ScreenBounds screenArea, final UnaryOperator<Bounds> entityMotion) {
        this.viewport = viewport;
        this.imageSize = imageSize;
        this.screenArea = screenArea;
        this.drawOrder = drawOrder;
        this.entityMotion = entityMotion;
    }

    public void addEntity(final Entity entity, final ShaderSetup overlayShader) {
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
            final var localDistance = screenBounds.center().substract(screenArea.offset());
            final var localOffset = Offset.at(
                    localDistance.x() / viewport.camera().zoom() - render.sprite.width() * render.options.scale() / 2,
                    imageSize.height() - localDistance.y() / viewport.camera().zoom() - render.sprite.height() * render.options.scale() / 2
            );
            final var shaderSetup = ShaderResolver.resolveShader(overlayShader, render.options.shaderSetup(), render.options.isIgnoreOverlayShader());
            entries.add(new Rendering(render.sprite, localOffset, render.options.shaderSetup(shaderSetup).invertVerticalFlip(), render.drawOrder));
        }
    }

    public BufferedImage create() {
        final BufferedImage image = ImageOperations.createImage(imageSize);
        final var graphics2d = (Graphics2D) image.getGraphics();
        final var renderer = new DefaultRenderer();
        renderer.updateContext(() -> graphics2d);
        entries.sort(DRAW_ORDER_COMPARATOR);
        final var clip = new ScreenBounds(imageSize);
        for (final var entry : entries) {
            renderer.drawSprite(entry.sprite, entry.localOffset, entry.options, clip);
        }
        graphics2d.dispose();
        return image;
    }
}