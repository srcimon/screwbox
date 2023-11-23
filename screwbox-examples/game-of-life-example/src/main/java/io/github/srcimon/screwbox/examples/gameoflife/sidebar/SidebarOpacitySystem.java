package io.github.srcimon.screwbox.examples.gameoflife.sidebar;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.entities.EntitySystem;

public class SidebarOpacitySystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        Percent opacity = Percent.of((300 - engine.mouse().offset().x()) / 300.0);
        engine.ui().setRenderer(new SidebarRenderer(opacity));
    }
}
