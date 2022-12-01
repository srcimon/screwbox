package de.suzufa.screwbox.playground.debo.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.Order;
import de.suzufa.screwbox.core.entities.UpdatePriority;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Pixelfont;
import de.suzufa.screwbox.core.graphics.Screen;
import de.suzufa.screwbox.playground.debo.components.TextComponent;

@Order(UpdatePriority.PRESENTATION_UI)
public class PrintSystem implements EntitySystem {

    private static final Archetype TEXTS = Archetype.of(TextComponent.class);

    @Override
    public void update(Engine engine) {
        final Pixelfont font = Pixelfont.defaultFont(Color.WHITE);
        for (var entity : engine.entities().fetchAll(TEXTS)) {
            TextComponent textComponent = entity.get(TextComponent.class);
            Screen screen = engine.graphics().screen();
            screen.drawTextCentered(screen.center(), textComponent.text, font, Percent.max(), 7);
            Offset subtextOffset = Offset.at(screen.center().x(), screen.center().y() + 80);
            screen.drawTextCentered(subtextOffset, textComponent.subtext, font, Percent.max(), 4);
        }
    }
}