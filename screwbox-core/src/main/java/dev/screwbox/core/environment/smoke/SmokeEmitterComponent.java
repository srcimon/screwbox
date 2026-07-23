package dev.screwbox.core.environment.smoke;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Color;

public class SmokeEmitterComponent implements Component {

    public double amount;
    public Color color;

    public SmokeEmitterComponent(double amount, Color color) {
        this.amount = amount;
        this.color = color;
    }
}
