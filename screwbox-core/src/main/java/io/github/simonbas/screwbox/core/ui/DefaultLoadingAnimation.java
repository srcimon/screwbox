package io.github.simonbas.screwbox.core.ui;

import io.github.simonbas.screwbox.core.Duration;
import io.github.simonbas.screwbox.core.Time;
import io.github.simonbas.screwbox.core.graphics.Color;
import io.github.simonbas.screwbox.core.graphics.Offset;
import io.github.simonbas.screwbox.core.graphics.Screen;
import io.github.simonbas.screwbox.core.utils.Timer;

import java.util.function.Consumer;

import static io.github.simonbas.screwbox.core.graphics.Color.RED;
import static io.github.simonbas.screwbox.core.graphics.Color.WHITE;
import static io.github.simonbas.screwbox.core.graphics.Dimension.square;

/**
 * The default animation that is shown on {@link Ui#showLoadingAnimation()}.
 * <p>
 * Can be replaced by calling {@link Ui#customizeLoadingAnimation(Consumer)}.
 */
public class DefaultLoadingAnimation implements Consumer<Screen> {

    private final Timer timer = Timer.withInterval(Duration.ofMillis(80));
    private int visibleBoxes = 0;
    private boolean countingUp = true;

    @Override
    public void accept(Screen screen) {
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
