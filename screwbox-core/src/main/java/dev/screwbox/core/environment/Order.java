package dev.screwbox.core.environment;

/**
 * The order of execution of an {@link EntitySystem}. Use {@link ExecutionOrder} annotation to specify order.
 * Order is determined by the {@link #ordinal()} of the enum value.
 *
 * @see <a href="https://screwbox.dev/docs/fundamentals/ecs#embedded-ecs">Documentation</a>
 */
public enum Order {
    OPTIMIZATION,
    PREPARATION,
    SIMULATION_PREPARE,
    SIMULATION_EARLY,
    SIMULATION,
    SIMULATION_LATE,
    PRESENTATION_PREPARE,
    PRESENTATION_BACKGROUND,
    PRESENTATION_WORLD,
    PRESENTATION_EFFECTS,
    PRESENTATION_LIGHT,
    PRESENTATION_FOREGROUND,
    PRESENTATION_OVERLAY,
    PRESENTATION_UI_BACKGROUND,
    PRESENTATION_UI,
    PRESENTATION_UI_FOREGROUND,
    PRESENTATION_TRANSITIONS,
    PRESENTATION_NOTIFICATIONS,
    PRESENTATION_UI_MENU,
    DEBUG_OVERLAY_EARLY,
    DEBUG_OVERLAY,
    DEBUG_OVERLAY_LATE;

    /**
     * Returns the draw order for all drawing calls resulting from an execution within this order.
     *
     * @since 3.14.0
     */
    public int drawOrder() {
        return ordinal() * 1000_000;
    }

    /**
     * Returns the draw order within the specified execution order.
     * Used to create drawing tasks outside of the current execution scope. E.g. drawing ui elements from within the
     * the simulation phase of the execution.
     *
     * @since 3.14.0
     */
    public int mixinDrawOrder(final int oder) {
        return drawOrder() + oder;
    }
}
