package io.github.srcimon.screwbox.examples.gameoflife.sidebar;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.ui.UiMenu;
import io.github.srcimon.screwbox.examples.gameoflife.grid.GridComponent;
import io.github.srcimon.screwbox.examples.gameoflife.grid.GridUpdateSystem;

public class SidebarMenu extends UiMenu {

    public SidebarMenu() {
        addItem(engine -> "fps: " + engine.loop().fps()).activeCondition(engine -> false);
        addItem(engine -> "frame: " + engine.loop().frameNumber()).activeCondition(engine -> false);
        addItem("colors").onActivate(engine -> {
            var grid = engine.entities().forcedFetchHaving(GridComponent.class).get(GridComponent.class);
            grid.noNeighboursColor = Color.random();
            grid.oneNeighboursColor = Color.random();
            grid.twoNeighboursColor = Color.random();
        });

        addItem(engine -> engine.entities().isSystemPresent(GridUpdateSystem.class) ? "pause" : "resume")
                .onActivate(engine -> engine.entities().toggleSystem(new GridUpdateSystem()));

        addItem(engine -> engine.graphics().configuration().isUseAntialising() ? "high quality" : "low quality").onActivate(engine -> engine.graphics().configuration().toggleAntialising());
        addItem("mode").onActivate(engine -> engine.graphics().configuration().toggleFullscreen());
        addItem("save").onActivate(engine -> engine.savegame().create("save"));
        addItem("load").onActivate(engine -> engine.savegame().load("save")).activeCondition(engine -> engine.savegame().exists("save"));
        addItem("exit").onActivate(Engine::stop);
    }

}