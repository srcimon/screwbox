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
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.internal.renderer.DefaultRenderer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

//TODO: check correct amount of drawn sprites?
@Order(SystemOrder.PRESENTATION_PREPARE)//TODO FIX
public class ReflectionRenderSystem implements EntitySystem {

    private static final Archetype REFLECTING_AREAS = Archetype.of(ReflectionComponent.class, TransformComponent.class);
    private static final Archetype RELECTED_ENTITIES = Archetype.of(TransformComponent.class, RenderComponent.class);

    private static double adjustGreater(final double value) {
        return Math.ceil(value / 16) * 16;
    }

    private static double adjustSmaller(final double value) {
        return Math.floor(value / 16) * 16;
    }

    public static void main(String[] args) {
        System.out.println(adjustSmaller(246.25));
    }

    @Override
    public void update(final Engine engine) {
        final List<Entity> oldReflections = engine.environment().fetchAll(Archetype.of(ReflectionImageComponent.class));
        engine.environment().remove(oldReflections);
        var reflectableEntities = engine.environment().fetchAll(RELECTED_ENTITIES);
        for (final Entity reflectionEntity : engine.environment().fetchAll(REFLECTING_AREAS)) {

            Bounds visibleArea = engine.graphics().world().visibleArea();
            double x = adjustSmaller(visibleArea.origin().x());
            double y = adjustSmaller(visibleArea.origin().y());
            double xdelte =  visibleArea.origin().x() - x;
            double ydelte =  visibleArea.origin().y() - y;
            Bounds visibleAreaAdjusted = Bounds.atOrigin(
                    x,
                    y,
                    visibleArea.width() + xdelte,
                    visibleArea.height() + ydelte);
            final var xxxx = reflectionEntity.get(TransformComponent.class).bounds.intersection(visibleAreaAdjusted);

            if(xxxx.isEmpty() && reflectionEntity.get(TransformComponent.class).bounds.intersection(visibleArea).isPresent()) {
                System.out.println("!!!" + visibleArea.height());
            }

            xxxx.ifPresent(reflection -> {
                var reflectionOnScreen = engine.graphics().toScreen(reflection);

                var reflectedArea = reflection.moveBy(Vector.y(-reflection.height()));


                int width = (int) (Math.ceil(reflectionOnScreen.size().width() / engine.graphics().camera().zoom()));
                int height = (int) (Math.ceil(reflectionOnScreen.size().height() / engine.graphics().camera().zoom()));

                if (width > 0 && height > 0) {
                    var image = new BufferedImage(
                            width,
                            height, BufferedImage.TYPE_INT_ARGB);
                    var graphics = (Graphics2D) image.getGraphics();

                    var renderer = new DefaultRenderer();
                    renderer.updateGraphicsContext(() -> graphics, Size.of(image.getWidth(), image.getHeight()));
                    for (var entity : reflectableEntities) {
                        if (entity.bounds().intersects(reflectedArea)) {
                            var render = entity.get(RenderComponent.class);
                            if (render.parallaxX == 1 && render.parallaxY == 1) {
                                var distance = entity.origin().substract(reflectedArea.origin());
                                var offset = Offset.at(
                                        distance.x(),
                                        height - distance.y() - render.sprite.size().height()

                                );
                                renderer.drawSprite(render.sprite, offset, render.options.invertVerticalFlip());
                            }

                        }

                    }

                    graphics.dispose();
                    Sprite reflectionSprite = Sprite.fromImage(image); // TODO Blur
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

}
