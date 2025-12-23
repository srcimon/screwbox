package dev.screwbox.core.environment.population;

public record Asssociate<T>(EntityBlueprint<T> blueprint, Object index) {

    public static <T> Asssociate<T> blueprint(EntityBlueprint<T> blueprint) {
        return new Asssociate<>(blueprint, null);
    }

    public Asssociate<T> withIndex(Object index) {
        return new Asssociate<>(blueprint, index);
    }
}
