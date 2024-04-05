package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.assets.BundledFonts;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;

import static io.github.srcimon.screwbox.core.graphics.Color.RED;
import static io.github.srcimon.screwbox.core.graphics.Color.WHITE;

public class DefaultLoadingScene implements Scene {

    @Override
    public void populate(final Environment environment) {
        environment.addSystem(engine -> {
            final Screen screen = engine.graphics().screen();
            printAninatedText(screen, 0, WHITE, "L", "O", "A", "D", "I", "N", "G");
            printAninatedText(screen, 20, RED, "S", "C", "R", "E", "W", "B", "O", "X");
        });
    }

    private void printAninatedText(final Screen screen, final int yOffset, final Color color, final String... texts) {
        var timeSeed = Time.now().milliseconds() / 600.0;
        var distance = 25;
        int x = -distance * texts.length / 2;
        for (var character : texts) {
            x += distance;
            final int size = (int) (Math.abs(Math.sin(x * 2 + timeSeed)) * 10);
            final Offset position = screen.center().add((int) (-size / 2.0) + x, (int) (-size / 2.0) + yOffset);
            screen.drawTextCentered(position, character, BundledFonts.SCREWBOX.customColor(color), size / 4.0);
        }
    }
}
