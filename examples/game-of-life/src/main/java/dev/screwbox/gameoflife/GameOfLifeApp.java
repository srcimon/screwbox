package dev.screwbox.gameoflife;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.core.QuitOnKeySystem;
import dev.screwbox.core.keyboard.Key;
import dev.screwbox.core.ui.presets.KeyboardAndMouseInteractor;
import dev.screwbox.gameoflife.grid.GridComponent;
import dev.screwbox.gameoflife.sidebar.SidebarDesign;
import dev.screwbox.gameoflife.sidebar.SidebarLayout;
import dev.screwbox.gameoflife.sidebar.SidebarMenu;

public class GameOfLifeApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Game Of Life");

        engine.environment()
                .enableRendering()
                .addSystemsFromPackage("dev.screwbox.gameoflife")
                .addEntity(new GridComponent())
                .addSystem(new QuitOnKeySystem(Key.ESCAPE))
                .addSystem(new LogFpsSystem());

        engine.ui()
                .setDesign(new SidebarDesign(Percent.zero()))
                .setLayout(new SidebarLayout())
                .setInteractor(new KeyboardAndMouseInteractor())
                .openMenu(new SidebarMenu());

        engine.graphics().camera().setZoomRestriction(2, 8).setZoom(4);

        engine.start();
    }
}