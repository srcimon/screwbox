package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Pixelfont;
import io.github.srcimon.screwbox.core.graphics.Screen;

import java.util.List;
import java.util.stream.Stream;

import static io.github.srcimon.screwbox.core.graphics.Color.RED;
import static io.github.srcimon.screwbox.core.graphics.Color.WHITE;
import static io.github.srcimon.screwbox.core.graphics.Size.square;

public class DefaultLoadingScene implements Scene {

    private List<String> texts = List.of( "L","O","A","D","I","N","G");
    private List<String> texts2 = List.of( "S","C","R","E","W","B","O","X");
    @Override
    public void populate(final Environment environment) {
        environment.addSystem(engine -> drawLoadingAnimation(engine.graphics().screen()));
    }

    private void drawLoadingAnimation(final Screen screen) {
        final int distance = 25;
        final double timeSeed = Time.now().milliseconds() / 600.0;
        int x = -distance * texts.size() / 2;
        for(var character : texts) {
            x+=distance;
            int size = (int) (Math.abs(Math.sin(x * 2 + timeSeed)) * 10);
            Offset position = screen.center().add((int) (-size / 2.0) + x, (int) (-size / 2.0));
            screen.drawTextCentered(position, character, Pixelfont.defaultFont(), size / 4.0);
        }

         x = -distance * texts2.size() / 2;
        for(var character : texts2) {
            x+=distance;
            int size = (int) (Math.abs(Math.sin(x * 2 + timeSeed)) * 10);
            Offset position = screen.center().add((int) (-size / 2.0) + x, (int) (-size / 2.0) + 20);
            screen.drawTextCentered(position, character, Pixelfont.defaultFont(RED), size / 4.0);
        }
    }
}
