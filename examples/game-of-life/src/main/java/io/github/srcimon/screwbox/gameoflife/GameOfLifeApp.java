package io.github.srcimon.screwbox.gameoflife;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.core.QuitOnKeySystem;
import dev.screwbox.core.keyboard.Key;
import dev.screwbox.core.ui.presets.KeyboardAndMouseInteractor;
import io.github.srcimon.screwbox.gameoflife.grid.GridComponent;
import io.github.srcimon.screwbox.gameoflife.sidebar.SidebarLayouter;
import io.github.srcimon.screwbox.gameoflife.sidebar.SidebarMenu;
import io.github.srcimon.screwbox.gameoflife.sidebar.SidebarRenderer;

public class GameOfLifeApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Game Of Life");

        engine.environment()
                .enableRendering()
                .addSystemsFromPackage("io.github.srcimon.screwbox.gameoflife")
                .addEntity(new GridComponent())
                .addSystem(new QuitOnKeySystem(Key.ESCAPE))
                .addSystem(new LogFpsSystem());

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