package dev.screwbox.core.environment.population;

public record Associate<T>(EntityBlueprint<T> blueprint, Object index) {

    public static <T> Associate<T> blueprint(EntityBlueprint<T> blueprint) {
        return new Associate<>(blueprint, null);
    }

    public Associate<T> withIndex(Object index) {
        return new Associate<>(blueprint, index);
    }
}
