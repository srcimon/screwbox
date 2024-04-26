package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.SpritesBundle;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.SystemOrder;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Frame;
import io.github.srcimon.screwbox.core.graphics.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.SpriteFillOptions;
import io.github.srcimon.screwbox.core.graphics.internal.ImageUtil;
import io.github.srcimon.screwbox.core.graphics.internal.renderer.DefaultRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Order(SystemOrder.PRESENTATION_BACKGROUND)//TODO FIX
public class ReflectionRenderSystem implements EntitySystem {

    private static final Archetype REFLECTING_AREAS = Archetype.of(ReflectionComponent.class, TransformComponent.class);
    private static final Archetype RELECTED_ENTITIES = Archetype.of(TransformComponent.class, RenderComponent.class);

   /* private static final class ReflectionArea {

        private final double opacityModifier;
        private final Bounds area;
        private final Bounds reflectedArea;
        final double waveSeed;
        private final boolean useWaveEffect;

        public ReflectionArea(final Bounds area, final ReflectionComponent options, final Time time) {
            this.area = area;
            waveSeed = time.milliseconds() / 500.0;
            useWaveEffect = options.useWaveEffect;
            opacityModifier = useWaveEffect
                    ? (Math.sin(waveSeed) * 0.25 + 0.75) * options.opacityModifier.value()
                    : options.opacityModifier.value();
            reflectedArea = area.moveBy(0, -area.height()).expandTop(useWaveEffect ? 2 : 0);
        }

        public SpriteBatch createRenderBatchFor(final List<Entity> reflectableEntities) {
            final SpriteBatch spriteBatch = new SpriteBatch();
            for (final var reflectableEntity : reflectableEntities) {
                final var reflectableBounds = reflectableEntity.get(TransformComponent.class).bounds;
                if (reflectableBounds.intersects(reflectedArea)) {
                    final RenderComponent render = reflectableEntity.get(RenderComponent.class);
                    if (render.parallaxX == 1 && render.parallaxY == 1) {
                        final var spriteSize = render.sprite.size();
                        final var spriteOrigin = reflectableBounds.position().add(-spriteSize.width() / 2.0, -spriteSize.height() / 2.0);

                        final var xDelta = useWaveEffect ? Math.sin(waveSeed + spriteOrigin.y() / 16) * 2 : 0;
                        final var yDelta = useWaveEffect ? Math.sin(waveSeed) * 2 : 0;
                        final var effectOrigin = Vector.of(
                                spriteOrigin.x() + xDelta,
                                2 * area.minY() - spriteOrigin.y() - spriteSize.height() + yDelta);

                        final var options = render.options
                                .opacity(render.options.opacity().multiply(opacityModifier))
                                .flipVertical(!render.options.isFlipVertical());
                        spriteBatch.addEntry(render.sprite, effectOrigin, options, render.drawOrder);
                    }
                }
            }
            return spriteBatch;
        }
    }*/

    @Override
    public void update(final Engine engine) {
      final Bounds visibleArea = engine.graphics().world().visibleArea();
        final List<Entity> xxx = engine.environment().fetchAll(Archetype.of(ReflectionImageComponent.class));
        engine.environment().remove(xxx);
        for (final Entity reflectionEntity : engine.environment().fetchAll(REFLECTING_AREAS)) {
            final var visibleReflection = reflectionEntity.get(TransformComponent.class).bounds.intersection(visibleArea);
            visibleReflection.ifPresent(reflection -> {
                var reflectionOnScreen = engine.graphics().toScreen(reflection);

                    engine.graphics().screen().drawRectangle(reflectionOnScreen, RectangleDrawOptions.outline(Color.RED).strokeWidth(8));
                    int width = (int) (reflectionOnScreen.size().width() / engine.graphics().camera().zoom());
                    int height = (int) (reflectionOnScreen.size().height() / engine.graphics().camera().zoom());

                if(width > 0 && height > 0) {
                    var image = new BufferedImage(
                            width,
                            height, BufferedImage.TYPE_INT_ARGB);
                    var graphics = (Graphics2D) image.getGraphics();
                    var renderer = new DefaultRenderer();
                    renderer.updateGraphicsContext(() -> graphics, Size.of(image.getWidth(), image.getHeight()));
                    renderer.fillWith(Color.RED);

                    //   renderer.fillWith(SpritesBundle.MOON_SURFACE_16.get(), SpriteFillOptions.scale(2));
//                final var area = new ReflectionArea(reflection, reflectionEntity.get(ReflectionComponent.class), engine.loop().lastUpdate());
//                final SpriteBatch batch = area.createRenderBatchFor(reflectableEntities);
//                engine.graphics().world().drawSpriteBatch(batch, visibleReflection.get());
                    graphics.dispose();
                    Sprite reflectionSprite = Sprite.fromImage(image);
                    RenderComponent renderComponent = new RenderComponent(
                            reflectionSprite,
                            reflectionEntity.get(ReflectionComponent.class).drawOrder,
                            SpriteDrawOptions.originalSize().opacity(reflectionEntity.get(ReflectionComponent.class).opacityModifier)
                    );
                    engine.environment().addEntity(
                            new TransformComponent(reflection),
                            renderComponent,
                            new ReflectionImageComponent()
                    );
                }
            });

        }

    }

    public static void exportPng(final Frame frame, final String fileName) {
        try {
            ImageIO.write(ImageUtil.toBufferedImage(frame.image()), "png", new File(fileName));
        } catch (IOException e) {
            throw new IllegalStateException("could not export png", e);
        }
    }

}
