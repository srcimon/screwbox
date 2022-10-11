package de.suzufa.screwbox.playground.debo.menues;

import java.util.List;
import java.util.function.Function;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.ui.ScrollingUiLayouter;
import de.suzufa.screwbox.core.ui.UiMenu;
import de.suzufa.screwbox.core.ui.UiMenuItem;
import de.suzufa.screwbox.core.ui.UiSubMenu;

public class OptionsMenu extends UiSubMenu {

    public OptionsMenu(UiMenu caller) {
        super(caller);

        Function<Engine, String> toogleFullscreenLabel = engine -> engine.graphics().configuration().isFullscreen()
                ? "switch to window"
                : "switch to fullscreen";

        Function<Engine, String> toggleAntialisingLabel = engine -> engine.graphics().configuration().isUseAntialising()
                ? "turn off antialising"
                : "turn on antialising";

        add(new UiMenuItem(toogleFullscreenLabel)
                .onActivate(engine -> engine.graphics().configuration().toggleFullscreen()));

        add(new UiMenuItem(toggleAntialisingLabel)
                .onActivate(engine -> engine.graphics().configuration().toggleAntialising()));

        add(new UiMenuItem("change resolution").onActivate(engine -> {
            List<Dimension> resolutions = engine.graphics().supportedResolutions();
            Dimension resolution = engine.graphics().configuration().resolution();
            engine.ui().setLayouter(new ScrollingUiLayouter());
            engine.ui().openMenu(new ResolutionOptionMenu(new OptionsMenu(caller), resolutions, resolution));
        }));

        add(new UiMenuItem("back").onActivate(engine -> onExit(engine)));
    }

}
