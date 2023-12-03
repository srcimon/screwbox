package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.entities.Entities;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;

import static io.github.srcimon.screwbox.core.graphics.Color.RED;
import static io.github.srcimon.screwbox.core.graphics.Color.WHITE;
import static io.github.srcimon.screwbox.core.graphics.Size.square;

public class DefaultLoadingScene implements Scene {

    @Override
    public void initialize(final Entities entities) {
        entities.addSystem(engine -> drawLoadingAnimation(engine.graphics().screen()));
    }

    private void drawLoadingAnimation(final Screen screen) {
        final int distance = 25;
        final double timeSeed = Time.now().milliseconds() / 400.0;
        for (int x = -distance; x <= distance; x += distance) {
            int size = (int) (Math.abs(Math.sin(x * 4 + timeSeed)) * 14);
            Color color = x == 0 ? RED : WHITE;
            Offset position = screen.center().add((int) (-size / 2.0) + x, (int) (-size / 2.0));
            screen.fillRectangle(position, square(size), color);
        }
    }
}
