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
import java.util.List;

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
            visibleAreaOfMirror.ifPresent(reflection -> new MirrorRendering(engine, mirror, reflection, reflectableEntities).run());
        }
    }

    private class MirrorRendering {
        private final Engine engine;
        private final Entity reflectionEntity;
        private final Bounds reflection;
        private final List<Entity> reflectableEntities;

        public MirrorRendering(Engine engine, Entity reflectionEntity, Bounds reflection, List<Entity> reflectableEntities) {
            this.engine = engine;
            this.reflectionEntity = reflectionEntity;
            this.reflection = reflection;
            this.reflectableEntities = reflectableEntities;
        }

        public void run() {
            double zoom = engine.graphics().camera().zoom();
            var reflectionOnScreen = engine.graphics().toScreen(reflection);

            var reflectedArea = reflection.moveBy(Vector.y(-reflection.height()));

            ReflectionComponent reflectionComponent = reflectionEntity.get(ReflectionComponent.class);

            final Size size = Size.of(
                    ceil(reflectionOnScreen.size().width() / zoom),
                    ceil(reflectionOnScreen.size().height() / zoom));

            if (size.isValid()) {
                SpriteBatch spriteBatch = new SpriteBatch();
                for (var entity : reflectableEntities) {
                    var render = entity.get(RenderComponent.class);
                    if (render.drawOrder <= reflectionComponent.drawOrder) {
                        Bounds entityRenderArea = Bounds.atPosition(entity.bounds().position(),
                                reflectionEntity.bounds().width() * render.options.scale(),
                                reflectionEntity.bounds().height() * render.options.scale());

                        ScreenBounds screenUsingParallax = engine.graphics().toScreenUsingParallax(entityRenderArea, render.parallaxX, render.parallaxY);

                        if (screenUsingParallax.intersects(engine.graphics().toScreen(reflectedArea))) {
                            var ldist = screenUsingParallax.center().substract(engine.graphics().toScreen(reflectedArea).offset());
                            var ldistOffset = Offset.at(
                                    ldist.x() / zoom - render.sprite.size().width() * render.options.scale() / 2,
                                    size.height() - ldist.y() / zoom - render.sprite.size().height() * render.options.scale() / 2
                            );

                            spriteBatch.add(render.sprite, ldistOffset, render.options.invertVerticalFlip(), render.drawOrder);
                        }
                    }
                }
                final var sprite = createReflection(size, spriteBatch, reflectionComponent.blur);
                RenderComponent renderComponent = new RenderComponent(
                        sprite,
                        reflectionComponent.drawOrder,
                        SpriteDrawOptions.originalSize().opacity(reflectionComponent.opacityModifier)
                );

                engine.environment().addEntity(
                        new TransformComponent(reflection),
                        renderComponent,
                        new ReflectionResultComponent()
                );
            }
        }
    }

    private Sprite createReflection(final Size size, final SpriteBatch spriteBatch, int blur) {
        var image = new BufferedImage(size.width(), size.height(), BufferedImage.TYPE_INT_ARGB);
        var graphics = (Graphics2D) image.getGraphics();

        var renderer = new DefaultRenderer();
        renderer.updateGraphicsContext(() -> graphics, size);
        renderer.drawSpriteBatch(spriteBatch);

        graphics.dispose();
        return blur > 1
                ? Sprite.fromImage(new BlurImageFilter(blur).apply(image))
                : Sprite.fromImage(image);
    }
}