package de.suzufa.screwbox.playground.debo.menues;

import java.util.List;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.ui.UiMenu;
import de.suzufa.screwbox.core.ui.UiMenuItem;
import de.suzufa.screwbox.core.ui.UiSubMenu;
import de.suzufa.screwbox.core.ui.WobblyUiLayouter;

public class ResolutionOptionMenu extends UiSubMenu {

    private class ResolutionMenuItem extends UiMenuItem {

        public ResolutionMenuItem(Dimension resolution) {
            super(resolution.width() + " : " + resolution.height());
            onActivate(engine -> engine.graphics().configuration().setResolution(resolution));
        }
    }

    protected ResolutionOptionMenu(UiMenu caller, List<Dimension> resolutions, Dimension currentResolution) {
        super(caller);
        for (var resolution : resolutions) {
            ResolutionMenuItem item = new ResolutionMenuItem(resolution);
            add(item);
            if (resolution.equals(currentResolution)) {
                selectItem(item);
            }
        }
        add(new UiMenuItem("back to options").onActivate(engine -> onExit(engine)));
    }

    @Override
    public void onExit(Engine engine) {
        engine.ui().setLayouter(new WobblyUiLayouter());
        super.onExit(engine);
    }

}
