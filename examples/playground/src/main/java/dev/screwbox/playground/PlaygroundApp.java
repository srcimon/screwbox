package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.assets.FontBundle;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.ShaderBundle;
import dev.screwbox.core.graphics.ShaderSetup;
import dev.screwbox.core.graphics.options.TextDrawOptions;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        screwBox.environment().addSystem(e -> {
            e.graphics().world().drawText(e.mouse().position(), "This is a test {text} that is {needed} to debug text drawing. Either you {{supp}}lied the wrong credentials", TextDrawOptions
                .font(FontBundle.BOLDZILLA)
                .scale(1.5)

                .altColor(Color.RED)
                .altShaderSetup(ShaderBundle.OUTLINE)
//
//                .secondaryAltColor(Color.YELLOW)
//                .secondaryAltShaderSetup(ShaderBundle.SEAWATER)

                .lineSpacing(10)
                .charactersPerLine(20));
        });
        screwBox.graphics().configuration().setBackgroundColor(Color.DARK_BLUE);
        screwBox.start();
    }

}