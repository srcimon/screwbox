package dev.screwbox.core.environment.blueprits;

import dev.screwbox.core.environment.Condition;
import dev.screwbox.core.environment.Entity;

import java.util.List;
import java.util.Optional;

public class Assign<T, I> {

    private final Condition<T, I> condition;
    private final ComplexBlueprint<T> blueprint;

    private Assign(Condition<T, I> condition, ComplexBlueprint<T> blueprint) {
        this.condition = condition;
        this.blueprint = blueprint;
    }

    public Optional<ComplexBlueprint<T>> blueprintFor(T type, I index) {
        //TODO if applies
        return Optional.of(blueprint);
    }

    public static <T, I> Assign<T, I> index(I index, Blueprint<T> blueprint) {
        return condition(Condition.index(index), blueprint);
    }

    public static <T, I> Assign<T, I> condition(Condition<T, I> condition, Blueprint<T> blueprint) {
        return new Assign<>(condition, (source, context) -> List.of(blueprint.create(source)));
    }

    public static <T, I> Assign<T, I> condition(Condition<T, I> condition, ContextAwareBlueprint<T> blueprint) {
        return new Assign<>(condition, (source, context) -> List.of(blueprint.create(source, context)));
    }

    public static <T, I> Assign<T, I> condition(Condition<T, I> condition, ComplexBlueprint<T> blueprint) {
        return new Assign<>(condition, blueprint);
    }


}
