package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.test.EnvironmentExtension;
import dev.screwbox.core.ui.Ui;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith({EnvironmentExtension.class, MockitoExtension.class})
class RenderUiSystemTest {


    @Test
    void update_rendersUi(DefaultEnvironment environment, Ui ui) {
        environment.addSystem(new RenderUiSystem());
        environment.update();

        verify(ui).renderMenu();
    }
}
