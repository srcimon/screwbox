package io.github.srcimon.screwbox.gameoflife.sidebar;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.EntitySystem;

public class SidebarOpacitySystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        Percent opacity = Percent.of((300 - engine.mouse().offset().x()) / 300.0);
        engine.ui().setRenderer(new SidebarRenderer(opacity));
    }
}
