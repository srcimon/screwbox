package io.github.srcimon.screwbox.examples.platformer.menues;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.ui.UiMenu;
import io.github.srcimon.screwbox.core.ui.WobblyUiLayouter;

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
        engine.ui().setLayouter(new WobblyUiLayouter()).openPreviousMenu();
    }

}
