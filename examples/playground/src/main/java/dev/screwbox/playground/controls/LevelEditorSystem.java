package dev.screwbox.playground.controls;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import dev.screwbox.playground.prototype.AutoTileComponent;

public class LevelEditorSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        Bounds focus = Bounds.atOrigin(engine.mouse().position().snap(16), 16, 16);
        engine.graphics().canvas().drawRectangle(engine.graphics().toCanvas(focus), RectangleDrawOptions.outline(Color.WHITE));

        if (engine.mouse().isDownLeft() && engine.physics().searchAtPosition(engine.mouse().position()).checkingFor(Archetype.ofSpacial()).selectAny().isEmpty()) {
            engine.environment().addEntity(new Entity()
                    .name("tile")
                    .bounds(focus)
                    .add(new AutoTileComponent())
                    .add(new RenderComponent(SpriteBundle.MARKER_CROSSHAIR))
            );
        }
    }
}
