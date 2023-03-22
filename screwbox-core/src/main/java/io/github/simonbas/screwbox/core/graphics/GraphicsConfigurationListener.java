package io.github.simonbas.screwbox.core.graphics;

import java.util.EventListener;

public interface GraphicsConfigurationListener extends EventListener {

    void configurationChanged(GraphicsConfigurationEvent event);
}
