package dev.screwbox.platformer.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.rendering.CameraBoundsComponent;
import dev.screwbox.core.graphics.ViewportLayout;
import dev.screwbox.core.graphics.layouts.HorizontalLayout;
import dev.screwbox.core.graphics.layouts.TableLayout;
import dev.screwbox.core.graphics.layouts.VerticalLayout;
import dev.screwbox.core.keyboard.Key;
import dev.screwbox.core.utils.ListUtil;

import static dev.screwbox.core.graphics.SplitScreenOptions.viewports;

public class ToggleSplitscreenSystem implements EntitySystem {

    private static final Archetype CAMERA_BOUNDS = Archetype.ofSpacial(CameraBoundsComponent.class);

    @Override
    public void update(Engine engine) {
        int viewportCount = engine.graphics().viewports().size();

        if (engine.keyboard().isPressed(Key.T)) {
            engine.graphics().enableSplitScreenMode(viewports(viewportCount + 1).layout(new TableLayout(3, false)).padding(4));
            randomizeAllCameras(engine);
        } else if (engine.keyboard().isPressed(Key.Z)) {
            if (viewportCount == 1) {
                engine.graphics().disableSplitScreenMode();
            } else {
                engine.graphics().enableSplitScreenMode(viewports(viewportCount - 1).padding(4));
                randomizeAllCameras(engine);
            }
        } else if (engine.keyboard().isPressed(Key.U)) {
            ViewportLayout layout = ListUtil.randomFrom(new TableLayout(3, true), new TableLayout(3, false), new TableLayout(), new HorizontalLayout(), new VerticalLayout());
            engine.graphics().enableSplitScreenMode(viewports(engine.graphics().viewports().size()).layout(layout));
            randomizeAllCameras(engine);
        }
    }

    private void randomizeAllCameras(final Engine engine) {
        final var configuration = engine.environment().fetchSingleton(CAMERA_BOUNDS);
        final var exitingEntities = engine.environment().fetchAll(Archetype.ofSpacial());
        for (var viewport : engine.graphics().viewports()) {
            if (!viewport.equals(engine.graphics().primaryViewport())) {
                Vector cameraMovement = ListUtil.randomFrom(exitingEntities).position().substract(viewport.camera().position());
                viewport.camera().moveWithinVisualBounds(cameraMovement, configuration.bounds());
            }
        }
    }
}
