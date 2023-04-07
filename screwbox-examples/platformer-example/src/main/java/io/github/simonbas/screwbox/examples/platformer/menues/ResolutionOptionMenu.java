package io.github.simonbas.screwbox.examples.platformer.menues;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.graphics.Dimension;
import io.github.simonbas.screwbox.core.ui.UiMenu;
import io.github.simonbas.screwbox.core.ui.WobblyUiLayouter;

import java.util.List;

public class ResolutionOptionMenu extends UiMenu {

    protected ResolutionOptionMenu(UiMenu caller, List<Dimension> resolutions, Dimension currentResolution) {
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
