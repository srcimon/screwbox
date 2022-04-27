package de.suzufa.screwbox.playground.debo.systems;

import static de.suzufa.screwbox.core.graphics.window.WindowText.textCentered;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.UpdatePriority;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.Font.Style;
import de.suzufa.screwbox.core.graphics.window.Window;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.playground.debo.components.TextComponent;

public class PrintSystem implements EntitySystem {

    private static final Font TITLE_FONT = new Font("Futura", 72, Style.BOLD);
    private static final Font SUBTEXT_FONT = new Font("Futura", 36, Style.BOLD);
    private static final Archetype TEXTS = Archetype.of(TextComponent.class);

    @Override
    public void update(Engine engine) {
        for (var entity : engine.entityEngine().fetchAll(TEXTS)) {
            TextComponent textComponent = entity.get(TextComponent.class);
            Window window = engine.graphics().window();
            window.draw(textCentered(window.center(), textComponent.text, TITLE_FONT, Color.WHITE));
            Offset subtextOffset = Offset.at(window.center().x(), window.center().y() + 80);
            window.draw(textCentered(subtextOffset, textComponent.subtext, SUBTEXT_FONT, Color.WHITE));
        }
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_UI;
    }
}
