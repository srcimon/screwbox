package de.suzufa.screwbox.examples.helloworld.systems;

import static de.suzufa.screwbox.core.graphics.window.WindowText.textCentered;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.window.Window;

public class PrintHelloWorldSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        Font font = new Font("Arial", 36);
        Offset mousePosition = engine.mouse().position();

        Window window = engine.graphics().window();
        window.draw(textCentered(mousePosition, "Hello World", font));
    }

}
