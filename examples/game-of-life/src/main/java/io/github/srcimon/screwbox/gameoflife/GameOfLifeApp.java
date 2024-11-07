package io.github.srcimon.screwbox.gameoflife;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.environment.core.LogFpsSystem;
import io.github.srcimon.screwbox.core.environment.core.QuitOnKeySystem;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.ui.KeyboardAndMouseInteractor;
import io.github.srcimon.screwbox.gameoflife.camera.CameraControlSystem;
import io.github.srcimon.screwbox.gameoflife.grid.GridComponent;
import io.github.srcimon.screwbox.gameoflife.grid.GridInteractionSystem;
import io.github.srcimon.screwbox.gameoflife.grid.GridRenderSystem;
import io.github.srcimon.screwbox.gameoflife.grid.GridUpdateSystem;
import io.github.srcimon.screwbox.gameoflife.sidebar.SidebarLayouter;
import io.github.srcimon.screwbox.gameoflife.sidebar.SidebarMenu;
import io.github.srcimon.screwbox.gameoflife.sidebar.SidebarOpacitySystem;
import io.github.srcimon.screwbox.gameoflife.sidebar.SidebarRenderer;

public class GameOfLifeApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Game Of Life");

        engine.environment()
                .enableRendering()
                .addEntity(new GridComponent())
                .addSystem(new GridUpdateSystem())
                .addSystem(new GridRenderSystem())
                .addSystem(new GridInteractionSystem())
                .addSystem(new QuitOnKeySystem(Key.ESCAPE))
                .addSystem(new SidebarOpacitySystem())
                .addSystem(new LogFpsSystem())
                .addSystem(new CameraControlSystem());

        engine.graphics().configuration().setUseAntialiasing(true);

        engine.ui()
                .setRenderer(new SidebarRenderer(Percent.zero()))
                .setLayouter(new SidebarLayouter())
                .setInteractor(new KeyboardAndMouseInteractor())
                .openMenu(new SidebarMenu());

        engine.graphics().camera().setZoomRestriction(2, 8).setZoom(4);

        engine.start();
    }
}