package de.suzufa.screwbox.examples.platformer.menues;

import java.util.List;
import java.util.function.Function;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.ui.ScrollingUiLayouter;
import de.suzufa.screwbox.core.ui.UiMenu;
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

        addItem(toogleFullscreenLabel)
                .onActivate(engine -> engine.graphics().configuration().toggleFullscreen());

        addItem(toggleAntialisingLabel)
                .onActivate(engine -> engine.graphics().configuration().toggleAntialising());

        addItem("change resolution").onActivate(engine -> {
            List<Dimension> resolutions = engine.graphics().supportedResolutions();
            Dimension resolution = engine.graphics().configuration().resolution();
            engine.ui().setLayouter(new ScrollingUiLayouter());
            engine.ui().openMenu(new ResolutionOptionMenu(new OptionsMenu(caller), resolutions, resolution));
        });

        addItem("delete savegame")
                .activeCondition(engine -> engine.savegame().exists("savegame.sav"))
                .onActivate(engine -> engine.savegame().delete("savegame.sav"));

        addItem("back").onActivate(this::onExit);
    }

}
