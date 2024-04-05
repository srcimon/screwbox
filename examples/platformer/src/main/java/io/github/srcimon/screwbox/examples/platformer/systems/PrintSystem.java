package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.SystemOrder;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.examples.platformer.components.TextComponent;

import static io.github.srcimon.screwbox.core.assets.BundledFonts.SCREWBOX;

@Order(SystemOrder.PRESENTATION_UI)
public class PrintSystem implements EntitySystem {

    private static final Archetype TEXTS = Archetype.of(TextComponent.class);

    @Override
    public void update(Engine engine) {
        for (var entity : engine.environment().fetchAll(TEXTS)) {
            TextComponent textComponent = entity.get(TextComponent.class);
            Screen screen = engine.graphics().screen();
            screen.drawTextCentered(screen.center(), textComponent.text, SCREWBOX.white(), Percent.max(), 7);
            Offset subtextOffset = Offset.at(screen.center().x(), screen.center().y() + 80);
            screen.drawTextCentered(subtextOffset, textComponent.subtext, SCREWBOX.white(), Percent.max(), 4);
        }
    }
}