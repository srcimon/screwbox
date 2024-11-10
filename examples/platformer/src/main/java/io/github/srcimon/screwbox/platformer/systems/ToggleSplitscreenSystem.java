package io.github.srcimon.screwbox.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.rendering.CameraBoundsComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.ViewportLayout;
import io.github.srcimon.screwbox.core.graphics.layouts.HorizontalLayout;
import io.github.srcimon.screwbox.core.graphics.layouts.TableLayout;
import io.github.srcimon.screwbox.core.graphics.layouts.VerticalLayout;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.utils.ListUtil;

import static io.github.srcimon.screwbox.core.graphics.SplitscreenOptions.viewports;

public class ToggleSplitscreenSystem implements EntitySystem {
    @Override
    public void update(Engine engine) {
        int viewportCount = engine.graphics().viewports().size();

        if (engine.keyboard().isPressed(Key.T)) {
            engine.graphics().enableSplitscreenMode(viewports(viewportCount + 1).layout(new TableLayout(3, false)).padding(4));
            randomizeAllCameras(engine);
        } else if (engine.keyboard().isPressed(Key.Z)) {
            if (viewportCount == 1) {
                engine.graphics().disableSplitscreenMode();
            } else {
                engine.graphics().enableSplitscreenMode(viewports(viewportCount - 1).padding(4));
                randomizeAllCameras(engine);
            }
        } else if (engine.keyboard().isPressed(Key.U)) {
            ViewportLayout layout = ListUtil.randomFrom(new TableLayout(3, true), new TableLayout(3, false), new TableLayout(), new HorizontalLayout(), new VerticalLayout());
            engine.graphics().enableSplitscreenMode(viewports(engine.graphics().viewports().size()).layout(layout));
            randomizeAllCameras(engine);
        }
    }

    private void randomizeAllCameras(final Engine engine) {
        final var configuration = engine.environment().fetchSingletonComponent(CameraBoundsComponent.class);
        final var exitingEntities = engine.environment().fetchAll(Archetype.of(TransformComponent.class));
        for (var viewport : engine.graphics().viewports()) {
            if (!viewport.equals(engine.graphics().primaryViewport())) {
                Vector cameraMovement = ListUtil.randomFrom(exitingEntities).position().substract(viewport.camera().position());
                viewport.camera().moveWithinVisualBounds(cameraMovement, configuration.cameraBounds);
            }
        }
    }
}
