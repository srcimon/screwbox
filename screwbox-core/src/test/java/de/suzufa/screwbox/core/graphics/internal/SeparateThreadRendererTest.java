package de.suzufa.screwbox.core.graphics.internal;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Offset;

@ExtendWith(MockitoExtension.class)
class SeparateThreadRendererTest {

    @Mock
    Renderer renderer;

    @InjectMocks
    SeparateThreadRenderer separateThreadRenderer;

    @Test
    void applyDrawActions_noUpdate_nextRendererNotInvoked() {
        separateThreadRenderer.drawLine(Offset.origin(), Offset.at(10, 20), Color.YELLOW);

        verify(renderer, never()).drawLine(Offset.origin(), Offset.at(10, 20), Color.YELLOW);
        verify(renderer, never()).updateScreen(anyBoolean());
    }

    @Test
    void applyDrawActions_update_nextRendererInvoked() {
        separateThreadRenderer.drawPolygon(emptyList(), Color.BLACK);
        separateThreadRenderer.drawCircle(Offset.origin(), 25, Color.BLUE);

        separateThreadRenderer.updateScreen(true);

        separateThreadRenderer.close();

        verify(renderer).drawPolygon(emptyList(), Color.BLACK);
        verify(renderer).drawCircle(Offset.origin(), 25, Color.BLUE);
        verify(renderer).updateScreen(true);
    }
}
