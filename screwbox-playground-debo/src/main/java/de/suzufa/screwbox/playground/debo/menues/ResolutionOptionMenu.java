package de.suzufa.screwbox.playground.debo.menues;

import java.util.List;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.ui.UiMenu;
import de.suzufa.screwbox.core.ui.UiSubMenu;
import de.suzufa.screwbox.core.ui.WobblyUiLayouter;

public class ResolutionOptionMenu extends UiSubMenu {

    protected ResolutionOptionMenu(UiMenu caller, List<Dimension> resolutions, Dimension currentResolution) {
        super(caller);
        for (var resolution : resolutions) {
            var item = addItem(resolution.width() + " : " + resolution.height())
                    .onActivate(engine -> engine.graphics().configuration().setResolution(resolution));
            if (resolution.equals(currentResolution)) {
                selectItem(item);
            }
        }
        addItem("back to options").onActivate(engine -> onExit(engine));
    }

    @Override
    public void onExit(Engine engine) {
        engine.ui().setLayouter(new WobblyUiLayouter());
        super.onExit(engine);
    }

}
