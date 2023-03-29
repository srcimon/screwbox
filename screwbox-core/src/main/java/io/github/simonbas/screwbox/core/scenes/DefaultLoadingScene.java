package io.github.simonbas.screwbox.core.scenes;

import io.github.simonbas.screwbox.core.entities.Entities;
import io.github.simonbas.screwbox.core.graphics.Color;
import io.github.simonbas.screwbox.core.graphics.Offset;

import static io.github.simonbas.screwbox.core.graphics.Color.RED;
import static io.github.simonbas.screwbox.core.graphics.Color.WHITE;
import static io.github.simonbas.screwbox.core.graphics.Dimension.square;

public class DefaultLoadingScene implements Scene {

    @Override
    public void initialize(Entities entities) {
        entities.add(engine -> {
            final int distance = 25;
            final double timeSeed = engine.loop().lastUpdate().milliseconds() / 400.0;
            for (int x = -distance; x <= distance; x += distance) {
                int size = (int) (Math.abs(Math.sin(x * 4 + timeSeed)) * 14);
                Color color = x == 0 ? RED : WHITE;
                Offset position = engine.graphics().screen().center().add((int) (-size / 2.0) + x, (int) (-size / 2.0));
                engine.graphics().screen().fillRectangle(position, square(size), color);
            }
        });
    }
}
