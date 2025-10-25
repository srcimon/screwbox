package dev.screwbox.platformer.menues;

import dev.screwbox.core.Engine;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.ui.UiMenu;
import dev.screwbox.core.ui.presets.WobblyUiLayout;

import java.util.List;

public class ResolutionOptionMenu extends UiMenu {

    protected ResolutionOptionMenu(List<Size> resolutions, Size currentResolution) {
        for (var resolution : resolutions) {
            var item = addItem(resolution.width() + " : " + resolution.height())
                    .onActivate(engine -> engine.graphics().configuration().setResolution(resolution));
            if (resolution.equals(currentResolution)) {
                selectItem(item);
            }
        }
        addItem("back to options").onActivate(this::onExit);
    }

    @Override
    public void onExit(Engine engine) {
        engine.ui().setLayout(new WobblyUiLayout()).openPreviousMenu();
    }

}
