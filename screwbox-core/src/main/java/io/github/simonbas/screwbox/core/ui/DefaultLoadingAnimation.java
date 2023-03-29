package io.github.simonbas.screwbox.core.ui;

import io.github.simonbas.screwbox.core.Duration;
import io.github.simonbas.screwbox.core.graphics.Screen;
import io.github.simonbas.screwbox.core.utils.Timer;

import java.util.function.Consumer;

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
        if (visibleBoxes == 9) {
            countingUp = false;
        }
        if (visibleBoxes == 0) {
            countingUp = true;
        }
        if (timer.isTick()) {
            visibleBoxes += countingUp ? 1 : -1;
        }
        int x = -20;
        int y = -20;
        for (int i = 0; i < visibleBoxes; i++) {
            x += 10;
            if (i == 3 || i == 6) {
                y += 10;
                x = -10;
            }
            screen.fillRectangle(screen.center().add(x, y), square(5), WHITE);
        }
    }
}
