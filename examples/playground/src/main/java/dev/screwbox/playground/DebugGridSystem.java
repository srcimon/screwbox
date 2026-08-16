package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Viewport;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;

public class DebugGridSystem implements EntitySystem {

    private int cellSize;
    private int majorLineSize;

    public DebugGridSystem(int cellSize, int majorLineSize) {
        this.cellSize = cellSize;
        this.majorLineSize = majorLineSize;
    }

    @Override
    public void update(Engine engine) {
        var bgColor = engine.graphics().configuration().backgroundColor();
        Color lineColor = bgColor.invert().opacity(0.15);
        var area = engine.graphics().visibleArea().snapExpand(cellSize);

        for (double y = area.minY(); y < area.maxY(); y += cellSize) {
            for (double x = area.minX(); x < area.maxX(); x += cellSize) {
                engine.graphics().world().drawRectangle(Bounds.atOrigin(x, y, cellSize, cellSize), RectangleDrawOptions.outline(lineColor));
            }
        }

        var area2 = engine.graphics().visibleArea().snapExpand(majorLineSize);
        for (double y = area2.minY(); y < area2.maxY(); y += majorLineSize) {
            for (double x = area2.minX(); x < area2.maxX(); x += majorLineSize) {
                engine.graphics().world().drawRectangle(Bounds.atOrigin(x, y, majorLineSize, majorLineSize), RectangleDrawOptions.outline(lineColor).strokeWidth(3));
            }
        }
    }
}
