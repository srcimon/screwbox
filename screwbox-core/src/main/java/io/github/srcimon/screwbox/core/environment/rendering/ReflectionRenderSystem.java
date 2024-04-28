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

@Order(SystemOrder.PRESENTATION_OVERLAY)
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

                double zoom = engine.graphics().camera().zoom();
                var reflectionOnScreen = engine.graphics().toScreen(reflection);
                final Size size = Size.of(
                        ceil(reflectionOnScreen.size().width() / zoom),
                        ceil(reflectionOnScreen.size().height() / zoom));
                if (size.isValid()) {
                    MirrorRendering rendering = new MirrorRendering(engine.graphics(), mirror, size, reflection);
                    for (final var entity : reflectableEntities) {
                        rendering.tryRenderEntity(entity.get(RenderComponent.class), entity.bounds());
                    }
                    engine.environment().addEntity(rendering.createReflectionEntity());
                }
            });
        }
    }

    //TODO clean up

    private static class MirrorRendering {
        private final Graphics graphics;
        private final Entity reflectionEntity;
        private final Bounds reflectionBounds;
        private final Size size;
        private final SpriteBatch spriteBatch = new SpriteBatch();
        private final ScreenBounds reflectedAreaOnSreen;
        private final ReflectionComponent reflectionComponent;

        public MirrorRendering(Graphics graphics, Entity reflectionEntity, Size size, Bounds reflectionBounds) {
            this.graphics = graphics;
            this.reflectionEntity = reflectionEntity;
            this.reflectionBounds = reflectionBounds;
            this.size = size;
            final var reflectedBounds = reflectionBounds.moveBy(Vector.y(-reflectionBounds.height()));
            reflectedAreaOnSreen = graphics.toScreen(reflectedBounds);
            this.reflectionComponent = reflectionEntity.get(ReflectionComponent.class);
        }

        public Entity createReflectionEntity() {
            return new Entity("reflection").add(
                    new TransformComponent(reflectionBounds),
                    new RenderComponent(
                            createReflectionImage(reflectionComponent.blur),
                            reflectionComponent.drawOrder,
                            SpriteDrawOptions.originalSize().opacity(reflectionComponent.opacityModifier)),
                    new ReflectionResultComponent());
        }

        private Sprite createReflectionImage(final int blur) {
            final var image = new BufferedImage(size.width(), size.height(), BufferedImage.TYPE_INT_ARGB);
            final var graphics = (Graphics2D) image.getGraphics();
            final var renderer = new DefaultRenderer();
            renderer.updateGraphicsContext(() -> graphics, size);
            renderer.drawSpriteBatch(spriteBatch);
            graphics.dispose();
            return Sprite.fromImage(blur > 1 ? new BlurImageFilter(blur).apply(image) : image);
        }

        public void tryRenderEntity(final RenderComponent render, final Bounds bounds) {
            if (render.drawOrder > reflectionComponent.drawOrder) {
                return;
            }
            final ScreenBounds screenBounds = calculateScreenBounds(render, bounds);

            if (screenBounds.intersects(reflectedAreaOnSreen)) {
                var localDistance = screenBounds.center().substract(reflectedAreaOnSreen.offset());
                var localOffset = Offset.at(
                        localDistance.x() / graphics.camera().zoom() - render.sprite.size().width() * render.options.scale() / 2,
                        size.height() - localDistance.y() / graphics.camera().zoom() - render.sprite.size().height() * render.options.scale() / 2
                );
                spriteBatch.add(render.sprite, localOffset, render.options.invertVerticalFlip(), render.drawOrder);
            }
        }

        private ScreenBounds calculateScreenBounds(final RenderComponent render, final Bounds bounds) {
            final Bounds entityRenderArea = Bounds.atPosition(bounds.position(),
                    reflectionEntity.bounds().width() * render.options.scale(),
                    reflectionEntity.bounds().height() * render.options.scale());

            return graphics.toScreenUsingParallax(entityRenderArea, render.parallaxX, render.parallaxY);
        }
    }
}