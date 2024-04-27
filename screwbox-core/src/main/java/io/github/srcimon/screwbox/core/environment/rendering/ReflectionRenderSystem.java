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
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.internal.SpriteBatchEntry;
import io.github.srcimon.screwbox.core.graphics.internal.filter.BlurImageFilter;
import io.github.srcimon.screwbox.core.graphics.internal.renderer.DefaultRenderer;
import io.github.srcimon.screwbox.core.utils.Pixelperfect;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Order(SystemOrder.PRESENTATION_OVERLAY)
public class ReflectionRenderSystem implements EntitySystem {

    private static final Archetype MIRRORS = Archetype.of(ReflectionComponent.class, TransformComponent.class);
    private static final Archetype RELECTED_ENTITIES = Archetype.of(TransformComponent.class, RenderComponent.class);
    private static final Archetype REFLECTION_RENDERERS = Archetype.of(ReflectionResultComponent.class);

    @Override
    public void update(final Engine engine) {
        engine.environment().removeAll(REFLECTION_RENDERERS);
        final var reflectableEntities = engine.environment().fetchAll(RELECTED_ENTITIES);
        for (final Entity reflectionEntity : engine.environment().fetchAll(MIRRORS)) {
            final var visibleArea = Pixelperfect.bounds(engine.graphics().world().visibleArea());
            final var visiblePartOfReflection = reflectionEntity.bounds().intersection(visibleArea);
            visiblePartOfReflection.ifPresent(reflection -> extracted(engine, reflectionEntity, reflection, reflectableEntities));
        }
    }

    //TODO make class
    private void extracted(Engine engine, Entity reflectionEntity, Bounds reflection, List<Entity> reflectableEntities) {
        double zoom = engine.graphics().camera().zoom();
        var reflectionOnScreen = engine.graphics().toScreen(reflection);

        var reflectedArea = reflection.moveBy(Vector.y(-reflection.height()));

        ReflectionComponent reflectionComponent = reflectionEntity.get(ReflectionComponent.class);

        int width = (int) (Math.ceil(reflectionOnScreen.size().width() / zoom));
        int height = (int) (Math.ceil(reflectionOnScreen.size().height() / zoom));

        if (width > 0 && height > 0) {
            List<SpriteBatchEntry> entries = new ArrayList<>();
            for (var entity : reflectableEntities) {
                var render = entity.get(RenderComponent.class);

                Bounds entityRenderArea = Bounds.atPosition(entity.bounds().position(),
                        reflectionEntity.bounds().width() * render.options.scale(),
                        reflectionEntity.bounds().height() * render.options.scale());

                ScreenBounds screenUsingParallax = engine.graphics().toScreenUsingParallax(entityRenderArea, render.parallaxX, render.parallaxY);

                if (screenUsingParallax.intersects(engine.graphics().toScreen(reflectedArea))) {

                    if (render.drawOrder <= reflectionComponent.drawOrder) {
                        var ldist = screenUsingParallax.center().substract(engine.graphics().toScreen(reflectedArea).offset());
                        var ldistOffset = Offset.at(
                                ldist.x() / zoom - render.sprite.size().width() * render.options.scale() / 2,
                                height - ldist.y() / zoom - render.sprite.size().height() * render.options.scale() / 2
                        );

                        entries.add(new SpriteBatchEntry(render.sprite, ldistOffset, render.options.invertVerticalFlip(), render.drawOrder));
                    }
                }


            }
            Size size = Size.of(width, height);
            var image = cereateImage(size, entries);

            Sprite reflectionSprite = reflectionComponent.blur > 1
                    ? Sprite.fromImage(new BlurImageFilter(reflectionComponent.blur).apply(image))
                    : Sprite.fromImage(image);
            RenderComponent renderComponent = new RenderComponent(
                    reflectionSprite,
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

    private static BufferedImage cereateImage(final Size size, final List<SpriteBatchEntry> entries) {
        var image = new BufferedImage(size.width(), size.height(), BufferedImage.TYPE_INT_ARGB);
        var graphics = (Graphics2D) image.getGraphics();

        var renderer = new DefaultRenderer();
        renderer.updateGraphicsContext(() -> graphics, size);
        Collections.sort(entries);
        for (final var entry : entries) {
            renderer.drawSprite(entry.sprite(), entry.offset(), entry.options());
        }

        graphics.dispose();
        return image;
    }
}