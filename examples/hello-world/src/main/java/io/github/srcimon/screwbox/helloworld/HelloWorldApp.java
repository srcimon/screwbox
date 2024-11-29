package io.github.srcimon.screwbox.helloworld;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions;
import io.github.srcimon.screwbox.core.mouse.MouseButton;

import static io.github.srcimon.screwbox.core.assets.FontBundle.BOLDZILLA;
import static io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions.font;

public class HelloWorldApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Hello World");

        screwBox.archivements().add(new BestClickerArchivement()).add(new EvenBetterClickerArchivement());

        screwBox.environment().addSystem(engine -> {
            int y = 0;
            for(var archivement : engine.archivements().allArchivements()) {
                engine.graphics().canvas().drawText(engine.graphics().canvas().center().addY(y+= 20),
                        archivement.title() + " : " + archivement.score() + " of " + archivement.goal(),
                        TextDrawOptions.font(BOLDZILLA.customColor(archivement.isArchived() ? Color.GREEN : Color.WHITE)).alignCenter());
            }
            if(engine.mouse().isPressed(MouseButton.LEFT)) {
                //TODO find better way
                engine.archivements().progess(BestClickerArchivement.class);
                engine.archivements().progess(EvenBetterClickerArchivement.class);
            }
        });

        screwBox.start();
    }
}