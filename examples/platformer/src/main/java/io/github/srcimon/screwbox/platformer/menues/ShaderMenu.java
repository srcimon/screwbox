package io.github.srcimon.screwbox.platformer.menues;

import dev.screwbox.core.graphics.ShaderBundle;
import dev.screwbox.core.ui.UiMenu;

import java.util.Arrays;

import static java.util.Objects.nonNull;

public class ShaderMenu extends UiMenu {

    public ShaderMenu() {
        Arrays.stream(ShaderBundle.values())
                .filter(shaderBundle -> !shaderBundle.get().shader().isAnimated())
                .forEach(shader -> addItem(shader.name())
                        .onActivate(engine -> {
                            engine.graphics().configuration().setOverlayShader(shader);
                            engine.ui().openPreviousMenu();
                        })
                        .activeCondition(engine -> !shader.get().equals(engine.graphics().configuration().overlayShader())));

        addItem("disable shader").onActivate(engine -> {
                    engine.graphics().configuration().disableOverlayShader();
                    engine.ui().openPreviousMenu();
                })
                .activeCondition(engine -> nonNull(engine.graphics().configuration().overlayShader()));

        addItem("back").onActivate(engine -> engine.ui().openPreviousMenu());
    }
}
