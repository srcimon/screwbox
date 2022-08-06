package de.suzufa.screwbox.playground.debo.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.UpdatePriority;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Pixelfont;
import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.playground.debo.components.TextComponent;

public class PrintSystem implements EntitySystem {

    private static final Pixelfont FONT = Pixelfont.defaultFont(Color.WHITE);
    private static final Archetype TEXTS = Archetype.of(TextComponent.class);

    @Override
    public void update(Engine engine) {
        for (var entity : engine.entityEngine().fetchAll(TEXTS)) {
            TextComponent textComponent = entity.get(TextComponent.class);
            Window window = engine.graphics().window();
            window.drawTextCentered(window.center(), textComponent.text, FONT, Percentage.max(), 7);
            Offset subtextOffset = Offset.at(window.center().x(), window.center().y() + 80);
            window.drawTextCentered(subtextOffset, textComponent.subtext, FONT, Percentage.max(), 4);
        }
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_UI;
    }
}
