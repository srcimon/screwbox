package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.assets.FontBundle;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.ShaderBundle;
import dev.screwbox.core.graphics.options.TextDrawOptions;

public class PlaygroundApp {
    //TODO use alternate colors to enhance uis in example apps
    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        screwBox.environment().addSystem(e -> {
            e.graphics().world().drawText(e.mouse().position(), """
                {Debo} is the best {{girlfriend}} in the {world! That} is nice""", TextDrawOptions
                .font(FontBundle.BOLDZILLA)
                .scale(1.5)

                .alternativeFont(FontBundle.BOLDZILLA.getCustomColor(Color.YELLOW))
                .alternativeShader(ShaderBundle.DISSOLVE)

                .alternativeFont(FontBundle.SKINNY_SANS.getCustomColor(Color.RED))
                .alternativeShader( ShaderBundle.UNDERWATER)

                .lineSpacing(10)
                .charactersPerLine(20));
        });
        screwBox.graphics().configuration().setBackgroundColor(Color.DARK_BLUE);
        screwBox.start();
    }

}