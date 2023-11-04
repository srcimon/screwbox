package io.github.srcimon.screwbox.examples.gameoflife.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.entities.EntitySystem;
import io.github.srcimon.screwbox.core.entities.Order;
import io.github.srcimon.screwbox.core.entities.SystemOrder;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.WindowBounds;
import io.github.srcimon.screwbox.examples.gameoflife.components.GridComponent;

@Order(SystemOrder.PRESENTATION_UI)
public class OnScreenDisplaySystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        var grid = engine.entities().forcedFetchHaving(GridComponent.class).get(GridComponent.class);
        WindowBounds buttonArea = new WindowBounds(20, 20, 60, 60);

        engine.graphics().screen()
                .fillRectangle(new WindowBounds(40, 20, 20, 60), grid.oneNeighboursColor)
                .fillRectangle(new WindowBounds(60, 20, 20, 60), grid.twoNeighboursColor)
                .fillRectangle(new WindowBounds(20, 20, 20, 60), grid.noNeighboursColor);

        if (engine.mouse().justClickedLeft() && buttonArea.contains(engine.mouse().position())) {
            grid.noNeighboursColor = Color.random();
            grid.oneNeighboursColor = Color.random();
            grid.twoNeighboursColor = Color.random();
        }
    }
}
