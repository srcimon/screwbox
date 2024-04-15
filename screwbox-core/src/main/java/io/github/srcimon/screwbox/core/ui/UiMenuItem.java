package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.Engine;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class UiMenuItem {

    private final Function<Engine, String> label;
    private Predicate<Engine> activeCondition = engine -> true;
    private Consumer<Engine> onActivate = engine -> {
    };

    UiMenuItem(final String label) {
        this.label = engine -> label;
    }

    UiMenuItem(final Function<Engine, String> dynamicLabel) {
        this.label = dynamicLabel;
    }

    public String label(final Engine engine) {
        return label.apply(engine);
    }

    public UiMenuItem onActivate(final Consumer<Engine> onActivate) {
        this.onActivate = onActivate;
        return this;
    }

    public void trigger(final Engine engine) {
        onActivate.accept(engine);
    }

    /**
     * Marks {@link UiMenuItem} als always disabled.
     *
     * @see #activeCondition(Predicate)
     */
    public UiMenuItem disabled() {
        activeCondition(engine -> false);
        return this;
    }

    /**
     * Sets the condition that determins if the {@link UiMenuItem} will be shown as active.
     *
     * @see #disabled()
     * @since 1.8.0
     */
    public UiMenuItem activeCondition(final Predicate<Engine> condition) {
        activeCondition = condition;
        return this;
    }

    /**
     * Returns true if the {@link UiMenuItem} is active. {@link Engine} is used to calculate that state.
     *
     * @see #activeCondition(Predicate)
     */
    public boolean isActive(final Engine engine) {
        return activeCondition.test(engine);
    }
}
