package dev.screwbox.core.environment;

/**
 * The order of execution of an {@link EntitySystem}.
 */
public enum Order {
    OPTIMIZATION,
    PREPARATION,
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

    public int drawOrder() {
        return ordinal() * 1000_000;
    }

    public int orderPlus(int above) {
        return drawOrder() + above;
    }
}
