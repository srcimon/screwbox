package io.github.srcimon.screwbox.examples.gameoflife.sidebar;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.ui.UiMenu;
import io.github.srcimon.screwbox.examples.gameoflife.grid.GridComponent;
import io.github.srcimon.screwbox.examples.gameoflife.grid.GridUpdateSystem;

public class SidebarMenu extends UiMenu {

    public SidebarMenu() {
        addItem("change colors").onActivate(engine -> {
            var grid = engine.entities().forcedFetchHaving(GridComponent.class).get(GridComponent.class);
            grid.noNeighboursColor = Color.random();
            grid.oneNeighboursColor = Color.random();
            grid.twoNeighboursColor = Color.random();
        });

        addItem(engine -> engine.entities().isSystemPresent(GridUpdateSystem.class) ? "pause" : "resume")
                .onActivate(engine -> engine.entities().toggleSystem(new GridUpdateSystem()));

        addItem("save").onActivate(engine -> engine.savegame().create("save"));
        addItem("load").onActivate(engine -> engine.savegame().load("save")).activeCondition(engine -> engine.savegame().exists("save"));
        addItem("exit").onActivate(engine -> engine.stop());
    }
}