package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Time;
import dev.screwbox.core.Vector;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.Viewport;
import dev.screwbox.core.graphics.internal.ReflectionImage;
import dev.screwbox.core.graphics.internal.filter.DistortionImageFilter;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.utils.MathUtil;
import dev.screwbox.core.utils.Pixelperfect;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.List;
import java.util.function.UnaryOperator;

import static dev.screwbox.core.environment.Order.PRESENTATION_WORLD;
import static dev.screwbox.core.graphics.internal.ImageOperations.applyFilter;
import static java.lang.Math.ceil;

@ExecutionOrder(PRESENTATION_WORLD)
public class ReflectionRenderSystem implements EntitySystem {

    private static final Archetype RENDERS = Archetype.ofSpacial(RenderComponent.class);
    private static final Archetype MIRRORS = Archetype.ofSpacial(ReflectionComponent.class);

    @Override
    public void update(final Engine engine) {
        final List<Entity> renderEntities = engine.environment().fetchAll(RENDERS);
        final List<Entity> mirrors = engine.environment().fetchAll(MIRRORS);
        if (!renderEntities.isEmpty() && !mirrors.isEmpty()) {
            for (final var viewport : engine.graphics().viewports()) {
                renderReflectionsOnViewport(renderEntities, engine, mirrors, viewport);
            }
        }
    }

    private void renderReflectionsOnViewport(final List<Entity> renderEntities, final Engine engine, final List<Entity> mirrors, final Viewport viewport) {
        final var visibleArea = Pixelperfect.bounds(viewport.visibleArea());
        final var zoom = viewport.camera().zoom();
        final var overlayShader = engine.graphics().configuration().overlayShader();

        for (final Entity mirror : mirrors) {
            final var visibleAreaOfMirror = mirror.bounds().intersection(visibleArea);
            visibleAreaOfMirror.ifPresent(intersection -> {
                // keep height to prevent graphic issue
                final var reflection = Bounds.atOrigin(intersection.minX(), mirror.bounds().minY(), intersection.width(), mirror.bounds().height());
                final var reflectionOnScreen = viewport.toCanvas(reflection);
                final Size size = Size.of(
                        ceil(reflectionOnScreen.width() / zoom),
                        ceil(reflectionOnScreen.height() / zoom));
                if (size.isValid()) {
                    final var reflectionConfig = mirror.get(ReflectionComponent.class);
                    final double seed = engine.loop().time().milliseconds() * engine.loop().speed();
                    final UnaryOperator<Bounds> entityMotion = reflectionConfig.applyWaveDistortionProjection
                            ? bounds -> bounds.moveBy(
                            MathUtil.fastSin((seed + bounds.position().y() * reflectionConfig.frequencyX * 40) * reflectionConfig.speed) * reflectionConfig.amplitude,
                            MathUtil.fastSin((seed + bounds.position().x() * reflectionConfig.frequencyX * 20) * reflectionConfig.speed / 2.0) * reflectionConfig.amplitude)
                            : null;
                    final var reflectedBounds = reflection.moveBy(Vector.y(-reflection.height()));
                    final var reflectedAreaOnScreen = viewport.toCanvas(reflectedBounds);
                    final var reflectionImage = new ReflectionImage(viewport, reflectionConfig.drawOrder, size, reflectedAreaOnScreen, entityMotion);

                    for (final var entity : renderEntities) {
                        reflectionImage.addEntity(entity, overlayShader);
                    }
                    final Asset<Sprite> reflectionSprite = Asset.asset(() -> createReflectionSprite(reflection, reflectionImage, reflectionConfig, seed));
                    engine.async().run(ReflectionRenderSystem.class, reflectionSprite::load);
                    viewport.canvas().drawSprite(reflectionSprite, viewport.toCanvas(reflection.origin()), SpriteDrawOptions.scaled(zoom).opacity(reflectionConfig.opacityModifier).ignoreOverlayShader().drawOrder(reflectionConfig.drawOrder));
                }
            });
        }
    }

    private Sprite createReflectionSprite(final Bounds reflection, final ReflectionImage reflectionImage, final ReflectionComponent reflectionConfig, double seed) {
        final var image = reflectionImage.create();
        Time t = Time.now();
        DistortionImageFilter.DistortionConfig filterConfig = createFilterConfig(reflection.origin(), reflectionConfig, seed);
//        BufferedImage image1 = applyFilter(image, new DistortionImageFilter(image, filterConfig));
       new DistortionImageFilter(image, filterConfig).apply(image);
        System.out.println(Duration.since(t).nanos());
        return reflectionConfig.applyWaveDistortionPostFilter
                ? Sprite.fromImage(image)
                : Sprite.fromImage(image);
    }

    private DistortionImageFilter.DistortionConfig createFilterConfig(final Vector origin, final ReflectionComponent reflectionConfig, final double seed) {
        return new DistortionImageFilter.DistortionConfig(
                seed * reflectionConfig.speed,
                reflectionConfig.amplitude,
                reflectionConfig.frequencyX,
                reflectionConfig.frequencyY,
                Offset.at(origin.x(), origin.y()));
    }

}
