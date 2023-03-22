package io.github.simonbas.screwbox.examples.slideshow.scenes;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.scenes.Scene;
import io.github.simonbas.screwbox.core.ui.UiMenu;
import io.github.simonbas.screwbox.examples.slideshow.PDFToSprite;

import java.io.File;
import java.util.List;

public class InputReceivedScene implements Scene {

    private final List<File> inputFiles;

    public InputReceivedScene(List<File> inputFiles) {
        this.inputFiles = inputFiles;
    }

    @Override
    public void onEnter(Engine engine) {
        var menu = new UiMenu();
        menu.addItem("preparing slideshow").activeCondition(e -> false);
        engine.ui().openMenu(menu);

        engine.async().run(this, () -> {
            var sprites = inputFiles.stream()
                    .filter(File::isFile)
                    .filter(File::exists)
                    .filter(file -> file.getName().endsWith(".pdf"))
                    .flatMap(pdfFile -> PDFToSprite.fromPdf(pdfFile).stream())
                    .toList();
            engine.scenes().add(new SlideshowScene(sprites)).switchTo(SlideshowScene.class);
        });
    }

    @Override
    public void onExit(Engine engine) {
        engine.ui().closeMenu();
    }
}
