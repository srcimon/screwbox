package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.SystemOrder;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.graphics.Graphics;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.internal.filter.BlurImageFilter;
import io.github.srcimon.screwbox.core.graphics.internal.renderer.DefaultRenderer;
import io.github.srcimon.screwbox.core.utils.Pixelperfect;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.ceil;

@Order(SystemOrder.PRESENTATION_PREPARE)
public class ReflectionRenderSystem implements EntitySystem {

    private static final Archetype MIRRORS = Archetype.of(ReflectionComponent.class, TransformComponent.class);
    private static final Archetype RELECTED_ENTITIES = Archetype.of(TransformComponent.class, RenderComponent.class);
    private static final Archetype REFLECTION_RENDERERS = Archetype.of(ReflectionResultComponent.class);

    @Override
    public void update(final Engine engine) {
        engine.environment().removeAll(REFLECTION_RENDERERS);
        final var reflectableEntities = engine.environment().fetchAll(RELECTED_ENTITIES);
        for (final Entity mirror : engine.environment().fetchAll(MIRRORS)) {
            final var visibleArea = Pixelperfect.bounds(engine.graphics().world().visibleArea());
            final var visibleAreaOfMirror = mirror.bounds().intersection(visibleArea);
            visibleAreaOfMirror.ifPresent(reflection -> {
                var reflectionOnScreen = engine.graphics().toScreen(reflection);
                final Size size = Size.of(
                        ceil(reflectionOnScreen.size().width() / engine.graphics().camera().zoom()),
                        ceil(reflectionOnScreen.size().height() / engine.graphics().camera().zoom()));
                if (size.isValid()) {
                    final var reflectionConfig = mirror.get(ReflectionComponent.class);
                    final var reflectedBounds = reflection.moveBy(Vector.y(-reflection.height()));
                    final var reflectedAreaOnSreen = engine.graphics().toScreen(reflectedBounds);
                    final var reflectionImage = new ReflectionImage(engine.graphics(), reflectionConfig.drawOrder, size, reflectedAreaOnSreen);
                    for (final var entity : reflectableEntities) {
                        reflectionImage.addEntity(entity.get(RenderComponent.class), entity, entity.position());
                    }

                    engine.environment().addEntity("reflection",
                            new TransformComponent(reflection),
                            new RenderComponent(
                                    reflectionImage.create(reflectionConfig.blur),
                                    reflectionConfig.drawOrder,
                                    SpriteDrawOptions.originalSize().opacity(reflectionConfig.opacityModifier)),
                            new ReflectionResultComponent());
                }
            });
        }
    }

    private static class ReflectionImage {
        private final Graphics graphics;
        private final Size imageSize;
        private final SpriteBatch spriteBatch = new SpriteBatch();
        private final ScreenBounds reflectedAreaOnSreen;
        private final int drawOrder;

        public ReflectionImage(final Graphics graphics, final int drawOrder, final Size imageSize, final ScreenBounds reflectedAreaOnSreen) {
            this.graphics = graphics;
            this.imageSize = imageSize;
            this.reflectedAreaOnSreen = reflectedAreaOnSreen;
            this.drawOrder = drawOrder;
        }

        public void addEntity(final RenderComponent render, final Entity entity, final Vector position) {
            if (render.drawOrder > drawOrder) {
                return;
            }
            final Bounds entityRenderArea = Bounds.atPosition(position,
                    entity.bounds().width() * render.options.scale(),
                    entity.bounds().height() * render.options.scale());

            final ScreenBounds screenBounds = graphics.toScreenUsingParallax(entityRenderArea, render.parallaxX, render.parallaxY);

            if (screenBounds.intersects(reflectedAreaOnSreen)) {
                var localDistance = screenBounds.center().substract(reflectedAreaOnSreen.offset());
                var localOffset = Offset.at(
                        localDistance.x() / graphics.camera().zoom() - render.sprite.size().width() * render.options.scale() / 2,
                        imageSize.height() - localDistance.y() / graphics.camera().zoom() - render.sprite.size().height() * render.options.scale() / 2
                );
                spriteBatch.add(render.sprite, localOffset, render.options.invertVerticalFlip(), render.drawOrder);
            }
        }

        public Sprite create(final int blur) {
            final var image = new BufferedImage(imageSize.width(), imageSize.height(), BufferedImage.TYPE_INT_ARGB);
            final var graphics = (Graphics2D) image.getGraphics();
            final var renderer = new DefaultRenderer();
            renderer.updateGraphicsContext(() -> graphics, imageSize);
            renderer.drawSpriteBatch(spriteBatch);
            graphics.dispose();
            return Sprite.fromImage(blur > 1 ? new BlurImageFilter(blur).apply(image) : image);
        }
    }
}