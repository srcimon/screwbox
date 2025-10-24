package dev.screwbox.core.scenes;

import dev.screwbox.core.Time;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;

import static dev.screwbox.core.assets.FontBundle.BOLDZILLA;
import static dev.screwbox.core.graphics.Color.RED;
import static dev.screwbox.core.graphics.Color.WHITE;
import static dev.screwbox.core.graphics.options.TextDrawOptions.font;

public class DefaultLoadingScene implements Scene {

    @Override
    public void populate(final Environment environment) {
        environment.addSystem(engine -> {
            final Canvas canvas = engine.graphics().canvas();
            printAnimatedText(canvas, 0, WHITE, "L", "O", "A", "D", "I", "N", "G");
            printAnimatedText(canvas, 20, RED, "S", "C", "R", "E", "W", "B", "O", "X");
        });
    }

    private void printAnimatedText(final Canvas canvas, final int yOffset, final Color color, final String... texts) {
        final var timeSeed = Time.now().milliseconds() / 600.0;
        final var distance = 25;
        int x = -distance * texts.length / 2;
        for (var character : texts) {
            x += distance;
            final int size = (int) (Math.abs(Math.sin(x * 2 + timeSeed)) * 10);
            final Offset position = canvas.center().add((int) (-size / 2.0) + x, (int) (-size / 2.0) + yOffset);
            canvas.drawText(position, character, font(BOLDZILLA.getCustomColor(color)).scale(size / 4.0));
        }
    }
}
