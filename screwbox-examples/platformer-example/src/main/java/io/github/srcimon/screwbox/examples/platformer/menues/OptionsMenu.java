package io.github.srcimon.screwbox.examples.platformer.menues;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.ui.ScrollingUiLayouter;
import io.github.srcimon.screwbox.core.ui.UiMenu;

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
            List<Size> resolutions = engine.graphics().supportedResolutions();
            Size resolution = engine.graphics().configuration().resolution();
            engine.ui().setLayouter(new ScrollingUiLayouter());
            engine.ui().openMenu(new ResolutionOptionMenu(resolutions, resolution));
        });

        addItem("delete savegame")
                .activeCondition(engine -> engine.environment().savegameExists("savegame.sav"))
                .onActivate(engine -> engine.environment().deleteSavegame("savegame.sav"));

        addItem("back").onActivate(this::onExit);
    }

    @Override
    public void onExit(Engine engine) {
        engine.ui().openPreviousMenu();
    }
}
