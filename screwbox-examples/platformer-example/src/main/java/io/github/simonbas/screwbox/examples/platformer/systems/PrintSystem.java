package io.github.simonbas.screwbox.examples.platformer.systems;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.Percent;
import io.github.simonbas.screwbox.core.entities.Archetype;
import io.github.simonbas.screwbox.core.entities.EntitySystem;
import io.github.simonbas.screwbox.core.entities.Order;
import io.github.simonbas.screwbox.core.entities.SystemOrder;
import io.github.simonbas.screwbox.core.graphics.Color;
import io.github.simonbas.screwbox.core.graphics.Offset;
import io.github.simonbas.screwbox.core.graphics.Pixelfont;
import io.github.simonbas.screwbox.core.graphics.Screen;
import io.github.simonbas.screwbox.examples.platformer.components.TextComponent;

@Order(SystemOrder.PRESENTATION_UI)
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