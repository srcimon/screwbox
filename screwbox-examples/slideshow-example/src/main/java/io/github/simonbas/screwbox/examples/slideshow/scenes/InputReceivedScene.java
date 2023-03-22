package io.github.simonbas.screwbox.examples.slideshow.scenes;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.entities.Entities;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.components.MagnetComponent;
import io.github.simonbas.screwbox.core.entities.components.TransformComponent;
import io.github.simonbas.screwbox.core.graphics.Offset;
import io.github.simonbas.screwbox.core.scenes.Scene;
import io.github.simonbas.screwbox.core.ui.UiMenu;
import io.github.simonbas.screwbox.examples.slideshow.entities.DropVisualizationSystem;

import java.io.File;
import java.util.List;

public class InputReceivedScene implements Scene {

    private final List<File> inputFiles;
    private final Offset dropPosition;

    public InputReceivedScene(Offset dropPosition, List<File> inputFiles) {
        this.inputFiles = inputFiles;
        this.dropPosition = dropPosition;
    }

    @Override
    public void initialize(Entities entities) {
        entities.add(new DropVisualizationSystem());
    }

    @Override
    public void onEnter(Engine engine) {
        System.out.println(engine.entities());
        Entity en = new Entity()
                .add(new MagnetComponent(32, 40))
                .add(new TransformComponent(engine.graphics().worldPositionOf(dropPosition), 4, 4));
        engine.entities()
                .add(en);
        System.out.println(engine.entities().entityCount());
        var menu = new UiMenu();
        menu.addItem("preparing slideshow").activeCondition(e -> false);
        engine.ui().openMenu(menu);

        engine.async().run(this, () -> {
            for (var file : inputFiles) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                engine.ui().currentMenu().get().addItem("loaded... " + file.getName());
            }
        });

    }
}
