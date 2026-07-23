package dev.screwbox.core.environment.smoke;

import dev.screwbox.core.environment.Component;

public class SmokeEmitterComponent implements Component {

    public double amount;

    public SmokeEmitterComponent(double amount) {
        this.amount = amount;
    }
}
