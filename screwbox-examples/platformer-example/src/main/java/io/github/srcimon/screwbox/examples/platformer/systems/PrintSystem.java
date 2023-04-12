package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.EntitySystem;
import io.github.srcimon.screwbox.core.entities.Order;
import io.github.srcimon.screwbox.core.entities.SystemOrder;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Pixelfont;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.examples.platformer.components.TextComponent;

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