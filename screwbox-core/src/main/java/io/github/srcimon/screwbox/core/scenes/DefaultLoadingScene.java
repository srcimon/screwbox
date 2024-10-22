package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;

import static io.github.srcimon.screwbox.core.assets.FontBundle.BOLDZILLA;
import static io.github.srcimon.screwbox.core.graphics.Color.RED;
import static io.github.srcimon.screwbox.core.graphics.Color.WHITE;
import static io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions.font;

public class DefaultLoadingScene implements Scene {

    @Override
    public void populate(final Environment environment) {
        environment.addSystem(engine -> {
            final Canvas canvas = engine.graphics().canvas();
            printAninatedText(canvas, 0, WHITE, "L", "O", "A", "D", "I", "N", "G");
            printAninatedText(canvas, 20, RED, "S", "C", "R", "E", "W", "B", "O", "X");
        });
    }

    private void printAninatedText(final Canvas canvas, final int yOffset, final Color color, final String... texts) {
        var timeSeed = Time.now().milliseconds() / 600.0;
        var distance = 25;
        int x = -distance * texts.length / 2;
        for (var character : texts) {
            x += distance;
            final int size = (int) (Math.abs(Math.sin(x * 2 + timeSeed)) * 10);
            final Offset position = canvas.center().add((int) (-size / 2.0) + x, (int) (-size / 2.0) + yOffset);
            canvas.drawText(position, character, font(BOLDZILLA.getCustomColor(color)).scale(size / 4.0));
        }
    }
}
