package io.github.simonbas.screwbox.examples.platformer.menues;

import io.github.simonbas.screwbox.core.graphics.Dimension;
import io.github.simonbas.screwbox.core.ui.ScrollingUiLayouter;
import io.github.simonbas.screwbox.core.ui.UiMenu;

import java.util.List;

public class OptionsMenu extends UiMenu {

    public OptionsMenu() {
        addItem(engine -> engine.graphics().configuration().isFullscreen()
                ? "switch to window"
                : "switch to fullscreen")
                .onActivate(engine -> engine.graphics().configuration().toggleFullscreen());

        addItem(engine -> engine.graphics().configuration().isUseAntialising()
                ? "turn off antialising"
                : "turn on antialising")
                .onActivate(engine -> engine.graphics().configuration().toggleAntialising());

        addItem("change resolution").onActivate(engine -> {
            List<Dimension> resolutions = engine.graphics().supportedResolutions();
            Dimension resolution = engine.graphics().configuration().resolution();
            engine.ui().setLayouter(new ScrollingUiLayouter());
            engine.ui().openMenu(new ResolutionOptionMenu(this, resolutions, resolution));
        });

        addItem("delete savegame")
                .activeCondition(engine -> engine.savegame().exists("savegame.sav"))
                .onActivate(engine -> engine.savegame().delete("savegame.sav"));

        addItem("back").onActivate(engine -> engine.ui().openPreviousMenu());
    }

}
