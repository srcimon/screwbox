package io.github.srcimon.screwbox.examples.gameoflife;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.environment.debug.LogFpsSystem;
import io.github.srcimon.screwbox.core.environment.systems.QuitOnKeyPressSystem;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.ui.KeyboardAndMouseInteractor;
import io.github.srcimon.screwbox.examples.gameoflife.camera.CameraControlSystem;
import io.github.srcimon.screwbox.examples.gameoflife.grid.GridComponent;
import io.github.srcimon.screwbox.examples.gameoflife.grid.GridInteractionSystem;
import io.github.srcimon.screwbox.examples.gameoflife.grid.GridRenderSystem;
import io.github.srcimon.screwbox.examples.gameoflife.grid.GridUpdateSystem;
import io.github.srcimon.screwbox.examples.gameoflife.sidebar.SidebarLayouter;
import io.github.srcimon.screwbox.examples.gameoflife.sidebar.SidebarMenu;
import io.github.srcimon.screwbox.examples.gameoflife.sidebar.SidebarOpacitySystem;
import io.github.srcimon.screwbox.examples.gameoflife.sidebar.SidebarRenderer;

public class GameOfLifeExample {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Game Of Life Example");

        engine.environment()
                .addEntity(new GridComponent())
                .addSystem(new GridUpdateSystem())
                .addSystem(new GridRenderSystem())
                .addSystem(new GridInteractionSystem())
                .addSystem(new QuitOnKeyPressSystem(Key.ESCAPE))
                .addSystem(new SidebarOpacitySystem())
                .addSystem(new LogFpsSystem())
                .addSystem(new CameraControlSystem());

        engine.graphics().configuration().setUseAntialiasing(true);

        engine.ui()
                .setRenderer(new SidebarRenderer(Percent.min()))
                .setLayouter(new SidebarLayouter())
                .setInteractor(new KeyboardAndMouseInteractor())
                .openMenu(new SidebarMenu());

        engine.graphics().updateCameraZoom(6);
        engine.start();
    }
}