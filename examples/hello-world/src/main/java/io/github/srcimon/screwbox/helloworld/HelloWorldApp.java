package io.github.srcimon.screwbox.helloworld;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions;
import io.github.srcimon.screwbox.helloworld.archivements.MouseDragArchivement;

import static io.github.srcimon.screwbox.core.assets.FontBundle.BOLDZILLA;

public class HelloWorldApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Hello World");

        screwBox.archivements().addAllFromPackage("io.github.srcimon.screwbox.helloworld.archivements");


        screwBox.environment().addSystem(engine -> {
            int y = 0;
            for (var archivement : engine.archivements().allArchivements()) {
                engine.graphics().canvas().drawText(engine.graphics().canvas().center().addY(y += 20),
                        archivement.title() + " : " + archivement.score() + " of " + archivement.goal(),
                        TextDrawOptions.font(BOLDZILLA.customColor(archivement.isArchived() ? Color.GREEN : Color.WHITE)).alignCenter());
            }


        });

//        try {
//
//            System.out.println(BestClickerArchivement.class.getDeclaredMethod("supports"));
//        } catch (NoSuchMethodException e) {
//            throw new RuntimeException(e);
//        }

        screwBox.start();
    }
}