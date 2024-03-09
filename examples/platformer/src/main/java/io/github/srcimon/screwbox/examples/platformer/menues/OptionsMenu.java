package io.github.srcimon.screwbox.examples.platformer.menues;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
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
                ? "antialising on"
                : "antialising off")
                .onActivate(engine -> engine.graphics().configuration().toggleAntialising());

        addItem(engine -> engine.graphics().configuration().lightmapScale() == 4
                ? "light quality low"
                : "light quality high").onActivate(engine ->
                engine.graphics().configuration().setLightmapScale(engine.graphics().configuration().lightmapScale() == 4
                        ? 2 : 4));

        addItem("change resolution").onActivate(engine -> {
            List<Size> resolutions = engine.graphics().supportedResolutions();
            Size resolution = engine.graphics().configuration().resolution();
            engine.ui().setLayouter(new ScrollingUiLayouter());
            engine.ui().openMenu(new ResolutionOptionMenu(resolutions, resolution));
        });

        addItem(engine -> "Music Volume %.0f".formatted(engine.audio().configuration().musicVolume().value() / 0.25 * 25))
                .onActivate(engine -> engine.audio().configuration().setMusicVolume(engine.audio().configuration().musicVolume().value() + 0.25 > 1
                        ? Percent.zero()
                        : Percent.of(engine.audio().configuration().musicVolume().value() + 0.25)));

        addItem(engine -> "Effects Volume %.0f".formatted(engine.audio().configuration().effectVolume().value() / 0.25 * 25))
                .onActivate(engine -> engine.audio().configuration().setEffectVolume(engine.audio().configuration().effectVolume().value() + 0.25 > 1
                        ? Percent.zero()
                        : Percent.of(engine.audio().configuration().effectVolume().value() + 0.25)));

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
