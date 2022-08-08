package de.suzufa.screwbox.core.graphics.internal;

import java.util.List;
import java.util.concurrent.ExecutorService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.graphics.GraphicsConfiguration;
import de.suzufa.screwbox.core.graphics.Sprite;

class DefaultWindowTest {

    // TODO: frame.invisible() / Sprite.invisible <- uses frame.invisible()
    // TODO: REMOVE
    @Test
    void canThisRunInContainer() {
        DefaultWindow window = new DefaultWindow(new WindowFrame(Mockito.mock(Engine.class)),
                new GraphicsConfiguration(),
                Mockito.mock(ExecutorService.class));

        Assertions
                .assertThatThrownBy(() -> window.setCursor(
                        new Sprite(List.of(Sprite.invisible().singleFrame(), Sprite.invisible().singleFrame()))))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
