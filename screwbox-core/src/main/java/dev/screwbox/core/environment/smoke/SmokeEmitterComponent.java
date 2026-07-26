package dev.screwbox.core.environment.smoke;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Color;

import java.io.Serial;
//TODO document test
public class SmokeEmitterComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double amount;
    public Color color;

    public SmokeEmitterComponent(double amount, Color color) {
        this.amount = amount;
        this.color = color;
    }
}
