package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.assets.FontBundle;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.ShaderBundle;
import dev.screwbox.core.graphics.options.TextDrawOptions;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        screwBox.environment().addSystem(e -> {
            e.graphics().world().drawText(e.mouse().position(), "{Debo} is the best {{girlfriend}} in the world!", TextDrawOptions
                .font(FontBundle.BOLDZILLA)
                .scale(1.5)

                .alternativeFont(FontBundle.BOLDZILLA.getCustomColor(Color.YELLOW))
                .alternativeShader(ShaderBundle.CHROMATIC_ABERRATION)
                .secondaryAlternativeFont(FontBundle.BOLDZILLA.getCustomColor(Color.BLUE))
                .secondaryAlternativeShader(ShaderBundle.IRIS_SHOT)

                .lineSpacing(10)
                .charactersPerLine(20));
        });
        screwBox.graphics().configuration().setBackgroundColor(Color.DARK_BLUE);
        screwBox.start();
    }

}