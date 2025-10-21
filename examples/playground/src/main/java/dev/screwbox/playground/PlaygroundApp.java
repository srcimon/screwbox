package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.assets.FontBundle;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.rendering.AutoTileComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.AutoTileBundle;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.graphics.options.TextDrawOptions;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");

        engine.graphics().camera().setZoom(3);
        engine.environment().enableAllFeatures()
                .addSystem(new LogFpsSystem())
                .addSystem(e -> {
                    e.graphics().canvas().drawText(Offset.at(10, 10), "Count: " + e.environment().entityCount(), TextDrawOptions.font(FontBundle.SKINNY_SANS).scale(5));
                    e.graphics().camera().setZoom(e.graphics().camera().zoom() + e.mouse().unitsScrolled() / 20.0);
                    Vector position = e.mouse().position().snap(16);
                    e.graphics().world().drawSprite(SpriteBundle.MARKER_CROSSHAIR, position.substract(Vector.$(8,8)), SpriteDrawOptions.originalSize().opacity(0.4));
                    if (e.mouse().isDownLeft()) {
                        if (e.navigation().searchAtPosition(position).checkingFor(Archetype.ofSpacial(RenderComponent.class)).selectAll().isEmpty()) {
                            e.environment().addEntity(new Entity()
                                    .bounds(Bounds.atPosition(position, 16, 16))
                                    .add(new AutoTileComponent(AutoTileBundle.ROCKS))
                                    .add(new RenderComponent(Sprite.invisible())));
                        }
                    } else if (e.mouse().isDownRight()) {
                        e.navigation().searchAtPosition(position).checkingFor(Archetype.ofSpacial(RenderComponent.class)).selectAll().forEach(en -> {
                            e.environment().remove(en);
                        });
                    }
                });

        engine.start();
    }
}