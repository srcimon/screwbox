package dev.screwbox.core.environment.light;

import dev.screwbox.core.Angle;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Color;

import java.io.Serial;

//TODO document
//TODO add to guide
public class DirectionalLightComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Color color = Color.BLACK;
    public Angle angle = Angle.none();
}